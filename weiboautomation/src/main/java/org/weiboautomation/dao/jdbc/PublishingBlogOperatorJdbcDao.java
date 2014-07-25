package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.PublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingBlogOperator;

public class PublishingBlogOperatorJdbcDao implements PublishingBlogOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<PublishingBlogOperator> rowMapper = new PublishingBlogOperatorRowMapper();

	private class PublishingBlogOperatorRowMapper implements
			RowMapper<PublishingBlogOperator> {

		@Override
		public PublishingBlogOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PublishingBlogOperator publishingBlogOperator = new PublishingBlogOperator();

			publishingBlogOperator.setId(rs.getInt("id"));
			publishingBlogOperator.setCookies(rs.getBytes("cookies"));

			return publishingBlogOperator;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_operator_blog_publishing";
	}

	@Override
	public List<PublishingBlogOperator> getPublishingBlogOperatorList(
			int typeCode) throws DaoException {
		String sql = "select id, cookies from " + getTableName(typeCode)
				+ " order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updatePublishingBlogOperator(int typeCode,
			PublishingBlogOperator publishingBlogOperator) throws DaoException {
		String sql = "update " + getTableName(typeCode) + " set cookies = ?";

		try {
			jdbcTemplate.update(sql, publishingBlogOperator.getCookies());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
