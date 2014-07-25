package org.weiboautomation.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.TransferingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUserOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TransferingUserOperatorDaoTest {

	@Autowired
	private TransferingUserOperatorDao transferingUserOperatorDao;

	@Test
	public void testGetTransferingUserOperator() throws DaoException {
		TransferingUserOperator transferingUserOperator = transferingUserOperatorDao
				.getTransferingUserOperator();

		Assert.assertNotNull(transferingUserOperator);
	}

}
