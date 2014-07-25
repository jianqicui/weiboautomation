package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.PublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingBlogOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class PublishingBlogOperatorJdbcDaoTest {

	@Autowired
	private PublishingBlogOperatorDao publishingBlogOperatorDao;

	@Test
	public void testGetPublishingBlogOperatorList() throws DaoException {
		int typeCode = 1;

		List<PublishingBlogOperator> publishingBlogOperatorList = publishingBlogOperatorDao
				.getPublishingBlogOperatorList(typeCode);

		Assert.assertNotNull(publishingBlogOperatorList);
	}

}
