package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.IndividuallyAddingCommentOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface IndividuallyAddingCommentOperatorService {

	List<IndividuallyAddingCommentOperator> getIndividuallyAddingCommentOperatorList()
			throws ServiceException;

	void updateIndividuallyAddingCommentOperator(
			IndividuallyAddingCommentOperator individuallyAddingCommentOperator)
			throws ServiceException;

}
