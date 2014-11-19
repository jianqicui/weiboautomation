package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserProfile;

public interface UserProfileDao {

	List<UserProfile> getDescendingUserProfileList(int index, int size)
			throws DaoException;

	void addUserProfile(UserProfile userProfile) throws DaoException;

	List<UserProfile> getUserProfileList(int index, int size)
			throws DaoException;

	List<UserProfile> getRandomUserProfileList(int index, int size)
			throws DaoException;

}
