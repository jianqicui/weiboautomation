package org.weiboautomation.service;

import org.weiboautomation.entity.TransferingUserOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface TransferingUserOperatorService {

	TransferingUserOperator getTransferingUserOperator()
			throws ServiceException;

	void updateTransferingUserOperator(
			TransferingUserOperator transferingUserOperator)
			throws ServiceException;

}
