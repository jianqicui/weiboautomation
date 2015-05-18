package org.weiboautomation.handler;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.Charsets;
import org.apache.commons.lang3.time.DateFormatUtils;
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
import org.weiboautomation.entity.MicroTask;
import org.weiboautomation.handler.exception.HandlerException;

public class MicroTaskHandler {

	private static final Pattern locationReplacePattern = Pattern
			.compile("location.replace\\(.*\\)");

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

	public void weiboAuth(HttpClient httpClient) throws HandlerException {
		get(httpClient, "http://weirenwu.weibo.com/taskv2/?c=index.wboauth");
	}

	public List<String> getMicroTaskIdList(HttpClient httpClient)
			throws HandlerException {
		List<String> microTaskIdList = new ArrayList<String>();

		String result = get(httpClient,
				"http://weirenwu.weibo.com/taskv2/index.php?c=Cpcs.cpcsSquare");

		Document doc = Jsoup.parse(result);

		Elements anchorElements = doc.select(".picBox160");

		for (Element anchorElement : anchorElements) {
			String href = anchorElement.attr("href");

			String microTaskId = href.replace("?c=cpcs.createOrder&tid=", "");

			microTaskIdList.add(microTaskId);
		}

		return microTaskIdList;
	}

	public List<MicroTask> getMicroTaskList(HttpClient httpClient, String id)
			throws HandlerException {
		List<MicroTask> microTaskList = new ArrayList<MicroTask>();

		String result = get(httpClient,
				"http://weirenwu.weibo.com/taskv2/index.php?c=cpcs.createOrder&tid="
						+ id);

		Document doc = Jsoup.parse(result);

		Element userIdSelectElement = doc.getElementById("userid");
		Element selectedOptionElement = userIdSelectElement
				.getElementsByAttribute("selected").get(0);
		String uid = selectedOptionElement.attr("value");

		Elements inputElements = doc.select(".ljbtn");

		for (int i = 0; i < inputElements.size(); i++) {
			int forwardId = i;
			int sendType = 1;

			MicroTask microTask = new MicroTask();
			microTask.setId(id);
			microTask.setUid(uid);
			microTask.setForwardId(forwardId);
			microTask.setSendType(sendType);

			microTaskList.add(microTask);
		}

		return microTaskList;
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

	public void publishMicroTask(HttpClient httpClient, MicroTask microTask)
			throws HandlerException {
		HttpPost post = new HttpPost(
				"http://weirenwu.weibo.com/taskv2/index.php?c=Ajax_Task.createCpcsOrder");

		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("tid", microTask.getId()));
		nameValuePairList
				.add(new BasicNameValuePair("uid", microTask.getUid()));
		nameValuePairList.add(new BasicNameValuePair("forwardId", String
				.valueOf(microTask.getForwardId())));
		nameValuePairList.add(new BasicNameValuePair("sendType", String
				.valueOf(microTask.getSendType())));
		nameValuePairList.add(new BasicNameValuePair("date", DateFormatUtils
				.format(new Date(), "yyyy-MM-dd HH:mm:ss")));

		post.setEntity(new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8));

		post(httpClient, post);
	}

}
