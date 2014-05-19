package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.CollectingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.CollectingUser;
import org.weiboautomation.service.CollectingUserService;
import org.weiboautomation.service.exception.ServiceException;

public class CollectingUserServiceImpl implements CollectingUserService {

	private CollectingUserDao collectingUserDao;

	public void setCollectingUserDao(CollectingUserDao collectingUserDao) {
		this.collectingUserDao = collectingUserDao;
	}

	@Override
	public List<CollectingUser> getCollectingUserList() throws ServiceException {
		try {
			return collectingUserDao.getCollectingUserList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
