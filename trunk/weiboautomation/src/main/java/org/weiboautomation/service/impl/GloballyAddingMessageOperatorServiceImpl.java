package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.GloballyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingMessageOperator;
import org.weiboautomation.service.GloballyAddingMessageOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class GloballyAddingMessageOperatorServiceImpl implements
		GloballyAddingMessageOperatorService {

	private GloballyAddingMessageOperatorDao globallyAddingMessageOperatorDao;

	public void setGloballyAddingMessageOperatorDao(
			GloballyAddingMessageOperatorDao globallyAddingMessageOperatorDao) {
		this.globallyAddingMessageOperatorDao = globallyAddingMessageOperatorDao;
	}

	@Override
	public List<GloballyAddingMessageOperator> getGloballyAddingMessageOperatorList()
			throws ServiceException {
		try {
			return globallyAddingMessageOperatorDao
					.getGloballyAddingMessageOperatorList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateGloballyAddingMessageOperator(
			GloballyAddingMessageOperator globallyAddingMessageOperator)
			throws ServiceException {
		try {
			globallyAddingMessageOperatorDao
					.updateGloballyAddingMessageOperator(globallyAddingMessageOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
