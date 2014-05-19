package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;

public interface FollowedUserDao {

	void addUser(int typeCode, int followingUserCode, User user)
			throws DaoException;

	List<User> getUserListBeforeDays(int typeCode, int followingUserCode,
			int days) throws DaoException;

	void deleteUser(int typeCode, int followingUserCode, int id)
			throws DaoException;

}
