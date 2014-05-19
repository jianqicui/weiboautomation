package org.weiboautomation.service.impl;

import org.weiboautomation.dao.QueryingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUser;
import org.weiboautomation.service.QueryingUserService;
import org.weiboautomation.service.exception.ServiceException;

public class QueryingUserServiceImpl implements QueryingUserService {

	private QueryingUserDao queryingUserDao;

	public void setQueryingUserDao(QueryingUserDao queryingUserDao) {
		this.queryingUserDao = queryingUserDao;
	}

	@Override
	public QueryingUser getQueryingUser() throws ServiceException {
		try {
			return queryingUserDao.getQueryingUser();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateQueryingUser(QueryingUser queryingUser)
			throws ServiceException {
		try {
			queryingUserDao.updateQueryingUser(queryingUser);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
