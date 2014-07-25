package org.weiboautomation.service.impl;

import org.weiboautomation.dao.TransferingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingBlogOperator;
import org.weiboautomation.service.TransferingBlogOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class TransferingBlogOperatorServiceImpl implements
		TransferingBlogOperatorService {

	private TransferingBlogOperatorDao transferingBlogOperatorDao;

	public void setTransferingBlogOperatorDao(
			TransferingBlogOperatorDao transferingBlogOperatorDao) {
		this.transferingBlogOperatorDao = transferingBlogOperatorDao;
	}

	@Override
	public TransferingBlogOperator getTransferingBlogOperator(int typeCode)
			throws ServiceException {
		try {
			return transferingBlogOperatorDao
					.getTransferingBlogOperator(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateTransferingBlogOperator(int typeCode,
			TransferingBlogOperator transferingBlogOperator)
			throws ServiceException {
		try {
			transferingBlogOperatorDao.updateTransferingBlogOperator(typeCode,
					transferingBlogOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
