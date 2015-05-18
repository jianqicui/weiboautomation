package org.weiboautomation.entity;

public class MicroTask {

	private String id;
	private String uid;
	private int forwardId;
	private int sendType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getForwardId() {
		return forwardId;
	}

	public void setForwardId(int forwardId) {
		this.forwardId = forwardId;
	}

	public int getSendType() {
		return sendType;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

}
