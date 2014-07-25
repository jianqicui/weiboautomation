package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.UserProfile;
import org.weiboautomation.service.exception.ServiceException;

public interface UserProfileService {

	List<UserProfile> getDescendingUserProfileList(int index, int size)
			throws ServiceException;

	void addUserProfile(UserProfile userProfile) throws ServiceException;

	List<UserProfile> getUserProfileList(int index, int size)
			throws ServiceException;

}
