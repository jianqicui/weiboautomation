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
import org.apache.http.HttpEntity;
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
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboApiPublishBlogTest {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\('.*'\\)");

	private ObjectMapper objectMapper;
	private DefaultHttpClient defaultHttpClient;

	public WeiboApiPublishBlogTest() {
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
				expiryDate = new Date((long) map.get("expiryDate"));
			}

			String path = (String) map.get("path");
			boolean secure = (boolean) map.get("secure");
			int version = (int) map.get("version");

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

	private void authorize() {
		String url = "https://api.weibo.com/oauth2/authorize?client_id=3144078080&redirect_uri=http://batchhelper.sinaapp.com/";

		HttpGet get = new HttpGet(url);

		try {
			defaultHttpClient.execute(get);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}
	}

	private void publishBlog(String text, String picture) {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("text", text));
		nameValuePairList.add(new BasicNameValuePair("picturePath", picture));

		HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8);

		String url = "http://batchhelper.sinaapp.com/action.php?action=uploadStatus";

		HttpPost post = new HttpPost(url);

		post.setEntity(httpEntity);

		try {
			HttpResponse response = defaultHttpClient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();

			System.out.println(statusCode);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			post.releaseConnection();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input cookiesJsonFile: ");
		String cookiesJsonFile = scanner.nextLine();

		System.out.println("Input text: ");
		String text = scanner.nextLine();

		System.out.println("Input picture: ");
		String picture = scanner.nextLine();

		WeiboApiPublishBlogTest weiboApiPublishBlogTest = new WeiboApiPublishBlogTest();

		weiboApiPublishBlogTest.setCookies(cookiesJsonFile);

		weiboApiPublishBlogTest.login();

		weiboApiPublishBlogTest.authorize();

		weiboApiPublishBlogTest.publishBlog(text, picture);
	}

}
