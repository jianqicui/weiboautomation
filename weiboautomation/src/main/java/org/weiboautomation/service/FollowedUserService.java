package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.User;
import org.weiboautomation.service.exception.ServiceException;

public interface FollowedUserService {

	void addUser(int followingUserCode, User user) throws ServiceException;

	List<User> getUserListBeforeDays(int followingUserCode, int days)
			throws ServiceException;

	void deleteUser(int followingUserCode, int id) throws ServiceException;

}
