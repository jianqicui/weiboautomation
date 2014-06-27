package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.FollowingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class FollowingUserJdbcDaoTest {

	@Autowired
	private FollowingUserDao followingUserDao;

	@Test
	public void testGetFollowingUserList() throws DaoException {
		List<FollowingUser> followingUserList = followingUserDao
				.getFollowingUserList();

		Assert.assertNotNull(followingUserList);
	}

}