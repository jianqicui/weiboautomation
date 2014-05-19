package org.weiboautomation.entity;

public class PpTidType {

	private int id;
	private int ppTid;
	private int ppTidPageSize;
	private int ppTidPageIndex;
	private Type type;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPpTid() {
		return ppTid;
	}

	public void setPpTid(int ppTid) {
		this.ppTid = ppTid;
	}

	public int getPpTidPageSize() {
		return ppTidPageSize;
	}

	public void setPpTidPageSize(int ppTidPageSize) {
		this.ppTidPageSize = ppTidPageSize;
	}

	public int getPpTidPageIndex() {
		return ppTidPageIndex;
	}

	public void setPpTidPageIndex(int ppTidPageIndex) {
		this.ppTidPageIndex = ppTidPageIndex;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

}
