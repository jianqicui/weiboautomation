package org.weiboautomation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class WeiboConvertUserNamesTest {

	private ObjectMapper objectMapper;

	public WeiboConvertUserNamesTest() {
		objectMapper = new ObjectMapper();
	}

	private void convert(String userNamesJsonFile, String userNamesTxtPath) {
		ArrayNode arrayNode = null;

		try {
			arrayNode = (ArrayNode) objectMapper.readTree(new File(
					userNamesJsonFile));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		List<String> userNameList = new ArrayList<String>();

		for (JsonNode jsonNode : arrayNode) {
			String userName = jsonNode.get("name").asText();

			userNameList.add(userName);
		}

		String baseName = FilenameUtils.getBaseName(userNamesJsonFile);
		int index = NumberUtils.toInt(StringUtils.split(baseName, '-')[0]);

		Map<String, String> userNamesMap = new HashMap<String, String>();

		for (int i = 0; i < 10; i++) {
			int fromIndex = index - 1;
			int toIndex = fromIndex + 1000;

			String userNames = StringUtils.join(
					userNameList.subList(fromIndex, toIndex), '\n');

			String key = index + "-" + (index + 999);

			userNamesMap.put(key, userNames);

			index = index + 1000;
		}

		for (Map.Entry<String, String> entry : userNamesMap.entrySet()) {
			String key = entry.getKey();
			String userNames = entry.getValue();

			String path = userNamesTxtPath + "/" + key + ".txt";

			try {
				FileUtils.write(new File(path), userNames, Charsets.UTF_8);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input userNamesJsonFile: ");
		String userNamesJsonFile = scanner.nextLine();

		System.out.println("Input userNamesTxtPath: ");
		String userNamesTxtPath = scanner.nextLine();

		WeiboConvertUserNamesTest weiboConvertUserNamesTest = new WeiboConvertUserNamesTest();

		weiboConvertUserNamesTest.convert(userNamesJsonFile, userNamesTxtPath);
	}

}
