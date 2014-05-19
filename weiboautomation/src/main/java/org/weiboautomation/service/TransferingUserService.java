package org.weiboautomation.service;

import org.weiboautomation.entity.TransferingUser;
import org.weiboautomation.service.exception.ServiceException;

public interface TransferingUserService {

	TransferingUser getTransferingUser(int typeCode) throws ServiceException;

	void updateTransferingUser(int typeCode, TransferingUser transferingUser)
			throws ServiceException;

}
