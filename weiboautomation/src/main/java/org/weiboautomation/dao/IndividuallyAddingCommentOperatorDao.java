package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingCommentOperator;

public interface IndividuallyAddingCommentOperatorDao {

	List<IndividuallyAddingCommentOperator> getIndividuallyAddingCommentOperatorList()
			throws DaoException;

	void updateIndividuallyAddingCommentOperator(
			IndividuallyAddingCommentOperator individuallyAddingCommentOperator)
			throws DaoException;

}
