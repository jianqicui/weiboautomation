package org.weiboautomation.service.impl;

import org.weiboautomation.dao.FillingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FillingUserOperator;
import org.weiboautomation.service.FillingUserOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class FillingUserOperatorServiceImpl implements
		FillingUserOperatorService {

	private FillingUserOperatorDao fillingUserOperatorDao;

	public void setFillingUserOperatorDao(
			FillingUserOperatorDao fillingUserOperatorDao) {
		this.fillingUserOperatorDao = fillingUserOperatorDao;
	}

	@Override
	public FillingUserOperator getFillingUserOperator() throws ServiceException {
		try {
			return fillingUserOperatorDao.getFillingUserOperator();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateFillingUserOperator(
			FillingUserOperator fillingUserOperator) throws ServiceException {
		try {
			fillingUserOperatorDao
					.updateFillingUserOperator(fillingUserOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
