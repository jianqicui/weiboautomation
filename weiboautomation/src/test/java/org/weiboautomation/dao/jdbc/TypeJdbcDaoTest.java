package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.TypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Type;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class TypeJdbcDaoTest {

	@Autowired
	private TypeDao typeDao;

	@Test
	public void testGetTypeList() throws DaoException {
		List<Type> typeList = typeDao.getTypeList();

		Assert.assertNotNull(typeList);
	}

}
