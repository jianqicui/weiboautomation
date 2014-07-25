package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.FollowingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUserOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class FollowingUserOperatorJdbcDaoTest {

	@Autowired
	private FollowingUserOperatorDao followingUserOperatorDao;

	@Test
	public void testGetFollowingUserOperatorListGlobally() throws DaoException {
		List<FollowingUserOperator> followingUserOperatorList = followingUserOperatorDao
				.getFollowingUserOperatorList();

		Assert.assertNotNull(followingUserOperatorList);
	}

	@Test
	public void testGetFollowingUserOperatorListIndividually()
			throws DaoException {
		int typeCode = 1;

		List<FollowingUserOperator> followingUserOperatorList = followingUserOperatorDao
				.getFollowingUserOperatorList(typeCode);

		Assert.assertNotNull(followingUserOperatorList);
	}

}
