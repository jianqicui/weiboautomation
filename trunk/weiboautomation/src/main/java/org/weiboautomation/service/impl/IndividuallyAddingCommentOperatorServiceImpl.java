package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.IndividuallyAddingCommentOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingCommentOperator;
import org.weiboautomation.service.IndividuallyAddingCommentOperatorService;
import org.weiboautomation.service.exception.ServiceException;

public class IndividuallyAddingCommentOperatorServiceImpl implements
		IndividuallyAddingCommentOperatorService {

	private IndividuallyAddingCommentOperatorDao individuallyAddingCommentOperatorDao;

	public void setIndividuallyAddingCommentOperatorDao(
			IndividuallyAddingCommentOperatorDao individuallyAddingCommentOperatorDao) {
		this.individuallyAddingCommentOperatorDao = individuallyAddingCommentOperatorDao;
	}

	@Override
	public List<IndividuallyAddingCommentOperator> getIndividuallyAddingCommentOperatorList()
			throws ServiceException {
		try {
			return individuallyAddingCommentOperatorDao
					.getIndividuallyAddingCommentOperatorList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateIndividuallyAddingCommentOperator(
			IndividuallyAddingCommentOperator individuallyAddingCommentOperator)
			throws ServiceException {
		try {
			individuallyAddingCommentOperatorDao
					.updateIndividuallyAddingCommentOperator(individuallyAddingCommentOperator);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
