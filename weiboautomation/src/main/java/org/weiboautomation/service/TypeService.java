package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.Type;
import org.weiboautomation.service.exception.ServiceException;

public interface TypeService {

	List<Type> getTypeList() throws ServiceException;

}
