package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserBase;

public interface UserBaseDao {

	List<UserBase> getUserBaseList(String tableName, int index, int size)
			throws DaoException;

}
