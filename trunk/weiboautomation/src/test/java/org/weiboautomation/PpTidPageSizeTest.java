package org.weiboautomation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class PpTidPageSizeTest {

	private ObjectMapper objectMapper;

	private HttpClient httpClient;

	public PpTidPageSizeTest() {
		objectMapper = new ObjectMapper();

		httpClient = new DefaultHttpClient();
	}

	private class PpTid {

		private int tid;
		private int tidPageSize;

		public void setTid(int tid) {
			this.tid = tid;
		}

		public int getTid() {
			return tid;
		}

		public int getTidPageSize() {
			return tidPageSize;
		}

		public void setTidPageSize(int tidPageSize) {
			this.tidPageSize = tidPageSize;
		}

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

	private String getSid() {
		String result = null;

		HttpGet get = new HttpGet(
				"http://login.pp.cc/login/plogin?u=jianqicui&p=0ccc36f0631cedafcbbdfcabbb7d091b");

		try {
			HttpResponse response = httpClient.execute(get);

			result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			get.releaseConnection();
		}

		JsonNode jsonNode = getJsonNode(result);

		String sid = jsonNode.get("sid").asText();

		return sid;
	}

	private boolean isHasBLog(String sid, int tid, int tidPageIndex) {
		String result = null;

		HttpGet get = new HttpGet(
				"http://t.pp.cc/time/index.php?mod=library&action=show&account=2690456720&tid="
						+ tid + "&page=" + tidPageIndex + "&offset=1");

		get.setHeader("Cookie", "uc_sid=" + sid + ";");

		try {
			HttpResponse response = httpClient.execute(get);

			result = EntityUtils.toString(response.getEntity(), Charsets.UTF_8);
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

		String html = jsonNode.get("html").asText();

		Document doc = Jsoup.parse(html);

		Elements divs = doc.select(".b_nrk_body");

		boolean hasBLog = !divs.isEmpty();

		return hasBLog;
	}

	private int getTidPageSize(String sid, int tid, int tidPageSize) {
		int tidPageIndex = tidPageSize;

		tidPageIndex++;

		while (true) {
			System.out.println(String.format(
					"Begin to handle tid = %s, tidPageIndex = %s", tid,
					tidPageIndex));

			boolean hasBLog = isHasBLog(sid, tid, tidPageIndex);

			System.out.println(String.format(
					"End to handle tid = %s, tidPageIndex = %s", tid,
					tidPageIndex));

			if (!hasBLog) {
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

	private void getTidPageSizes(String ppTidFile) {
		ArrayNode arrayNode = null;

		try {
			arrayNode = (ArrayNode) objectMapper.readTree(new File(ppTidFile));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<PpTid> ppTidList = new ArrayList<PpTid>();

		for (JsonNode jsonNode : arrayNode) {
			int tid = jsonNode.get("tid").asInt();
			int tidPageSize = jsonNode.get("tidPageSize").asInt();

			PpTid ppTid = new PpTid();

			ppTid.setTid(tid);
			ppTid.setTidPageSize(tidPageSize);

			ppTidList.add(ppTid);
		}

		String sid = getSid();

		for (PpTid ppTid : ppTidList) {
			int tid = ppTid.getTid();
			int tidPageSize = ppTid.getTidPageSize();

			System.out.println(String.format(
					"In the beginning, tid = %s, tidPageSize = %s", tid,
					tidPageSize));

			tidPageSize = getTidPageSize(sid, tid, tidPageSize);

			System.out
					.println(String.format(
							"In the end, tid = %s, tidPageSize = %s", tid,
							tidPageSize));

			ppTid.setTidPageSize(tidPageSize);
		}

		try {
			objectMapper.writeValue(new File(ppTidFile), ppTidList);
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (PpTid ppTid : ppTidList) {
			int tid = ppTid.getTid();
			int tidPageSize = ppTid.getTidPageSize();

			String sql = "update pp_tid_type set pp_tid_page_size = "
					+ tidPageSize + ", pp_tid_page_index = " + tidPageSize
					+ " where pp_tid = " + tid + ";";

			System.out.println(sql);
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input ppTid file: ");
		String ppTidFile = scanner.nextLine();

		PpTidPageSizeTest ppTidPageSizeTest = new PpTidPageSizeTest();

		ppTidPageSizeTest.getTidPageSizes(ppTidFile);
	}

}
