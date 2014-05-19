package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.PpTidType;
import org.weiboautomation.service.exception.ServiceException;

public interface PpTidTypeService {

	List<PpTidType> getPpTidTypeList() throws ServiceException;

	void updatePpTidType(PpTidType ppTidType) throws ServiceException;

}
