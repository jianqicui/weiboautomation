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
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
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

public class WeiboGetFollowersTest {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\(.*\\)");

	private ObjectMapper objectMapper;
	private DefaultHttpClient defaultHttpClient;

	public WeiboGetFollowersTest() {
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

	private JsonNode getJsonNode(String result) {
		int beginIndex = result.indexOf("(");
		int endIndex = result.lastIndexOf(")");

		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(result.substring(beginIndex + 1,
					endIndex));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return jsonNode;
	}

	private List<User> getFollowedUserListByUserSn(String userSn, int page) {
		List<User> userList = new ArrayList<User>();

		String url = "http://www.weibo.com/" + userSn + "/myfans?t=1&f=1&page="
				+ page;

		String result = get(url);

		Document doc = Jsoup.parse(result);

		Elements scripts = doc.getElementsByTag("script");

		if (!scripts.isEmpty()) {
			String scriptHtml = null;

			for (Element script : scripts) {
				if (script.html().contains("pl_relation_fans")) {
					scriptHtml = script.html();

					break;
				}
			}

			if (scriptHtml != null) {
				JsonNode jsonNode = getJsonNode(scriptHtml);

				JsonNode htmlJsonNode = jsonNode.get("html");

				if (htmlJsonNode != null) {
					String html = htmlJsonNode.asText();

					Document htmlDoc = Jsoup.parse(html);

					Elements faceDivs = htmlDoc.select(".face");

					for (Element faceDiv : faceDivs) {
						Element anchorElement = faceDiv.child(0);

						String usercard = anchorElement.attr("usercard");

						String sn = StringUtils.split(usercard, "=")[1];

						String name = anchorElement.attr("title");

						User user = new User();
						user.setSn(sn);
						user.setName(name);

						userList.add(user);
					}
				}
			}
		}

		return userList;
	}

	private void getFollowedUserListByUserSn(String userSn,
			String followedUsersFile) {
		List<User> userList = new ArrayList<User>();

		int page = 1;

		while (true) {
			List<User> vUserList = getFollowedUserListByUserSn(userSn, page);

			if (vUserList.isEmpty()) {
				break;
			}

			System.out.println("Page " + page + " OK");

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			userList.addAll(vUserList);

			page++;
		}

		try {
			objectMapper.writeValue(new File(followedUsersFile), userList);
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

		System.out.println("Input userSn: ");
		String userSn = scanner.nextLine();

		System.out.println("Input followedUsersFile: ");
		String followedUsersFile = scanner.nextLine();

		WeiboGetFollowersTest weiboGetFollowersTest = new WeiboGetFollowersTest();

		weiboGetFollowersTest.setCookies(cookiesJsonFile);

		weiboGetFollowersTest.login();

		weiboGetFollowersTest.getFollowedUserListByUserSn(userSn,
				followedUsersFile);
	}
}
