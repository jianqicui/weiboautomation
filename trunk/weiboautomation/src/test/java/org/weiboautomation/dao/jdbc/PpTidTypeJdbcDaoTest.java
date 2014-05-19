package org.weiboautomation.dao.jdbc;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.weiboautomation.dao.PpTidTypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PpTidType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:applicationContext.xml" })
public class PpTidTypeJdbcDaoTest {

	@Autowired
	private PpTidTypeDao ppTidTypeDao;

	@Test
	public void testGetPpTidTypeList() throws DaoException {
		List<PpTidType> ppTidTypeList = ppTidTypeDao.getPpTidTypeList();

		Assert.assertNotNull(ppTidTypeList);
	}

}
