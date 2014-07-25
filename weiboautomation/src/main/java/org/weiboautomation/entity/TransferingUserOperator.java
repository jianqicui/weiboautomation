package org.weiboautomation.entity;

public class TransferingUserOperator {

	private int id;
	private byte[] cookies;
	private int userIndex;
	
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
	
	public int getUserIndex() {
		return userIndex;
	}
	
	public void setUserIndex(int userIndex) {
		this.userIndex = userIndex;
	}
	
}
