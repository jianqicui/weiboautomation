package org.weiboautomation.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.QueryingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class QueryingUserJdbcDaoTest {

	@Autowired
	private QueryingUserDao queryingUserDao;

	@Test
	public void testGetQueryingUser() throws DaoException {
		QueryingUser queryingUser = queryingUserDao.getQueryingUser();

		Assert.assertNotNull(queryingUser);
	}

}
