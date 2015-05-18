package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingMicroTaskOperator;

public interface PublishingMicroTaskOperatorDao {

	List<PublishingMicroTaskOperator> getPublishingMicroTaskOperatorList(
			int typeCode) throws DaoException;

	void updatePublishingMicroTaskOperator(int typeCode,
			PublishingMicroTaskOperator publishingMicroTaskOperator)
			throws DaoException;

}
