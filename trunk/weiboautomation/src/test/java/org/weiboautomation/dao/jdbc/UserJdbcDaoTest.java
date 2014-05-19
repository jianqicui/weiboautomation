package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.UserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class UserJdbcDaoTest {

	@Autowired
	private UserDao userDao;

	@Test
	public void testGetUserList() throws DaoException {
		UserPhase userPhase = UserPhase.collected;
		int index = 0;
		int size = 10;

		List<User> userList = userDao.getUserList(userPhase, index, size);

		Assert.assertNotNull(userList);
	}

}
