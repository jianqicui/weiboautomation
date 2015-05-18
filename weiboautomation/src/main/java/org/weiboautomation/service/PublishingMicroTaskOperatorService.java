package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.PublishingMicroTaskOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface PublishingMicroTaskOperatorService {

	List<PublishingMicroTaskOperator> getPublishingMicroTaskOperatorList(
			int typeCode) throws ServiceException;

	void updatePublishingMicroTaskOperator(int typeCode,
			PublishingMicroTaskOperator publishingMicroTaskOperator)
			throws ServiceException;

}
