package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.FollowingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface FollowingUserService {

	List<FollowingUser> getFollowingUserList() throws ServiceException;

	void updateFollowingUser(FollowingUser followingUser)
			throws ServiceException;

}
