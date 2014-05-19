package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.PublishingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingUser;
import org.weiboautomation.service.PublishingUserService;
import org.weiboautomation.service.exception.ServiceException;

public class PublishingUserServiceImpl implements PublishingUserService {

	private PublishingUserDao publishingUserDao;

	public void setPublishingUserDao(PublishingUserDao publishingUserDao) {
		this.publishingUserDao = publishingUserDao;
	}

	@Override
	public List<PublishingUser> getPublishingUserList(int typeCode)
			throws ServiceException {
		try {
			return publishingUserDao.getPublishingUserList(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePublishingUser(int typeCode, PublishingUser publishingUser)
			throws ServiceException {
		try {
			publishingUserDao.updatePublishingUser(typeCode, publishingUser);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
