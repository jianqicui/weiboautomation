package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.FollowingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUser;
import org.weiboautomation.service.FollowingUserService;
import org.weiboautomation.service.exception.ServiceException;

public class FollowingUserServiceImpl implements FollowingUserService {

	private FollowingUserDao followingUserDao;

	public void setFollowingUserDao(FollowingUserDao followingUserDao) {
		this.followingUserDao = followingUserDao;
	}

	@Override
	public List<FollowingUser> getFollowingUserList() throws ServiceException {
		try {
			return followingUserDao.getFollowingUserList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateFollowingUser(FollowingUser followingUser)
			throws ServiceException {
		try {
			followingUserDao.updateFollowingUser(followingUser);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
