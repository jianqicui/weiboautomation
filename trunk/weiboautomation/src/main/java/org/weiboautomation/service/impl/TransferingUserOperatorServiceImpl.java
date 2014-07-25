package org.weiboautomation.service.impl;

import org.weiboautomation.dao.TransferingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUserOperator;
import org.weiboautomation.service.TransferingUserOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class TransferingUserOperatorServiceImpl implements
		TransferingUserOperatorService {

	private TransferingUserOperatorDao transferingUserOperatorDao;

	public void setTransferingUserOperatorDao(
			TransferingUserOperatorDao transferingUserOperatorDao) {
		this.transferingUserOperatorDao = transferingUserOperatorDao;
	}

	@Override
	public TransferingUserOperator getTransferingUserOperator()
			throws ServiceException {
		try {
			return transferingUserOperatorDao.getTransferingUserOperator();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateTransferingUserOperator(
			TransferingUserOperator transferingUserOperator)
			throws ServiceException {
		try {
			transferingUserOperatorDao
					.updateTransferingUserOperator(transferingUserOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
