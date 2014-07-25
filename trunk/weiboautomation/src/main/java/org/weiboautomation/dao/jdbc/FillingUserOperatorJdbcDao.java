package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.FillingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FillingUserOperator;

public class FillingUserOperatorJdbcDao implements FillingUserOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<FillingUserOperator> rowMapper = new FillingUserOperatorRowMapper();

	private class FillingUserOperatorRowMapper implements
			RowMapper<FillingUserOperator> {

		@Override
		public FillingUserOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FillingUserOperator fillingUserOperator = new FillingUserOperator();

			fillingUserOperator.setId(rs.getInt("id"));
			fillingUserOperator.setCookies(rs.getBytes("cookies"));
			fillingUserOperator.setUserIndex(rs.getInt("user_index"));

			return fillingUserOperator;
		}

	}

	@Override
	public FillingUserOperator getFillingUserOperator() throws DaoException {
		String sql = "select id, cookies, user_index from operator_user_filling order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateFillingUserOperator(
			FillingUserOperator fillingUserOperator) throws DaoException {
		String sql = "update operator_user_filling set cookies = ?, user_index = ?";

		try {
			jdbcTemplate.update(sql, fillingUserOperator.getCookies(),
					fillingUserOperator.getUserIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
