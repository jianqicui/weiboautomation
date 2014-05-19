package org.weiboautomation.dao;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUser;

public interface TransferingUserDao {

	TransferingUser getTransferingUser(int typeCode) throws DaoException;

	void updateTransferingUser(int typeCode, TransferingUser transferingUser)
			throws DaoException;

}
