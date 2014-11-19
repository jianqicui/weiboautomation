package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.GloballyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingMessageOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class GloballyAddingMessageOperatorJdbcDaoTest {

	@Autowired
	private GloballyAddingMessageOperatorDao globallyAddingMessageOperatorDao;

	@Test
	public void testGetGloballyAddingMessageOperatorList() throws DaoException {
		List<GloballyAddingMessageOperator> globallyAddingMessageOperatorList = globallyAddingMessageOperatorDao
				.getGloballyAddingMessageOperatorList();

		Assert.assertNotNull(globallyAddingMessageOperatorList);
	}

}
