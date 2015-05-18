package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.PublishingMicroTaskOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PublishingMicroTaskOperator;

public class PublishingMicroTaskOperatorJdbcDao implements
		PublishingMicroTaskOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<PublishingMicroTaskOperator> rowMapper = new PublishingMicroTaskOperatorRowMapper();

	private class PublishingMicroTaskOperatorRowMapper implements
			RowMapper<PublishingMicroTaskOperator> {

		@Override
		public PublishingMicroTaskOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			PublishingMicroTaskOperator publishingMicroTaskOperator = new PublishingMicroTaskOperator();

			publishingMicroTaskOperator.setId(rs.getInt("id"));
			publishingMicroTaskOperator.setCookies(rs.getBytes("cookies"));

			return publishingMicroTaskOperator;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_operator_micro_task_publishing";
	}

	@Override
	public List<PublishingMicroTaskOperator> getPublishingMicroTaskOperatorList(
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
	public void updatePublishingMicroTaskOperator(int typeCode,
			PublishingMicroTaskOperator publishingMicroTaskOperator)
			throws DaoException {
		String sql = "update " + getTableName(typeCode) + " set cookies = ?";

		try {
			jdbcTemplate.update(sql, publishingMicroTaskOperator.getCookies());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
