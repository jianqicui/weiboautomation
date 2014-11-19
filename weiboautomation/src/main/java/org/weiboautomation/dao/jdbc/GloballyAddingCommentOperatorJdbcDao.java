package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.GloballyAddingCommentOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.GloballyAddingCommentOperator;

public class GloballyAddingCommentOperatorJdbcDao implements
		GloballyAddingCommentOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<GloballyAddingCommentOperator> rowMapper = new GloballyAddingCommentOperatorRowMapper();

	private class GloballyAddingCommentOperatorRowMapper implements
			RowMapper<GloballyAddingCommentOperator> {

		@Override
		public GloballyAddingCommentOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			GloballyAddingCommentOperator globallyAddingCommentOperator = new GloballyAddingCommentOperator();

			globallyAddingCommentOperator.setId(rs.getInt("id"));
			globallyAddingCommentOperator.setSn(rs.getString("sn"));
			globallyAddingCommentOperator.setCookies(rs.getBytes("cookies"));
			globallyAddingCommentOperator.setText(rs.getString("text"));
			globallyAddingCommentOperator.setUserSize(rs.getInt("user_size"));

			globallyAddingCommentOperator
					.setBeginDate(rs.getDate("begin_date"));
			globallyAddingCommentOperator.setEndDate(rs.getDate("end_date"));

			String[] hourStrings = StringUtils
					.split(rs.getString("hours"), ',');
			List<Integer> hourList = new ArrayList<Integer>();
			for (String hourString : hourStrings) {
				hourList.add(NumberUtils.toInt(hourString));
			}
			globallyAddingCommentOperator.setHourList(hourList);

			return globallyAddingCommentOperator;
		}

	}

	@Override
	public List<GloballyAddingCommentOperator> getGloballyAddingCommentOperatorList()
			throws DaoException {
		String sql = "select id, sn, cookies, text, user_size, begin_date, end_date, hours from operator_comment_adding_globally order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateGloballyAddingCommentOperator(
			GloballyAddingCommentOperator globallyAddingCommentOperator)
			throws DaoException {
		String sql = "update operator_comment_adding_globally set sn = ?, cookies = ?, text = ?, user_size = ?, begin_date = ?, end_date = ?, hours = ?";

		try {
			jdbcTemplate.update(
					sql,
					globallyAddingCommentOperator.getSn(),
					globallyAddingCommentOperator.getCookies(),
					globallyAddingCommentOperator.getText(),
					globallyAddingCommentOperator.getUserSize(),
					globallyAddingCommentOperator.getBeginDate(),
					globallyAddingCommentOperator.getEndDate(),
					StringUtils.join(
							globallyAddingCommentOperator.getHourList(), ','));
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
