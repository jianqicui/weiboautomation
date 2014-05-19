package org.weiboautomation.entity;

public class TransferingUser {

	private int id;
	private byte[] cookies;
	private int blogIndex;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getCookies() {
		return cookies;
	}

	public void setCookies(byte[] cookies) {
		this.cookies = cookies;
	}

	public int getBlogIndex() {
		return blogIndex;
	}

	public void setBlogIndex(int blogIndex) {
		this.blogIndex = blogIndex;
	}

}
