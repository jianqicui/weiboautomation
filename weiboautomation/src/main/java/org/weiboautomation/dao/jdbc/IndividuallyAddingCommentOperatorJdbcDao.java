package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.IndividuallyAddingCommentOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.IndividuallyAddingCommentOperator;

public class IndividuallyAddingCommentOperatorJdbcDao implements
		IndividuallyAddingCommentOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<IndividuallyAddingCommentOperator> rowMapper = new IndividuallyAddingCommentOperatorRowMapper();

	private class IndividuallyAddingCommentOperatorRowMapper implements
			RowMapper<IndividuallyAddingCommentOperator> {

		@Override
		public IndividuallyAddingCommentOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			IndividuallyAddingCommentOperator individuallyAddingCommentOperator = new IndividuallyAddingCommentOperator();

			individuallyAddingCommentOperator.setId(rs.getInt("id"));
			individuallyAddingCommentOperator.setSn(rs.getString("sn"));
			individuallyAddingCommentOperator
					.setCookies(rs.getBytes("cookies"));
			individuallyAddingCommentOperator.setText(rs.getString("text"));
			individuallyAddingCommentOperator.setUserSize(rs
					.getInt("user_size"));

			individuallyAddingCommentOperator.setBeginDate(rs
					.getDate("begin_date"));
			individuallyAddingCommentOperator
					.setEndDate(rs.getDate("end_date"));

			String[] hourStrings = StringUtils
					.split(rs.getString("hours"), ',');
			List<Integer> hourList = new ArrayList<Integer>();
			for (String hourString : hourStrings) {
				hourList.add(NumberUtils.toInt(hourString));
			}
			individuallyAddingCommentOperator.setHourList(hourList);

			individuallyAddingCommentOperator.setUserBaseTableName(rs
					.getString("user_base_table_name"));
			individuallyAddingCommentOperator.setUserBaseIndex(rs
					.getInt("user_base_index"));

			return individuallyAddingCommentOperator;
		}

	}

	@Override
	public List<IndividuallyAddingCommentOperator> getIndividuallyAddingCommentOperatorList()
			throws DaoException {
		String sql = "select id, sn, cookies, text, user_size, begin_date, end_date, hours, user_base_table_name, user_base_index from operator_comment_adding_individually order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateIndividuallyAddingCommentOperator(
			IndividuallyAddingCommentOperator individuallyAddingCommentOperator)
			throws DaoException {
		String sql = "update operator_comment_adding_individually set sn = ?, cookies = ?, text = ?, user_size = ?, begin_date = ?, end_date = ?, hours = ?, user_base_table_name = ?, user_base_index = ?";

		try {
			jdbcTemplate.update(sql, individuallyAddingCommentOperator.getSn(),
					individuallyAddingCommentOperator.getCookies(),
					individuallyAddingCommentOperator.getText(),
					individuallyAddingCommentOperator.getUserSize(),
					individuallyAddingCommentOperator.getBeginDate(),
					individuallyAddingCommentOperator.getEndDate(), StringUtils
							.join(individuallyAddingCommentOperator
									.getHourList(), ','),
					individuallyAddingCommentOperator.getUserBaseTableName(),
					individuallyAddingCommentOperator.getUserBaseIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
