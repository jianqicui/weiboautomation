package org.weiboautomation;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import com.fasterxml.jackson.databind.ObjectMapper;

public class WeiboConvertCookiesTest {

	private ObjectMapper objectMapper;

	public WeiboConvertCookiesTest() {
		objectMapper = new ObjectMapper();
	}

	private void convertCookies(String cookiesTextFile, String cookiesJsonFile)
			throws Exception {
		List<String> cookieStringList = FileUtils.readLines(new File(
				cookiesTextFile), Charsets.UTF_8);

		List<Cookie> cookieList = new ArrayList<Cookie>();

		for (String cookieString : cookieStringList) {
			String[] cookieStrings = StringUtils.split(cookieString);

			if (cookieStrings.length == 7) {
				String domain = cookieStrings[0];

				if ("login.sina.com.cn".equals(domain)
						|| "sina.com.cn".equals(domain)) {
					String path = cookieStrings[2];
					boolean secure = BooleanUtils.toBoolean(cookieStrings[3]);
					Date expiryDate = new Date(
							NumberUtils.toLong(cookieStrings[4]) * 1000);
					String name = cookieStrings[5];
					String value = cookieStrings[6];

					BasicClientCookie basicClientCookie = new BasicClientCookie(
							name, value);

					basicClientCookie.setDomain(domain);
					basicClientCookie.setExpiryDate(expiryDate);
					basicClientCookie.setPath(path);
					basicClientCookie.setSecure(secure);

					cookieList.add(basicClientCookie);
				}
			}
		}

		objectMapper.writeValue(new File(cookiesJsonFile), cookieList);
	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Input cookiesTextFile: ");
		String cookiesTextFile = scanner.nextLine();

		System.out.println("Input cookiesJsonFile: ");
		String cookiesJsonFile = scanner.nextLine();

		WeiboConvertCookiesTest weiboConvertCookiesTest = new WeiboConvertCookiesTest();

		weiboConvertCookiesTest
				.convertCookies(cookiesTextFile, cookiesJsonFile);
	}

}
