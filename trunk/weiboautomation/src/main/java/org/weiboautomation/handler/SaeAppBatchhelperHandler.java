package org.weiboautomation.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.weiboautomation.entity.Blog;
import org.weiboautomation.handler.exception.HandlerException;

public class SaeAppBatchhelperHandler {

	public void authorize(HttpClient httpClient) throws HandlerException {
		String url = "https://api.weibo.com/oauth2/authorize?client_id=3144078080&redirect_uri=http://batchhelper.sinaapp.com/";

		HttpGet get = new HttpGet(url);

		try {
			httpClient.execute(get);
		} catch (ClientProtocolException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		} finally {
			get.releaseConnection();
		}
	}

	private void publishEntity(HttpClient httpClient, HttpEntity httpEntity)
			throws HandlerException {
		String url = "http://batchhelper.sinaapp.com/action.php?action=uploadStatus";

		HttpPost post = new HttpPost(url);

		post.setEntity(httpEntity);

		try {
			HttpResponse response = httpClient.execute(post);

			int statusCode = response.getStatusLine().getStatusCode();

			if (statusCode != HttpStatus.SC_OK) {
				throw new HandlerException(String.valueOf(statusCode));
			}
		} catch (ClientProtocolException e) {
			throw new HandlerException(e);
		} catch (IOException e) {
			throw new HandlerException(e);
		} finally {
			post.releaseConnection();
		}
	}

	public void publishBlog(HttpClient httpClient, Blog blog)
			throws HandlerException {
		List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
		nameValuePairList.add(new BasicNameValuePair("text", blog.getText()));
		nameValuePairList.add(new BasicNameValuePair("picturePath", blog
				.getPicture()));

		HttpEntity httpEntity = new UrlEncodedFormEntity(nameValuePairList,
				Charsets.UTF_8);

		publishEntity(httpClient, httpEntity);
	}

}
