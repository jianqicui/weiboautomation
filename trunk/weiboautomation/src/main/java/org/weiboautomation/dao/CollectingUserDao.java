package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.CollectingUser;

public interface CollectingUserDao {

	List<CollectingUser> getCollectingUserList() throws DaoException;

}
