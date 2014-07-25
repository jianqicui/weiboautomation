package org.weiboautomation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class FollowersTest {

	private static final Pattern accessTokenPattern = Pattern
			.compile("2\\.00(.{28})");

	private ObjectMapper objectMapper;

	private DefaultHttpClient defaultHttpClient;

	public FollowersTest() {
		objectMapper = new ObjectMapper();

		defaultHttpClient = new DefaultHttpClient();
	}

	private void loadCookies(String cookiesFile) {
		List<Map<String, Object>> list = null;

		try {
			list = objectMapper.readValue(new File(cookiesFile),
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
			String path = (String) map.get("path");
			String comment = (String) map.get("comment");
			int version = (int) map.get("version");
			String domain = (String) map.get("domain");
			boolean secure = (boolean) map.get("secure");

			Date expiryDate = null;

			if (map.get("expiryDate") != null) {
				expiryDate = new Date((long) map.get("expiryDate"));
			}

			BasicClientCookie basicClientCookie = new BasicClientCookie(name,
					value);
			basicClientCookie.setPath(path);
			basicClientCookie.setComment(comment);
			basicClientCookie.setVersion(version);
			basicClientCookie.setDomain(domain);
			basicClientCookie.setSecure(secure);
			basicClientCookie.setExpiryDate(expiryDate);

			basicCookieStore.addCookie(basicClientCookie);
		}

		defaultHttpClient.setCookieStore(basicCookieStore);
	}

	public String getAccessToken() {
		String html = null;

		HttpGet get = new HttpGet(
				"https://api.weibo.com/oauth2/authorize?client_id=707016719&redirect_uri=http://fansmaster.sinaapp.com/static/frame/weibo_phpsdk/callback.php");

		try {
			HttpResponse response = defaultHttpClient.execute(get);

			html = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		String accessToken = null;

		Matcher matcher = accessTokenPattern.matcher(html);

		if (matcher.find()) {
			accessToken = matcher.group(0);
		}

		return accessToken;
	}

	public List<String> getFollowedSnListByUserSn(String accessToken,
			String userSn, int cursor, int size) {
		byte[] result = null;

		StringBuilder url = new StringBuilder();

		url.append("https://api.weibo.com/2/friendships/followers/ids.json");
		url.append("?");
		url.append("uid");
		url.append("=");
		url.append(userSn);
		url.append("&");
		url.append("cursor");
		url.append("=");
		url.append(cursor);
		url.append("&");
		url.append("count");
		url.append("=");
		url.append(size);
		url.append("&");
		url.append("access_token");
		url.append("=");
		url.append(accessToken);

		HttpGet get = new HttpGet(url.toString());

		try {
			HttpResponse response = defaultHttpClient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toByteArray(response.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> snList = null;

		ArrayNode userSnsArrayNode = (ArrayNode) jsonNode.get("ids");

		if (userSnsArrayNode != null) {
			snList = new ArrayList<String>();

			for (int i = 0; i < userSnsArrayNode.size(); i++) {
				String sn = userSnsArrayNode.get(i).asText();

				snList.add(sn);
			}
		}

		return snList;
	}

	private void getFollowers(String cookiesFile, String followersFile,
			String usersFile, String tableName) {
		loadCookies(cookiesFile);

		String accessToken = getAccessToken();

		List<String> userSnList = null;

		try {
			userSnList = FileUtils.readLines(new File(followersFile),
					Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}

		Set<String> snSet = new LinkedHashSet<String>();

		for (String userSn : userSnList) {
			List<String> snList = getFollowedSnListByUserSn(accessToken,
					userSn, 0, 2000);

			System.out.println(String.format("userSn = %s", userSn));
			System.out.println(String.format("snListSize = %s", snList.size()));

			snSet.addAll(snList);
		}

		StringBuilder sql = new StringBuilder();

		sql.append("insert into " + tableName + " (sn) values ");
		sql.append("\n");

		int i = 0;

		for (String sn : snSet) {

			if (i != snSet.size() - 1) {
				sql.append("('" + sn + "'),");
				sql.append("\n");
			} else {
				sql.append("('" + sn + "');");
			}

			i++;
		}

		try {
			FileUtils
					.write(new File(usersFile), sql.toString(), Charsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input cookies file: ");
		String cookiesFile = scanner.nextLine();

		System.out.println("Input followers file: ");
		String followersFile = scanner.nextLine();

		System.out.println("Input users file: ");
		String usersFile = scanner.nextLine();

		System.out.println("Input table name: ");
		String tableName = scanner.nextLine();

		FollowersTest followersTest = new FollowersTest();

		followersTest.getFollowers(cookiesFile, followersFile, usersFile,
				tableName);
	}

}
