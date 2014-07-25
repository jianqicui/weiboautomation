package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;

public interface UserDao {

	void addUser(UserPhase userPhase, User user) throws DaoException;

	List<User> getUserList(UserPhase userPhase, int index, int size)
			throws DaoException;

	boolean isSameUserExisting(UserPhase userPhase, User user)
			throws DaoException;

	void deleteUser(UserPhase userPhase, int id) throws DaoException;

	void addUser(int typeCode, UserPhase userPhase, User user)
			throws DaoException;

	List<User> getUserList(int typeCode, UserPhase userPhase, int index,
			int size) throws DaoException;

	boolean isSameUserExisting(int typeCode, UserPhase userPhase, User user)
			throws DaoException;

	void deleteUser(int typeCode, UserPhase userPhase, int id)
			throws DaoException;

}
