package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.IndividuallyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingMessageOperator;
import org.weiboautomation.service.IndividuallyAddingMessageOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class IndividuallyAddingMessageOperatorServiceImpl implements
		IndividuallyAddingMessageOperatorService {

	private IndividuallyAddingMessageOperatorDao individuallyAddingMessageOperatorDao;

	public void setIndividuallyAddingMessageOperatorDao(
			IndividuallyAddingMessageOperatorDao individuallyAddingMessageOperatorDao) {
		this.individuallyAddingMessageOperatorDao = individuallyAddingMessageOperatorDao;
	}

	@Override
	public List<IndividuallyAddingMessageOperator> getIndividuallyAddingMessageOperatorList()
			throws ServiceException {
		try {
			return individuallyAddingMessageOperatorDao
					.getIndividuallyAddingMessageOperatorList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateIndividuallyAddingMessageOperator(
			IndividuallyAddingMessageOperator individuallyAddingMessageOperator)
			throws ServiceException {
		try {
			individuallyAddingMessageOperatorDao
					.updateIndividuallyAddingMessageOperator(individuallyAddingMessageOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
