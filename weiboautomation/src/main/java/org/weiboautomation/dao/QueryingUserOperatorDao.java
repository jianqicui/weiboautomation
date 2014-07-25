package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUserOperator;

public interface QueryingUserOperatorDao {

	QueryingUserOperator getQueryingUserOperator() throws DaoException;

	void updateQueryingUserOperator(QueryingUserOperator queryingUserOperator)
			throws DaoException;

}
