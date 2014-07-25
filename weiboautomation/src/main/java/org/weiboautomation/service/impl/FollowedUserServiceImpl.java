package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.FollowedUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;
import org.weiboautomation.service.FollowedUserService;
import org.weiboautomation.service.exception.ServiceException;

public class FollowedUserServiceImpl implements FollowedUserService {

	private FollowedUserDao followedUserDao;

	public void setFollowedUserDao(FollowedUserDao followedUserDao) {
		this.followedUserDao = followedUserDao;
	}

	@Override
	public void addUser(int followingUserCode, User user)
			throws ServiceException {
		try {
			followedUserDao.addUser(followingUserCode, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<User> getUserListBeforeDays(int followingUserCode, int days,
			int index, int size) throws ServiceException {
		try {
			return followedUserDao.getUserListBeforeDays(followingUserCode,
					days, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteUser(int followingUserCode, int id)
			throws ServiceException {
		try {
			followedUserDao.deleteUser(followingUserCode, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addUser(int typeCode, int followingUserCode, User user)
			throws ServiceException {
		try {
			followedUserDao.addUser(typeCode, followingUserCode, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<User> getUserListBeforeDays(int typeCode,
			int followingUserCode, int days, int index, int size)
			throws ServiceException {
		try {
			return followedUserDao.getUserListBeforeDays(typeCode,
					followingUserCode, days, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteUser(int typeCode, int followingUserCode, int id)
			throws ServiceException {
		try {
			followedUserDao.deleteUser(typeCode, followingUserCode, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
