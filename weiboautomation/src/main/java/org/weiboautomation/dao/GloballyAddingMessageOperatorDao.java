package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingMessageOperator;

public interface GloballyAddingMessageOperatorDao {

	List<GloballyAddingMessageOperator> getGloballyAddingMessageOperatorList()
			throws DaoException;

	void updateGloballyAddingMessageOperator(
			GloballyAddingMessageOperator globallyAddingMessageOperator)
			throws DaoException;

}
