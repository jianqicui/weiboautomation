package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.GloballyAddingCommentOperator;
import org.weiboautomation.service.exception.ServiceException;

public interface GloballyAddingCommentOperatorService {

	List<GloballyAddingCommentOperator> getGloballyAddingCommentOperatorList()
			throws ServiceException;

	void updateGloballyAddingCommentOperator(
			GloballyAddingCommentOperator globallyAddingCommentOperator)
			throws ServiceException;

}
