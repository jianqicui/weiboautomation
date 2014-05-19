package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PpTidType;

public interface PpTidTypeDao {

	List<PpTidType> getPpTidTypeList() throws DaoException;

	void updatePpTidType(PpTidType ppTidType) throws DaoException;

}
