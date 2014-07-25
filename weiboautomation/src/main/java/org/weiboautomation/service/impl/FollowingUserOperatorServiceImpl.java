package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.FollowingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUserOperator;
import org.weiboautomation.service.FollowingUserOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class FollowingUserOperatorServiceImpl implements
		FollowingUserOperatorService {

	private FollowingUserOperatorDao followingUserOperatorDao;

	public void setFollowingUserOperatorDao(
			FollowingUserOperatorDao followingUserOperatorDao) {
		this.followingUserOperatorDao = followingUserOperatorDao;
	}

	@Override
	public List<FollowingUserOperator> getFollowingUserOperatorList()
			throws ServiceException {
		try {
			return followingUserOperatorDao.getFollowingUserOperatorList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateFollowingUserOperator(
			FollowingUserOperator followingUserOperator)
			throws ServiceException {
		try {
			followingUserOperatorDao
					.updateFollowingUserOperator(followingUserOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<FollowingUserOperator> getFollowingUserOperatorList(int typeCode)
			throws ServiceException {
		try {
			return followingUserOperatorDao
					.getFollowingUserOperatorList(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateFollowingUserOperator(int typeCode,
			FollowingUserOperator followingUserOperator)
			throws ServiceException {
		try {
			followingUserOperatorDao.updateFollowingUserOperator(typeCode,
					followingUserOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
