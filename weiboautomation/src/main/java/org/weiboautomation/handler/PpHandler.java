package org.weiboautomation.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.weiboautomation.entity.Blog;
import org.weiboautomation.handler.exception.HandlerException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class PpHandler {

	private ObjectMapper objectMapper;

	public void initialize() {
		objectMapper = new ObjectMapper();
	}

	private JsonNode getJsonNode(String result) throws HandlerException {
		int beginIndex = result.indexOf("(");
		int endIndex = result.lastIndexOf(")");

		JsonNode jsonNode;

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

	public String getSid(HttpClient httpClient, String username, String password)
			throws HandlerException {
		String result;

		StringBuilder url = new StringBuilder();
		url.append("http://login.pp.cc/login/plogin");
		url.append("?");
		url.append("u");
		url.append("=");
		url.append(username);
		url.append("&");
		url.append("p");
		url.append("=");
		url.append(password);

		HttpGet get = new HttpGet(url.toString());

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

		JsonNode jsonNode = getJsonNode(result);

		String sid = jsonNode.get("sid").asText();

		return sid;
	}

	private Elements getBlogElements(HttpClient httpClient, String sid,
			int tid, int tidPageIndex) throws HandlerException {
		byte[] result;

		StringBuilder url = new StringBuilder();
		url.append("http://t.pp.cc/time/index.php");
		url.append("?");
		url.append("mod");
		url.append("=");
		url.append("library");
		url.append("&");
		url.append("action");
		url.append("=");
		url.append("show");
		url.append("&");
		url.append("account");
		url.append("=");
		url.append("2690456720");
		url.append("&");
		url.append("tid");
		url.append("=");
		url.append(tid);
		url.append("&");
		url.append("page");
		url.append("=");
		url.append(tidPageIndex);
		url.append("&");
		url.append("offset");
		url.append("=");
		url.append("1");

		HttpGet get = new HttpGet(url.toString());

		get.setHeader("Cookie", "uc_sid=" + sid + ";");

		try {
			HttpResponse response = httpClient.execute(get);

			result = EntityUtils.toByteArray(response.getEntity());
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

		String html = jsonNode.get("html").asText();

		Document doc = Jsoup.parse(html);

		Elements divs = doc.select(".b_nrk_body");

		return divs;
	}

	public int getTidPageSize(HttpClient httpClient, String sid, int tid,
			int tidPageSize) throws HandlerException {
		int tidPageIndex = tidPageSize;

		tidPageIndex++;

		while (true) {
			Elements divs = getBlogElements(httpClient, sid, tid, tidPageIndex);

			if (divs.isEmpty()) {
				break;
			}

			tidPageIndex++;

			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		return tidPageIndex - 1;
	}

	public List<Blog> getBlogList(HttpClient httpClient, String sid, int tid,
			int tidPageIndex) throws HandlerException {
		Elements divs = getBlogElements(httpClient, sid, tid, tidPageIndex);

		List<Blog> blogList = new ArrayList<Blog>();

		for (Element div : divs) {
			Element textElement = div.select(".b_nrk_nr").get(0);
			Element pictureElement = div.select(".nrk_pic").get(0).child(0);

			String text = textElement.text();
			String picture = pictureElement.attr("href");

			Blog blog = new Blog();

			blog.setText(text);
			blog.setPicture(picture);

			blogList.add(blog);
		}

		return blogList;
	}

}
