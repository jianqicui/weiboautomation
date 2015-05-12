package org.weiboautomation;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboAddMessageTest {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\(.*\\)");

	private ObjectMapper objectMapper;
	private DefaultHttpClient defaultHttpClient;

	public WeiboAddMessageTest() {
		objectMapper = new ObjectMapper();
		defaultHttpClient = new DefaultHttpClient();
	}

	private void setCookies(String cookiesJsonFile) {
		List<Map<String, Object>> list = null;

		try {
			list = objectMapper.readValue(new File(cookiesJsonFile),
					new TypeReference<List<Map<String, Object>>>() {
					});
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		BasicCookieStore basicCookieStore = new BasicCookieStore();

		for (Map<String, Object> map : list) {
			String name = (String) map.get("name");
			String value = (String) map.get("value");

			String comment = (String) map.get("comment");
			String domain = (String) map.get("domain");

			Date expiryDate = null;
			if (map.get("expiryDate") != null) {
				expiryDate = new Date((Long) map.get("expiryDate"));
			}

			String path = (String) map.get("path");
			boolean secure = (Boolean) map.get("secure");
			int version = (Integer) map.get("version");

			BasicClientCookie basicClientCookie = new BasicClientCookie(name,
					value);

			basicClientCookie.setComment(comment);
			basicClientCookie.setDomain(domain);
			basicClientCookie.setExpiryDate(expiryDate);
			basicClientCookie.setPath(path);
			basicClientCookie.setSecure(secure);
			basicClientCookie.setVersion(version);

			basicCookieStore.addCookie(basicClientCookie);
		}

		defaultHttpClient.setCookieStore(basicCookieStore);
	}

	private String get(String url) {
		String result = null;

		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = defaultHttpClient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(),
						Charsets.UTF_8);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		return result;
	}

	private void login() {
		URI uri = null;

		try {
			uri = new URIBuilder().setScheme("http")
					.setHost("login.sina.com.cn").setPath("/sso/login.php")
					.setParameter("url", "http://weibo.com/")
					.addParameter("gateway", "1")
					.setParameter("service", "miniblog")
					.setParameter("entry", "miniblog")
					.setParameter("useticket", "1")
					.setParameter("returntype", "META").build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		String result = get(uri.toString());

		Matcher matcher = locationReplacePattern.matcher(result);

		String url = null;

		if (matcher.find()) {
			result = matcher.group();

			url = result.substring(18, result.length() - 2);
		}

		get(url);
	}

	private void addMessage(String userName, String text) {
		HttpPost post = new HttpPost("http://www.weibo.com/aj/message/add");

		post.setHeader("Referer", "http://www.weibo.com/notesboard?leftnav=1");

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("text", "你好，" + userName
				+ "。" + text));
		nameValuePairList.add(new BasicNameValuePair("screen_name", userName));
		nameValuePairList.add(new BasicNameValuePair("id", "0"));
		nameValuePairList.add(new BasicNameValuePair("tovfids", ""));
		nameValuePairList.add(new BasicNameValuePair("fids", ""));
		nameValuePairList.add(new BasicNameValuePair("touid", "0"));
		nameValuePairList.add(new BasicNameValuePair("style_id", "2"));
		nameValuePairList.add(new BasicNameValuePair("location", ""));
		nameValuePairList.add(new BasicNameValuePair("module", "msglayout"));
		nameValuePairList.add(new BasicNameValuePair("is_notesboard", "1"));
		nameValuePairList.add(new BasicNameValuePair("current_mid", ""));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8));

		try {
			HttpResponse response = defaultHttpClient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();

			JsonNode jsonNode = objectMapper.readTree(EntityUtils
					.toByteArray(response.getEntity()));

			System.out.println(userName + " " + statusCode + " " + jsonNode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
	}

	private void addMessage(List<String> userNameList, String text) {
		for (String userName : userNameList) {
			addMessage(userName, text);

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input cookiesJsonFile: ");
		String cookiesJsonFile = scanner.nextLine();

		System.out.println("Input userNamesFile: ");
		String userNamesFile = scanner.nextLine();

		System.out.println("Input textFile: ");
		String textFile = scanner.nextLine();

		WeiboAddMessageTest weiboAddMessageTest = new WeiboAddMessageTest();

		weiboAddMessageTest.setCookies(cookiesJsonFile);

		weiboAddMessageTest.login();

		List<String> userNameList = null;

		try {
			userNameList = FileUtils.readLines(new File(userNamesFile),
					Charsets.UTF_8.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		String text = null;

		try {
			text = FileUtils.readFileToString(new File(textFile),
					Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		weiboAddMessageTest.addMessage(userNameList, text);
	}

}
