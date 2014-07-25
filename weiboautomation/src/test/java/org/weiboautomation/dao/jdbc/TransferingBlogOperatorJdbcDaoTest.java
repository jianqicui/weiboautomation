package org.weiboautomation.dao.jdbc;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.TransferingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingBlogOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TransferingBlogOperatorJdbcDaoTest {

	@Autowired
	private TransferingBlogOperatorDao transferingBlogOperatorDao;

	@Test
	public void testGetTransferingBlogOperator() throws DaoException {
		int typeCode = 1;

		TransferingBlogOperator transferingBlogOperator = transferingBlogOperatorDao
				.getTransferingBlogOperator(typeCode);

		Assert.assertNotNull(transferingBlogOperator);
	}

}
