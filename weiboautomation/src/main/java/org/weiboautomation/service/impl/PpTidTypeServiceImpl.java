package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.PpTidTypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PpTidType;
import org.weiboautomation.service.PpTidTypeService;
import org.weiboautomation.service.exception.ServiceException;

public class PpTidTypeServiceImpl implements PpTidTypeService {

	private PpTidTypeDao ppTidTypeDao;

	public void setPpTidTypeDao(PpTidTypeDao ppTidTypeDao) {
		this.ppTidTypeDao = ppTidTypeDao;
	}

	@Override
	public List<PpTidType> getPpTidTypeList() throws ServiceException {
		try {
			return ppTidTypeDao.getPpTidTypeList();
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updatePpTidType(PpTidType ppTidType) throws ServiceException {
		try {
			ppTidTypeDao.updatePpTidType(ppTidType);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
