package org.weiboautomation.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.TransferingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TransferingUserJdbcDaoTest {

	@Autowired
	private TransferingUserDao transferingUserDao;

	@Test
	public void testGetTransferingUser() throws DaoException {
		int typeCode = 1;

		TransferingUser transferingUser = transferingUserDao
				.getTransferingUser(typeCode);

		Assert.assertNotNull(transferingUser);
	}

}
