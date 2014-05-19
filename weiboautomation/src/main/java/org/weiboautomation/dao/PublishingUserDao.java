package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingUser;

public interface PublishingUserDao {

	List<PublishingUser> getPublishingUserList(int typeCode)
			throws DaoException;

	void updatePublishingUser(int typeCode, PublishingUser publishingUser)
			throws DaoException;

}
