package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.TransferingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUserOperator;

public class TransferingUserOperatorJdbcDao implements
		TransferingUserOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<TransferingUserOperator> rowMapper = new TransferingUserOperatorRowMapper();

	private class TransferingUserOperatorRowMapper implements
			RowMapper<TransferingUserOperator> {

		@Override
		public TransferingUserOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TransferingUserOperator transferingUserOperator = new TransferingUserOperator();

			transferingUserOperator.setId(rs.getInt("id"));
			transferingUserOperator.setCookies(rs.getBytes("cookies"));
			transferingUserOperator.setUserIndex(rs.getInt("user_index"));

			return transferingUserOperator;
		}

	}

	@Override
	public TransferingUserOperator getTransferingUserOperator()
			throws DaoException {
		String sql = "select id, cookies, user_index from operator_user_transfering order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateTransferingUserOperator(
			TransferingUserOperator transferingUserOperator)
			throws DaoException {
		String sql = "update operator_user_transfering set cookies = ?, user_index = ?";

		try {
			jdbcTemplate.update(sql, transferingUserOperator.getCookies(),
					transferingUserOperator.getUserIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
