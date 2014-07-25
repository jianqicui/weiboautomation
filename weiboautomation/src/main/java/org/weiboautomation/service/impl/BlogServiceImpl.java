package org.weiboautomation.service.impl;

import java.util.List;

import org.weiboautomation.dao.BlogDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Blog;
import org.weiboautomation.service.BlogService;
import org.weiboautomation.service.exception.ServiceException;

public class BlogServiceImpl implements BlogService {

	private BlogDao blogDao;

	public void setBlogDao(BlogDao blogDao) {
		this.blogDao = blogDao;
	}

	@Override
	public List<Blog> getDescendingBlogList(int typeCode, int index, int size)
			throws ServiceException {
		try {
			return blogDao.getDescendingBlogList(typeCode, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void addBlog(int typeCode, Blog blog) throws ServiceException {
		try {
			blogDao.addBlog(typeCode, blog);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<Blog> getBlogList(int typeCode, int index, int size)
			throws ServiceException {
		try {
			return blogDao.getBlogList(typeCode, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Blog> getRandomBlogList(int typeCode, int index, int size)
			throws ServiceException {
		try {
			return blogDao.getRandomBlogList(typeCode, index, size);
		} catch (DaoException e) {
			throw new ServiceException(e);
		}
	}

}
