package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.UserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;
import org.weiboautomation.service.UserService;
import org.weiboautomation.service.exception.ServiceException;

public class UserServiceImpl implements UserService {

	private UserDao userDao;

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public void addUser(UserPhase userPhase, User user) throws ServiceException {
		try {
			userDao.addUser(userPhase, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<User> getUserList(UserPhase userPhase, int index, int size)
			throws ServiceException {
		try {
			return userDao.getUserList(userPhase, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<User> getRandomUserList(UserPhase userPhase, int index, int size)
			throws ServiceException {
		try {
			return userDao.getRandomUserList(userPhase, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isSameUserExisting(UserPhase userPhase, User user)
			throws ServiceException {
		try {
			return userDao.isSameUserExisting(userPhase, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void moveUser(UserPhase fromUserPhase, UserPhase toUserPhase,
			User user) throws ServiceException {
		try {
			userDao.addUser(toUserPhase, user);

			userDao.deleteUser(fromUserPhase, user.getId());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteUser(UserPhase userPhase, int id) throws ServiceException {
		try {
			userDao.deleteUser(userPhase, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addUser(int typeCode, UserPhase userPhase, User user)
			throws ServiceException {
		try {
			userDao.addUser(typeCode, userPhase, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<User> getUserList(int typeCode, UserPhase userPhase, int index,
			int size) throws ServiceException {
		try {
			return userDao.getUserList(typeCode, userPhase, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public boolean isSameUserExisting(int typeCode, UserPhase userPhase,
			User user) throws ServiceException {
		try {
			return userDao.isSameUserExisting(typeCode, userPhase, user);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void moveUser(int typeCode, UserPhase fromUserPhase,
			UserPhase toUserPhase, User user) throws ServiceException {
		try {
			userDao.addUser(typeCode, toUserPhase, user);

			userDao.deleteUser(fromUserPhase, user.getId());
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteUser(int typeCode, UserPhase userPhase, int id)
			throws ServiceException {
		try {
			userDao.deleteUser(typeCode, userPhase, id);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
