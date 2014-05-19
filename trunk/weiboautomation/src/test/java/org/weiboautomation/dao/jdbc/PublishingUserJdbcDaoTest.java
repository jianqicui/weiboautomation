package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.PublishingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingUser;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class PublishingUserJdbcDaoTest {

	@Autowired
	private PublishingUserDao publishingUserDao;

	@Test
	public void testGetPublishingUserList() throws DaoException {
		int typeCode = 1;

		List<PublishingUser> publishingUserList = publishingUserDao
				.getPublishingUserList(typeCode);

		Assert.assertNotNull(publishingUserList);
	}

}
