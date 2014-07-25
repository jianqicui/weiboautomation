package org.weiboautomation.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.QueryingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUserOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class QueryingUserOperatorJdbcDaoTest {

	@Autowired
	private QueryingUserOperatorDao queryingUserOperatorDao;

	@Test
	public void testGetQueryingUserOperator() throws DaoException {
		QueryingUserOperator queryingUserOperator = queryingUserOperatorDao
				.getQueryingUserOperator();

		Assert.assertNotNull(queryingUserOperator);
	}

}
