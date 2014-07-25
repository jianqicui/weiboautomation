package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.PublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingBlogOperator;
import org.weiboautomation.service.PublishingBlogOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class PublishingBlogOperatorServiceImpl implements
		PublishingBlogOperatorService {

	private PublishingBlogOperatorDao publishingBlogOperatorDao;

	public void setPublishingBlogOperatorDao(
			PublishingBlogOperatorDao publishingBlogOperatorDao) {
		this.publishingBlogOperatorDao = publishingBlogOperatorDao;
	}

	@Override
	public List<PublishingBlogOperator> getPublishingBlogOperatorList(
			int typeCode) throws ServiceException {
		try {
			return publishingBlogOperatorDao
					.getPublishingBlogOperatorList(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePublishingBlogOperator(int typeCode,
			PublishingBlogOperator publishingBlogOperator)
			throws ServiceException {
		try {
			publishingBlogOperatorDao.updatePublishingBlogOperator(typeCode,
					publishingBlogOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
