package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.BlogDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Blog;

public class BlogJdbcDao implements BlogDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<Blog> rowMapper = new BlogRowMapper();

	private class BlogRowMapper implements RowMapper<Blog> {

		@Override
		public Blog mapRow(ResultSet rs, int rowNum) throws SQLException {
			Blog blog = new Blog();

			blog.setId(rs.getInt("id"));
			blog.setText(rs.getString("text"));
			blog.setPicture(rs.getString("picture"));

			return blog;
		}

	}

	private String getTableName(int typeCode) {
		String tableName = "type" + typeCode + "_blog";

		return tableName;
	}

	@Override
	public List<Blog> getDescendingBlogList(int typeCode, int index, int size)
			throws DaoException {
		String sql = "select id, text, picture from " + getTableName(typeCode)
				+ " order by id desc limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void addBlog(int typeCode, Blog blog) throws DaoException {
		String sql = "insert into " + getTableName(typeCode)
				+ " (text, picture) values (?, ?)";

		try {
			jdbcTemplate.update(sql, blog.getText(), blog.getPicture());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Blog> getBlogList(int typeCode, int index, int size)
			throws DaoException {
		String sql = "select id, text, picture from " + getTableName(typeCode)
				+ " order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<Blog> getRandomBlogList(int typeCode, int index, int size)
			throws DaoException {
		String sql = "select id, text, picture from " + getTableName(typeCode)
				+ " order by rand() limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
