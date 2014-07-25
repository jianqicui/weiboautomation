package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingBlogOperator;

public interface TransferingBlogOperatorDao {

	TransferingBlogOperator getTransferingBlogOperator(int typeCode)
			throws DaoException;

	void updateTransferingBlogOperator(int typeCode,
			TransferingBlogOperator transferingBlogOperator)
			throws DaoException;

}
