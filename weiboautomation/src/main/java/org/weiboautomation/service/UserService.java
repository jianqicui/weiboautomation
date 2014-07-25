package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;
import org.weiboautomation.service.exception.ServiceException;

public interface UserService {

	void addUser(UserPhase userPhase, User user) throws ServiceException;

	List<User> getUserList(UserPhase userPhase, int index, int size)
			throws ServiceException;

	boolean isSameUserExisting(UserPhase userPhase, User user)
			throws ServiceException;

	void moveUser(UserPhase fromUserPhase, UserPhase toUserPhase, User user)
			throws ServiceException;

	void deleteUser(UserPhase userPhase, int id) throws ServiceException;

	void addUser(int typeCode, UserPhase userPhase, User user)
			throws ServiceException;

	List<User> getUserList(int typeCode, UserPhase userPhase, int index,
			int size) throws ServiceException;

	boolean isSameUserExisting(int typeCode, UserPhase userPhase, User user)
			throws ServiceException;

	void moveUser(int typeCode, UserPhase fromUserPhase, UserPhase toUserPhase,
			User user) throws ServiceException;

	void deleteUser(int typeCode, UserPhase userPhase, int id)
			throws ServiceException;

}
