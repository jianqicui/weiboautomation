package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;

public interface FollowedUserDao {

	void addUser(int followingUserCode, User user) throws DaoException;

	List<User> getUserListBeforeDays(int followingUserCode, int days)
			throws DaoException;

	void deleteUser(int followingUserCode, int id) throws DaoException;

}
