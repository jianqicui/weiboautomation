package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.PublishingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingUser;

public class PublishingUserJdbcDao implements PublishingUserDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<PublishingUser> rowMapper = new PublishingUserRowMapper();

	private class PublishingUserRowMapper implements RowMapper<PublishingUser> {

		@Override
		public PublishingUser mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PublishingUser publishingUser = new PublishingUser();

			publishingUser.setId(rs.getInt("id"));
			publishingUser.setCookies(rs.getBytes("cookies"));

			return publishingUser;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_user_publishing";
	}

	@Override
	public List<PublishingUser> getPublishingUserList(int typeCode)
			throws DaoException {
		String sql = "select id, cookies from " + getTableName(typeCode)
				+ " order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updatePublishingUser(int typeCode, PublishingUser publishingUser)
			throws DaoException {
		String sql = "update " + getTableName(typeCode) + " set cookies = ?";

		try {
			jdbcTemplate.update(sql, publishingUser.getCookies());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
}
