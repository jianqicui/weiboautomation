package org.weiboautomation.service;

import org.weiboautomation.entity.QueryingUserOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface QueryingUserOperatorService {

	QueryingUserOperator getQueryingUserOperator() throws ServiceException;

	void updateQueryingUserOperator(QueryingUserOperator queryingUserOperator)
			throws ServiceException;

}
