package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.PublishingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface PublishingUserService {

	List<PublishingUser> getPublishingUserList(int typeCode)
			throws ServiceException;

	void updatePublishingUser(int typeCode, PublishingUser publishingUser)
			throws ServiceException;

}
