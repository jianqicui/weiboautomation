package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.UserBaseDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserBase;
import org.weiboautomation.service.UserBaseService;
import org.weiboautomation.service.exception.ServiceException;

public class UserBaseServiceImpl implements UserBaseService {

	private UserBaseDao userBaseDao;

	public void setUserBaseDao(UserBaseDao userBaseDao) {
		this.userBaseDao = userBaseDao;
	}

	@Override
	public List<UserBase> getUserBaseList(String tableName, int index, int size)
			throws ServiceException {
		try {
			return userBaseDao.getUserBaseList(tableName, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
