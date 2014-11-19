package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingCommentOperator;

public interface GloballyAddingCommentOperatorDao {

	List<GloballyAddingCommentOperator> getGloballyAddingCommentOperatorList()
			throws DaoException;

	void updateGloballyAddingCommentOperator(
			GloballyAddingCommentOperator globallyAddingCommentOperator)
			throws DaoException;

}
