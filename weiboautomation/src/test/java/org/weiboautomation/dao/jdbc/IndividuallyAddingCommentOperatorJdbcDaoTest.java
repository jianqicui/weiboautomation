package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.IndividuallyAddingCommentOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingCommentOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class IndividuallyAddingCommentOperatorJdbcDaoTest {

	@Autowired
	private IndividuallyAddingCommentOperatorDao individuallyAddingCommentOperatorDao;

	@Test
	public void testGetIndividuallyAddingCommentOperatorList()
			throws DaoException {
		List<IndividuallyAddingCommentOperator> individuallyAddingCommentOperatorList = individuallyAddingCommentOperatorDao
				.getIndividuallyAddingCommentOperatorList();

		Assert.assertNotNull(individuallyAddingCommentOperatorList);
	}

}
