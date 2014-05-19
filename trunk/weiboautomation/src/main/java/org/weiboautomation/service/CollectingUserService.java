package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.CollectingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface CollectingUserService {

	List<CollectingUser> getCollectingUserList() throws ServiceException;

}
