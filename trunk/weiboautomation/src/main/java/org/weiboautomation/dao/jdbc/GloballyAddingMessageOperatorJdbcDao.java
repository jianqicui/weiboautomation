package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.GloballyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingMessageOperator;

public class GloballyAddingMessageOperatorJdbcDao implements
		GloballyAddingMessageOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<GloballyAddingMessageOperator> rowMapper = new GloballyAddingMessageOperatorRowMapper();

	private class GloballyAddingMessageOperatorRowMapper implements
			RowMapper<GloballyAddingMessageOperator> {

		@Override
		public GloballyAddingMessageOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			GloballyAddingMessageOperator globallyAddingMessageOperator = new GloballyAddingMessageOperator();

			globallyAddingMessageOperator.setId(rs.getInt("id"));
			globallyAddingMessageOperator.setSn(rs.getString("sn"));
			globallyAddingMessageOperator.setCookies(rs.getBytes("cookies"));
			globallyAddingMessageOperator.setText(rs.getString("text"));
			globallyAddingMessageOperator.setUserSize(rs.getInt("user_size"));

			globallyAddingMessageOperator
					.setBeginDate(rs.getDate("begin_date"));
			globallyAddingMessageOperator.setEndDate(rs.getDate("end_date"));

			String[] hourStrings = StringUtils
					.split(rs.getString("hours"), ',');
			List<Integer> hourList = new ArrayList<Integer>();
			for (String hourString : hourStrings) {
				hourList.add(NumberUtils.toInt(hourString));
			}
			globallyAddingMessageOperator.setHourList(hourList);

			return globallyAddingMessageOperator;
		}

	}

	@Override
	public List<GloballyAddingMessageOperator> getGloballyAddingMessageOperatorList()
			throws DaoException {
		String sql = "select id, sn, cookies, text, user_size, begin_date, end_date, hours from operator_message_adding_globally order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateGloballyAddingMessageOperator(
			GloballyAddingMessageOperator globallyAddingMessageOperator)
			throws DaoException {
		String sql = "update operator_message_adding_globally set sn = ?, cookies = ?, text = ?, user_size = ?, begin_date = ?, end_date = ?, hours = ?";

		try {
			jdbcTemplate.update(
					sql,
					globallyAddingMessageOperator.getSn(),
					globallyAddingMessageOperator.getCookies(),
					globallyAddingMessageOperator.getText(),
					globallyAddingMessageOperator.getUserSize(),
					globallyAddingMessageOperator.getBeginDate(),
					globallyAddingMessageOperator.getEndDate(),
					StringUtils.join(
							globallyAddingMessageOperator.getHourList(), ','));
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
