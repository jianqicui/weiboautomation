package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.TiminglyPublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TiminglyPublishingBlogOperator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TiminglyPublishingBlogOperatorJdbcDaoTest {

	@Autowired
	private TiminglyPublishingBlogOperatorDao timinglyPublishingBlogOperatorDao;

	@Test
	public void testGetTiminglyPublishingBlogOperatorList() throws DaoException {
		int typeCode = 1;

		List<TiminglyPublishingBlogOperator> timinglyPublishingBlogOperatorList = timinglyPublishingBlogOperatorDao
				.getTiminglyPublishingBlogOperatorList(typeCode);

		Assert.assertNotNull(timinglyPublishingBlogOperatorList);
	}

}
