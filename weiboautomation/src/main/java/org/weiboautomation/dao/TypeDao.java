package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Type;

public interface TypeDao {

	List<Type> getTypeList() throws DaoException;

}
