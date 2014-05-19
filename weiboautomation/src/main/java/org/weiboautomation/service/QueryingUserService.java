package org.weiboautomation.service;

import org.weiboautomation.entity.QueryingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface QueryingUserService {

	QueryingUser getQueryingUser() throws ServiceException;

	void updateQueryingUser(QueryingUser queryingUser) throws ServiceException;

}
