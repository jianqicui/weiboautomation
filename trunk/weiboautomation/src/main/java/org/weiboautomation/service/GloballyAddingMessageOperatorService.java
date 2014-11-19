package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.GloballyAddingMessageOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface GloballyAddingMessageOperatorService {

	List<GloballyAddingMessageOperator> getGloballyAddingMessageOperatorList()
			throws ServiceException;

	void updateGloballyAddingMessageOperator(
			GloballyAddingMessageOperator globallyAddingMessageOperator)
			throws ServiceException;

}
