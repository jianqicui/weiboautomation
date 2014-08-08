package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TiminglyPublishingBlogOperator;

public interface TiminglyPublishingBlogOperatorDao {

	List<TiminglyPublishingBlogOperator> getTiminglyPublishingBlogOperatorList(
			int typeCode) throws DaoException;

	void updateTiminglyPublishingBlogOperator(int typeCode,
			TiminglyPublishingBlogOperator timinglyPublishingBlogOperator)
			throws DaoException;

}
