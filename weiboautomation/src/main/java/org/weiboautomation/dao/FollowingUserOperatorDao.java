package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUserOperator;

public interface FollowingUserOperatorDao {

	List<FollowingUserOperator> getFollowingUserOperatorList()
			throws DaoException;

	void updateFollowingUserOperator(FollowingUserOperator followingUserOperator)
			throws DaoException;

	List<FollowingUserOperator> getFollowingUserOperatorList(int typeCode)
			throws DaoException;

	void updateFollowingUserOperator(int typeCode,
			FollowingUserOperator followingUserOperator) throws DaoException;

}
