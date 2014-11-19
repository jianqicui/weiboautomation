package org.weiboautomation.entity;

public class IndividuallyAddingCommentOperator extends
		GloballyAddingCommentOperator {

	private String userBaseTableName;
	private int userBaseIndex;

	public String getUserBaseTableName() {
		return userBaseTableName;
	}

	public void setUserBaseTableName(String userBaseTableName) {
		this.userBaseTableName = userBaseTableName;
	}

	public int getUserBaseIndex() {
		return userBaseIndex;
	}

	public void setUserBaseIndex(int userBaseIndex) {
		this.userBaseIndex = userBaseIndex;
	}

}
