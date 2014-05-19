package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.FollowingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface FollowingUserService {

	List<FollowingUser> getFollowingUserList(int typeCode)
			throws ServiceException;

	void updateFollowingUser(int typeCode, FollowingUser followingUser)
			throws ServiceException;

}
