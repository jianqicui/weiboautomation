package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.IndividuallyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingMessageOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class IndividuallyAddingMessageOperatorJdbcDaoTest {

	@Autowired
	private IndividuallyAddingMessageOperatorDao individuallyAddingMessageOperatorDao;

	@Test
	public void testGetIndividuallyAddingMessageOperatorList()
			throws DaoException {
		List<IndividuallyAddingMessageOperator> individuallyAddingMessageOperatorList = individuallyAddingMessageOperatorDao
				.getIndividuallyAddingMessageOperatorList();

		Assert.assertNotNull(individuallyAddingMessageOperatorList);
	}

}
