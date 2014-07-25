package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FillingUserOperator;

public interface FillingUserOperatorDao {

	FillingUserOperator getFillingUserOperator() throws DaoException;

	void updateFillingUserOperator(FillingUserOperator fillingUserOperator)
			throws DaoException;

}
