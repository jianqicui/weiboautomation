package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUserOperator;

public interface TransferingUserOperatorDao {

	TransferingUserOperator getTransferingUserOperator() throws DaoException;

	void updateTransferingUserOperator(
			TransferingUserOperator transferingUserOperator)
			throws DaoException;

}
