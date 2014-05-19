package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUser;

public interface QueryingUserDao {

	QueryingUser getQueryingUser() throws DaoException;

	void updateQueryingUser(QueryingUser queryingUser) throws DaoException;

}
