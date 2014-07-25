package org.weiboautomation.entity;

public class FollowingUserOperator {

	private int id;
	private int code;
	private byte[] cookies;
	private int userIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public byte[] getCookies() {
		return cookies;
	}

	public void setCookies(byte[] cookies) {
		this.cookies = cookies;
	}

	public int getUserIndex() {
		return userIndex;
	}

	public void setUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}

}
