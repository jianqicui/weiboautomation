package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.PublishingMicroTaskOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingMicroTaskOperator;
import org.weiboautomation.service.PublishingMicroTaskOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class PublishingMicroTaskOperatorServiceImpl implements
		PublishingMicroTaskOperatorService {

	private PublishingMicroTaskOperatorDao publishingMicroTaskOperatorDao;

	public void setPublishingMicroTaskOperatorDao(
			PublishingMicroTaskOperatorDao publishingMicroTaskOperatorDao) {
		this.publishingMicroTaskOperatorDao = publishingMicroTaskOperatorDao;
	}

	@Override
	public List<PublishingMicroTaskOperator> getPublishingMicroTaskOperatorList(
			int typeCode) throws ServiceException {
		try {
			return publishingMicroTaskOperatorDao
					.getPublishingMicroTaskOperatorList(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePublishingMicroTaskOperator(int typeCode,
			PublishingMicroTaskOperator publishingMicroTaskOperator)
			throws ServiceException {
		try {
			publishingMicroTaskOperatorDao.updatePublishingMicroTaskOperator(
					typeCode, publishingMicroTaskOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
