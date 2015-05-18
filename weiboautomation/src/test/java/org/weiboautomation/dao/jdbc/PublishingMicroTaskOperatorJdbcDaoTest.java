package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.PublishingMicroTaskOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingMicroTaskOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class PublishingMicroTaskOperatorJdbcDaoTest {

	@Autowired
	private PublishingMicroTaskOperatorDao publishingMicroTaskOperatorDao;

	@Test
	public void testGetPublishingMicroTaskOperatorList() throws DaoException {
		int typeCode = 1;

		List<PublishingMicroTaskOperator> publishingMicroTaskOperatorList = publishingMicroTaskOperatorDao
				.getPublishingMicroTaskOperatorList(typeCode);

		Assert.assertNotNull(publishingMicroTaskOperatorList);
	}

}
