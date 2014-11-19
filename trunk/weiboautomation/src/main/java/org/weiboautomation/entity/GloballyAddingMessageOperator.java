package org.weiboautomation.entity;

import java.util.Date;
import java.util.List;

public class GloballyAddingMessageOperator {

	private int id;
	private String sn;
	private byte[] cookies;
	private String text;
	private int userSize;
	private Date beginDate;
	private Date endDate;
	private List<Integer> hourList;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSn() {
		return sn;
	}

	public void setSn(String sn) {
		this.sn = sn;
	}

	public byte[] getCookies() {
		return cookies;
	}

	public void setCookies(byte[] cookies) {
		this.cookies = cookies;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getUserSize() {
		return userSize;
	}

	public void setUserSize(int userSize) {
		this.userSize = userSize;
	}

	public Date getBeginDate() {
		return beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<Integer> getHourList() {
		return hourList;
	}

	public void setHourList(List<Integer> hourList) {
		this.hourList = hourList;
	}

}
