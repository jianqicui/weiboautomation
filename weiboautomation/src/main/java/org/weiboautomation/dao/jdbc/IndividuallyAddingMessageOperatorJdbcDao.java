package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.IndividuallyAddingMessageOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingMessageOperator;

public class IndividuallyAddingMessageOperatorJdbcDao implements
		IndividuallyAddingMessageOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<IndividuallyAddingMessageOperator> rowMapper = new IndividuallyAddingMessageOperatorRowMapper();

	private class IndividuallyAddingMessageOperatorRowMapper implements
			RowMapper<IndividuallyAddingMessageOperator> {

		@Override
		public IndividuallyAddingMessageOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			IndividuallyAddingMessageOperator individuallyAddingMessageOperator = new IndividuallyAddingMessageOperator();

			individuallyAddingMessageOperator.setId(rs.getInt("id"));
			individuallyAddingMessageOperator.setSn(rs.getString("sn"));
			individuallyAddingMessageOperator
					.setCookies(rs.getBytes("cookies"));
			individuallyAddingMessageOperator.setText(rs.getString("text"));
			individuallyAddingMessageOperator.setUserSize(rs
					.getInt("user_size"));

			individuallyAddingMessageOperator.setBeginDate(rs
					.getDate("begin_date"));
			individuallyAddingMessageOperator
					.setEndDate(rs.getDate("end_date"));

			String[] hourStrings = StringUtils
					.split(rs.getString("hours"), ',');
			List<Integer> hourList = new ArrayList<Integer>();
			for (String hourString : hourStrings) {
				hourList.add(NumberUtils.toInt(hourString));
			}
			individuallyAddingMessageOperator.setHourList(hourList);

			individuallyAddingMessageOperator.setUserBaseTableName(rs
					.getString("user_base_table_name"));
			individuallyAddingMessageOperator.setUserBaseIndex(rs
					.getInt("user_base_index"));

			return individuallyAddingMessageOperator;
		}

	}

	@Override
	public List<IndividuallyAddingMessageOperator> getIndividuallyAddingMessageOperatorList()
			throws DaoException {
		String sql = "select id, sn, cookies, text, user_size, begin_date, end_date, hours, user_base_table_name, user_base_index from operator_message_adding_individually order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateIndividuallyAddingMessageOperator(
			IndividuallyAddingMessageOperator individuallyAddingMessageOperator)
			throws DaoException {
		String sql = "update operator_message_adding_individually set sn = ?, cookies = ?, text = ?, user_size = ?, begin_date = ?, end_date = ?, hours = ?, user_base_table_name = ?, user_base_index = ?";

		try {
			jdbcTemplate.update(sql, individuallyAddingMessageOperator.getSn(),
					individuallyAddingMessageOperator.getCookies(),
					individuallyAddingMessageOperator.getText(),
					individuallyAddingMessageOperator.getUserSize(),
					individuallyAddingMessageOperator.getBeginDate(),
					individuallyAddingMessageOperator.getEndDate(), StringUtils
							.join(individuallyAddingMessageOperator
									.getHourList(), ','),
					individuallyAddingMessageOperator.getUserBaseTableName(),
					individuallyAddingMessageOperator.getUserBaseIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
