package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUser;

public interface FollowingUserDao {

	List<FollowingUser> getFollowingUserList() throws DaoException;

	void updateFollowingUser(FollowingUser followingUser)
			throws DaoException;

}
