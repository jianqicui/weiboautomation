package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingBlogOperator;

public interface PublishingBlogOperatorDao {

	List<PublishingBlogOperator> getPublishingBlogOperatorList(int typeCode)
			throws DaoException;

	void updatePublishingBlogOperator(int typeCode,
			PublishingBlogOperator publishingBlogOperator) throws DaoException;

}
