package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.BlogDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Blog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class BlogJdbcDaoTest {

	@Autowired
	private BlogDao blogDao;

	@Test
	public void testGetBlogList() throws DaoException {
		int typeCode = 1;
		int index = 0;
		int size = 10;

		List<Blog> blogList = blogDao.getBlogList(typeCode, index, size);

		Assert.assertNotNull(blogList);
	}

	@Test
	public void testGetRandomBlogList() throws DaoException {
		int typeCode = 1;
		int index = 0;
		int size = 10;

		List<Blog> blogList = blogDao.getRandomBlogList(typeCode, index, size);

		Assert.assertNotNull(blogList);
	}

}
