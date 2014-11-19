package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.UserBase;
import org.weiboautomation.service.exception.ServiceException;

public interface UserBaseService {

	List<UserBase> getUserBaseList(String tableName, int index, int size)
			throws ServiceException;

}
