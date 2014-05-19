package org.weiboautomation.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.weiboautomation.entity.User;
import org.weiboautomation.handler.exception.HandlerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class WeiboApiHandler {

	private static final Pattern accessTokenPattern = Pattern
			.compile("2\\.00(.{28})");

	private ObjectMapper objectMapper;

	public void initialize() {
		objectMapper = new ObjectMapper();
	}

	public String getAccessToken(HttpClient httpClient) throws HandlerException {
		String html;

		HttpGet get = new HttpGet(
				"https://api.weibo.com/oauth2/authorize?client_id=707016719&redirect_uri=http://fansmaster.sinaapp.com/static/frame/weibo_phpsdk/callback.php");

		try {
			HttpResponse response = httpClient.execute(get);

			html = EntityUtils.toString(response.getEntity(), "UTF-8");
		} catch (ClientProtocolException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		} finally {
			get.releaseConnection();
		}

		String accessToken;

		Matcher matcher = accessTokenPattern.matcher(html);

		if (matcher.find()) {
			accessToken = matcher.group(0);
		} else {
			throw new HandlerException("GetAccessToken failed");
		}

		return accessToken;
	}

	public List<User> getUserListByUserSn(HttpClient httpClient,
			String accessToken, String userSn, int cursor, int size)
			throws HandlerException {
		byte[] result;

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
			HttpResponse response = httpClient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toByteArray(response.getEntity());
			} else {
				throw new HandlerException(String.valueOf(statusCode));
			}
		} catch (ClientProtocolException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		} finally {
			get.releaseConnection();
		}

		JsonNode jsonNode;

		try {
			jsonNode = objectMapper.readTree(result);
		} catch (JsonProcessingException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		}

		List<User> userList;

		ArrayNode userSnsArrayNode = (ArrayNode) jsonNode.get("ids");

		if (userSnsArrayNode != null) {
			userList = new ArrayList<User>();

			for (int i = 0; i < userSnsArrayNode.size(); i++) {
				User user = new User();

				user.setSn(userSnsArrayNode.get(i).asText());

				userList.add(user);
			}
		} else {
			throw new HandlerException("GetUserListByUserSn failed");
		}

		return userList;
	}

}
