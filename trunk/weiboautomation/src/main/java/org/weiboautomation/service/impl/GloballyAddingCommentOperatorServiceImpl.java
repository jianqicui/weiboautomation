package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.GloballyAddingCommentOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingCommentOperator;
import org.weiboautomation.service.GloballyAddingCommentOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class GloballyAddingCommentOperatorServiceImpl implements
		GloballyAddingCommentOperatorService {

	private GloballyAddingCommentOperatorDao globallyAddingCommentOperatorDao;

	public void setGloballyAddingCommentOperatorDao(
			GloballyAddingCommentOperatorDao globallyAddingCommentOperatorDao) {
		this.globallyAddingCommentOperatorDao = globallyAddingCommentOperatorDao;
	}

	@Override
	public List<GloballyAddingCommentOperator> getGloballyAddingCommentOperatorList()
			throws ServiceException {
		try {
			return globallyAddingCommentOperatorDao
					.getGloballyAddingCommentOperatorList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateGloballyAddingCommentOperator(
			GloballyAddingCommentOperator globallyAddingCommentOperator)
			throws ServiceException {
		try {
			globallyAddingCommentOperatorDao
					.updateGloballyAddingCommentOperator(globallyAddingCommentOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
