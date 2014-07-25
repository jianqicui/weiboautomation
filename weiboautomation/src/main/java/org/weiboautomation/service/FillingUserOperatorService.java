package org.weiboautomation.service;

import org.weiboautomation.entity.FillingUserOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface FillingUserOperatorService {

	FillingUserOperator getFillingUserOperator() throws ServiceException;

	void updateFillingUserOperator(FillingUserOperator fillingUserOperator)
			throws ServiceException;

}
