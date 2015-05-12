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
import org.apache.commons.lang3.math.NumberUtils;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboGetGroupUsersTest {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\(.*\\)");

	private ObjectMapper objectMapper;
	private DefaultHttpClient defaultHttpClient;

	public WeiboGetGroupUsersTest() {
		objectMapper = new ObjectMapper();
		defaultHttpClient = new DefaultHttpClient();
	}

	public class User {

		private String sn;
		private String name;

		public String getSn() {
			return sn;
		}

		public void setSn(String sn) {
			this.sn = sn;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

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

	private String getGroupUserListHtmlByGroupIdAndPage(String groupId, int page) {
		String html = null;

		for (int i = 0; i < 10; i++) {
			String url = "http://q.weibo.com/ajax/members/page";

			HttpPost post = new HttpPost(url);

			post.addHeader("Referer", "http://q.weibo.com/" + groupId
					+ "/members/all");

			List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

			nameValuePairList.add(new BasicNameValuePair("page", String
					.valueOf(page)));
			nameValuePairList.add(new BasicNameValuePair("gid", groupId));
			nameValuePairList.add(new BasicNameValuePair("vip", "1"));
			nameValuePairList.add(new BasicNameValuePair("query", ""));
			nameValuePairList.add(new BasicNameValuePair("tab", "all"));

			HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList,
					Charsets.UTF_8);

			post.setEntity(httpEntity);

			byte[] result = null;

			try {
				HttpResponse response = defaultHttpClient.execute(post);

				int statusCode = response.getStatusLine().getStatusCode();

				if (statusCode == HttpStatus.SC_OK) {
					result = EntityUtils.toByteArray(response.getEntity());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				post.releaseConnection();
			}

			JsonNode jsonNode = null;

			try {
				jsonNode = objectMapper.readTree(result);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("Page " + page + ": " + jsonNode.toString());

			String code = jsonNode.get("code").asText();

			if ("100000".equals(code)) {
				html = jsonNode.get("data").get("html").asText();

				break;
			}
		}

		return html;
	}

	private int getGroupUserListPageSizeByGroupId(String groupId) {
		int pageSize = 0;

		String html = getGroupUserListHtmlByGroupIdAndPage(groupId, 1);

		if (html != null) {
			Document doc = Jsoup.parse(html);

			Elements divElements = doc.getElementsByTag("div");
			Element divElement = divElements.get(divElements.size() - 1);

			Elements anchorElements = divElement.getElementsByTag("a");
			Element anchorElement = anchorElements
					.get(anchorElements.size() - 2);

			String text = anchorElement.text();

			pageSize = NumberUtils.toInt(text);
		}

		return pageSize;
	}

	private List<User> getGroupUserListByGroupIdAndPage(String groupId, int page) {
		List<User> userList = new ArrayList<User>();

		String html = getGroupUserListHtmlByGroupIdAndPage(groupId, page);

		if (html != null) {
			Document doc = Jsoup.parse(html);

			Elements liElements = doc.getElementsByTag("li");

			for (Element element : liElements) {
				Element anchorElement = element.getElementsByTag("a").get(0);

				String userSn = anchorElement.attr("uid");
				String userName = anchorElement.attr("title");

				User user = new User();
				user.setSn(userSn);
				user.setName(userName);

				userList.add(user);
			}
		}

		return userList;
	}

	private void getGroupUserListByGroupId(String groupId, String groupUsersFile) {
		int pageSize = getGroupUserListPageSizeByGroupId(groupId);

		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		List<User> userList = new ArrayList<User>();

		for (int page = 1; page <= pageSize; page++) {
			userList.addAll(getGroupUserListByGroupIdAndPage(groupId, page));

			System.out.println("Page " + page + " OK");

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		try {
			objectMapper.writeValue(new File(groupUsersFile), userList);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input cookiesJsonFile: ");
		String cookiesJsonFile = scanner.nextLine();

		System.out.println("Input groupId: ");
		String groupId = scanner.nextLine();

		System.out.println("Input groupUsersFile: ");
		String groupUsersFile = scanner.nextLine();

		WeiboGetGroupUsersTest weiboGetGroupUsersTest = new WeiboGetGroupUsersTest();

		weiboGetGroupUsersTest.setCookies(cookiesJsonFile);

		weiboGetGroupUsersTest.login();

		weiboGetGroupUsersTest.getGroupUserListByGroupId(groupId,
				groupUsersFile);
	}

}
