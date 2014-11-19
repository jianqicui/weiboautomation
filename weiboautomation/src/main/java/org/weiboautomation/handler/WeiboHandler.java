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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
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

	public void addMessage(HttpClient httpClient, String userName, String text)
			throws HandlerException {
		HttpPost post = new HttpPost("http://www.weibo.com/aj/message/add");

		post.setHeader("Referer", "http://www.weibo.com/notesboard?leftnav=1");

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("text", text));
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

		byte[] result = post(httpClient, post);

		analyzeResult(result);
	}

	private JsonNode getJsonNode(String result) throws HandlerException {
		int beginIndex = result.indexOf("(");
		int endIndex = result.lastIndexOf(")");

		JsonNode jsonNode = null;

		try {
			jsonNode = objectMapper.readTree(result.substring(beginIndex + 1,
					endIndex));
		} catch (JsonProcessingException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		}

		return jsonNode;
	}

	public List<String> getBlogSnList(HttpClient httpClient, String userSn)
			throws HandlerException {
		List<String> blogSnList = new ArrayList<String>();

		String result = get(httpClient, "http://www.weibo.com/u/" + userSn);

		Document doc = Jsoup.parse(result);

		Elements scripts = doc.getElementsByTag("script");

		String scriptHtml = null;

		for (Element script : scripts) {
			if (script.html().contains("WB_detail")) {
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

				Elements htmlDivs = htmlDoc.select(".WB_feed_type");

				for (Element htmlDiv : htmlDivs) {
					String blogSn = htmlDiv.attr("mid");

					blogSnList.add(blogSn);
				}
			} else {
				throw new HandlerException("GetBlogSnList failed");
			}
		} else {
			throw new HandlerException("GetBlogSnList failed");
		}

		return blogSnList;
	}

	public void addComment(HttpClient httpClient, String blogSn, String text)
			throws HandlerException {
		HttpPost post = new HttpPost("http://www.weibo.com/aj/comment/add");

		post.setHeader("Referer", "http://www.weibo.com");

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("act", "post"));
		nameValuePairList.add(new BasicNameValuePair("mid", blogSn));
		nameValuePairList.add(new BasicNameValuePair("forward", "0"));
		nameValuePairList.add(new BasicNameValuePair("isroot", "0"));
		nameValuePairList.add(new BasicNameValuePair("content", text));
		nameValuePairList.add(new BasicNameValuePair("location",
				"page_100505_home"));
		nameValuePairList.add(new BasicNameValuePair("module", "scommlist"));
		nameValuePairList.add(new BasicNameValuePair("group_source", ""));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8));

		byte[] result = post(httpClient, post);

		analyzeResult(result);
	}

}