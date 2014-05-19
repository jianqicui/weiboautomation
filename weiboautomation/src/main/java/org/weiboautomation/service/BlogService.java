package org.weiboautomation.service;

import java.util.List;

import org.weiboautomation.entity.Blog;
import org.weiboautomation.service.exception.ServiceException;

public interface BlogService {

	void addBlog(int typeCode, Blog blog) throws ServiceException;

	List<Blog> getBlogList(int typeCode, int index, int size)
			throws ServiceException;

	List<Blog> getRandomBlogList(int typeCode, int index, int size)
			throws ServiceException;

}
