package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.UserProfileDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserProfile;
import org.weiboautomation.service.UserProfileService;
import org.weiboautomation.service.exception.ServiceException;

public class UserProfileServiceImpl implements UserProfileService {

	private UserProfileDao userProfileDao;

	public void setUserProfileDao(UserProfileDao userProfileDao) {
		this.userProfileDao = userProfileDao;
	}

	@Override
	public List<UserProfile> getDescendingUserProfileList(int index, int size)
			throws ServiceException {
		try {
			return userProfileDao.getDescendingUserProfileList(index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addUserProfile(UserProfile userProfile) throws ServiceException {
		try {
			userProfileDao.addUserProfile(userProfile);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<UserProfile> getUserProfileList(int index, int size)
			throws ServiceException {
		try {
			return userProfileDao.getUserProfileList(index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<UserProfile> getRandomUserProfileList(int index, int size)
			throws ServiceException {
		try {
			return userProfileDao.getRandomUserProfileList(index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
