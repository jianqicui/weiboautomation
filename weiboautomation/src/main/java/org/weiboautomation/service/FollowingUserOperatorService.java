package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.FollowingUserOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface FollowingUserOperatorService {

	List<FollowingUserOperator> getFollowingUserOperatorList()
			throws ServiceException;

	void updateFollowingUserOperator(FollowingUserOperator followingUserOperator)
			throws ServiceException;
	
	List<FollowingUserOperator> getFollowingUserOperatorList(int typeCode)
			throws ServiceException;

	void updateFollowingUserOperator(int typeCode,
			FollowingUserOperator followingUserOperator)
			throws ServiceException;

}
