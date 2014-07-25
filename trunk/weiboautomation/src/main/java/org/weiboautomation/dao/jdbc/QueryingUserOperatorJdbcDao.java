package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.QueryingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.QueryingUserOperator;

public class QueryingUserOperatorJdbcDao implements QueryingUserOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<QueryingUserOperator> rowMapper = new QueryingUserOperatorRowMapper();

	private class QueryingUserOperatorRowMapper implements
			RowMapper<QueryingUserOperator> {

		@Override
		public QueryingUserOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			QueryingUserOperator queryingUserOperator = new QueryingUserOperator();

			queryingUserOperator.setId(rs.getInt("id"));
			queryingUserOperator.setCookies(rs.getBytes("cookies"));

			return queryingUserOperator;
		}

	}

	@Override
	public QueryingUserOperator getQueryingUserOperator() throws DaoException {
		String sql = "select id, cookies from operator_user_querying order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateQueryingUserOperator(
			QueryingUserOperator queryingUserOperator) throws DaoException {
		String sql = "update operator_user_querying set cookies = ?";

		try {
			jdbcTemplate.update(sql, queryingUserOperator.getCookies());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
