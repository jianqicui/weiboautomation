package org.weiboautomation.service.impl;

import org.weiboautomation.dao.QueryingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUserOperator;
import org.weiboautomation.service.QueryingUserOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class QueryingUserOperatorServiceImpl implements
		QueryingUserOperatorService {

	private QueryingUserOperatorDao queryingUserOperatorDao;

	public void setQueryingUserOperatorDao(
			QueryingUserOperatorDao queryingUserOperatorDao) {
		this.queryingUserOperatorDao = queryingUserOperatorDao;
	}

	@Override
	public QueryingUserOperator getQueryingUserOperator()
			throws ServiceException {
		try {
			return queryingUserOperatorDao.getQueryingUserOperator();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateQueryingUserOperator(
			QueryingUserOperator queryingUserOperator) throws ServiceException {
		try {
			queryingUserOperatorDao
					.updateQueryingUserOperator(queryingUserOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
