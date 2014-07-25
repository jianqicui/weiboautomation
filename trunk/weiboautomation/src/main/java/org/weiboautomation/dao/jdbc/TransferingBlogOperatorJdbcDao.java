package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.TransferingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingBlogOperator;

public class TransferingBlogOperatorJdbcDao implements
		TransferingBlogOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<TransferingBlogOperator> rowMapper = new TransferingBlogOperatorRowMapper();

	private class TransferingBlogOperatorRowMapper implements
			RowMapper<TransferingBlogOperator> {

		@Override
		public TransferingBlogOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TransferingBlogOperator transferingBlogOperator = new TransferingBlogOperator();

			transferingBlogOperator.setId(rs.getInt("id"));
			transferingBlogOperator.setCookies(rs.getBytes("cookies"));
			transferingBlogOperator.setBlogIndex(rs.getInt("blog_index"));

			return transferingBlogOperator;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_operator_blog_transfering";
	}

	@Override
	public TransferingBlogOperator getTransferingBlogOperator(int typeCode)
			throws DaoException {
		String sql = "select id, cookies, blog_index from "
				+ getTableName(typeCode) + " order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateTransferingBlogOperator(int typeCode,
			TransferingBlogOperator transferingBlogOperator)
			throws DaoException {
		String sql = "update " + getTableName(typeCode)
				+ " set cookies = ?, blog_index = ?";

		try {
			jdbcTemplate.update(sql, transferingBlogOperator.getCookies(),
					transferingBlogOperator.getBlogIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
