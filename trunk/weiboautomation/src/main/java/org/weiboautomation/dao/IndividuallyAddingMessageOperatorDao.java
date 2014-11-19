package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingMessageOperator;

public interface IndividuallyAddingMessageOperatorDao {

	List<IndividuallyAddingMessageOperator> getIndividuallyAddingMessageOperatorList()
			throws DaoException;

	void updateIndividuallyAddingMessageOperator(
			IndividuallyAddingMessageOperator individuallyAddingMessageOperator)
			throws DaoException;

}
