package org.weiboautomation.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.weiboautomation.handler.exception.HandlerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboHandler {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\('.*'\\)");

	private ObjectMapper objectMapper;

	public void initialize() {
		objectMapper = new ObjectMapper();
	}

	private String get(HttpClient httpClient, String url)
			throws HandlerException {
		String result;

		HttpGet get = new HttpGet(url);

		try {
			HttpResponse response = httpClient.execute(get);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode == HttpStatus.SC_OK) {
				result = EntityUtils.toString(response.getEntity(),
						Charsets.UTF_8);
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

		return result;
	}

	public void login(HttpClient httpClient) throws HandlerException {
		URI uri;

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
			throw new HandlerException(e);
		}

		String result = get(httpClient, uri.toString());

		Matcher matcher = locationReplacePattern.matcher(result);

		String url;

		if (matcher.find()) {
			result = matcher.group();

			url = result.substring(18, result.length() - 2);
		} else {
			throw new HandlerException("Login failed");
		}

		get(httpClient, url);
	}

	private byte[] post(HttpClient httpClient, HttpPost post)
			throws HandlerException {
		byte[] result;

		try {
			HttpResponse response = httpClient.execute(post);

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
			post.releaseConnection();
		}

		return result;
	}

	private void analyzeResult(byte[] result) throws HandlerException {
		JsonNode jsonNode;

		try {
			jsonNode = objectMapper.readTree(result);
		} catch (JsonProcessingException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		}

		String code = jsonNode.get("code").asText();

		if (!"100000".equals(code)) {
			throw new HandlerException(code);
		}
	}

	public void follow(HttpClient httpClient, String userSn)
			throws HandlerException {
		HttpPost post = new HttpPost("http://www.weibo.com/aj/f/followed");

		post.addHeader(HttpHeaders.REFERER, "http://www.weibo.com/" + userSn);

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

		nameValuePairList.add(new BasicNameValuePair("uid", userSn));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8));

		byte[] result = post(httpClient, post);

		analyzeResult(result);
	}

	public void unfollow(HttpClient httpClient, String userSn)
			throws HandlerException {
		HttpPost post = new HttpPost("http://www.weibo.com/aj/f/unfollow");

		post.addHeader(HttpHeaders.REFERER, "http://www.weibo.com/" + userSn);

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();

		nameValuePairList.add(new BasicNameValuePair("uid", userSn));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8));

		byte[] result = post(httpClient, post);

		analyzeResult(result);
	}

}