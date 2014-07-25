package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.PublishingBlogOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface PublishingBlogOperatorService {

	List<PublishingBlogOperator> getPublishingBlogOperatorList(int typeCode)
			throws ServiceException;

	void updatePublishingBlogOperator(int typeCode,
			PublishingBlogOperator publishingBlogOperator)
			throws ServiceException;

}
