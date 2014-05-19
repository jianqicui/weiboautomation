package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.QueryingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUser;

public class QueryingUserJdbcDao implements QueryingUserDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<QueryingUser> rowMapper = new QueryingUserRowMapper();

	private class QueryingUserRowMapper implements RowMapper<QueryingUser> {

		@Override
		public QueryingUser mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			QueryingUser queryingUser = new QueryingUser();

			queryingUser.setId(rs.getInt("id"));
			queryingUser.setCookies(rs.getBytes("cookies"));

			return queryingUser;
		}

	}

	@Override
	public QueryingUser getQueryingUser() throws DaoException {
		String sql = "select id, cookies from user_querying order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateQueryingUser(QueryingUser queryingUser)
			throws DaoException {
		String sql = "update user_querying set cookies = ?";

		try {
			jdbcTemplate.update(sql, queryingUser.getCookies());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
