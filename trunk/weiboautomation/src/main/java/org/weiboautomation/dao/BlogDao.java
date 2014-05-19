package org.weiboautomation.dao;

import java.util.List;

import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Blog;

public interface BlogDao {

	void addBlog(int typeCode, Blog blog) throws DaoException;

	List<Blog> getBlogList(int typeCode, int index, int size)
			throws DaoException;

	List<Blog> getRandomBlogList(int typeCode, int index, int size)
			throws DaoException;

}
