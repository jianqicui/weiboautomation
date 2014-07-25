package org.weiboautomation.service;

import org.weiboautomation.entity.TransferingBlogOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface TransferingBlogOperatorService {

	TransferingBlogOperator getTransferingBlogOperator(int typeCode)
			throws ServiceException;

	void updateTransferingBlogOperator(int typeCode,
			TransferingBlogOperator transferingBlogOperator)
			throws ServiceException;

}
