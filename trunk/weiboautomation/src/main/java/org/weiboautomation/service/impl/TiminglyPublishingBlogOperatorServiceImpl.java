package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.TiminglyPublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TiminglyPublishingBlogOperator;
import org.weiboautomation.service.TiminglyPublishingBlogOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class TiminglyPublishingBlogOperatorServiceImpl implements
		TiminglyPublishingBlogOperatorService {

	private TiminglyPublishingBlogOperatorDao timinglyPublishingBlogOperatorDao;

	public void setTiminglyPublishingBlogOperatorDao(
			TiminglyPublishingBlogOperatorDao timinglyPublishingBlogOperatorDao) {
		this.timinglyPublishingBlogOperatorDao = timinglyPublishingBlogOperatorDao;
	}

	@Override
	public List<TiminglyPublishingBlogOperator> getTiminglyPublishingBlogOperatorList(
			int typeCode) throws ServiceException {
		try {
			return timinglyPublishingBlogOperatorDao
					.getTiminglyPublishingBlogOperatorList(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateTiminglyPublishingBlogOperator(int typeCode,
			TiminglyPublishingBlogOperator timinglyPublishingBlogOperator)
			throws ServiceException {
		try {
			timinglyPublishingBlogOperatorDao
					.updateTiminglyPublishingBlogOperator(typeCode,
							timinglyPublishingBlogOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
