package org.weiboautomation.service.impl;

import org.weiboautomation.dao.TransferingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUser;
import org.weiboautomation.service.TransferingUserService;
import org.weiboautomation.service.exception.ServiceException;

public class TransferingUserServiceImpl implements TransferingUserService {

	private TransferingUserDao transferingUserDao;

	public void setTransferingUserDao(TransferingUserDao transferingUserDao) {
		this.transferingUserDao = transferingUserDao;
	}

	@Override
	public TransferingUser getTransferingUser(int typeCode)
			throws ServiceException {
		try {
			return transferingUserDao.getTransferingUser(typeCode);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateTransferingUser(int typeCode,
			TransferingUser transferingUser) throws ServiceException {
		try {
			transferingUserDao.updateTransferingUser(typeCode, transferingUser);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
