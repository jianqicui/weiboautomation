package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.IndividuallyAddingMessageOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface IndividuallyAddingMessageOperatorService {

	List<IndividuallyAddingMessageOperator> getIndividuallyAddingMessageOperatorList()
			throws ServiceException;

	void updateIndividuallyAddingMessageOperator(
			IndividuallyAddingMessageOperator individuallyAddingMessageOperator)
			throws ServiceException;

}
