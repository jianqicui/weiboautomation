package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.TypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Type;
import org.weiboautomation.service.TypeService;
import org.weiboautomation.service.exception.ServiceException;

public class TypeServiceImpl implements TypeService {

	private TypeDao typeDao;

	public void setTypeDao(TypeDao typeDao) {
		this.typeDao = typeDao;
	}

	@Override
	public List<Type> getTypeList() throws ServiceException {
		try {
			return typeDao.getTypeList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
