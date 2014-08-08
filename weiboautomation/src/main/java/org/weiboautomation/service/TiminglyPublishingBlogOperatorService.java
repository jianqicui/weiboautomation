package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.TiminglyPublishingBlogOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface TiminglyPublishingBlogOperatorService {

	List<TiminglyPublishingBlogOperator> getTiminglyPublishingBlogOperatorList(
			int typeCode) throws ServiceException;

	void updateTiminglyPublishingBlogOperator(int typeCode,
			TiminglyPublishingBlogOperator timinglyPublishingBlogOperator)
			throws ServiceException;

}
