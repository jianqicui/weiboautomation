package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.TiminglyPublishingBlogOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TiminglyPublishingBlogOperator;

public class TiminglyPublishingBlogOperatorJdbcDao implements
		TiminglyPublishingBlogOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<TiminglyPublishingBlogOperator> rowMapper = new TiminglyPublishingBlogOperatorRowMapper();

	private class TiminglyPublishingBlogOperatorRowMapper implements
			RowMapper<TiminglyPublishingBlogOperator> {

		@Override
		public TiminglyPublishingBlogOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TiminglyPublishingBlogOperator timinglyPublishingBlogOperator = new TiminglyPublishingBlogOperator();

			timinglyPublishingBlogOperator.setId(rs.getInt("id"));
			timinglyPublishingBlogOperator.setSn(rs.getString("sn"));
			timinglyPublishingBlogOperator.setCookies(rs.getBytes("cookies"));

			timinglyPublishingBlogOperator.setBeginDate(rs
					.getDate("begin_date"));
			timinglyPublishingBlogOperator.setEndDate(rs.getDate("end_date"));

			String[] hourStrings = StringUtils
					.split(rs.getString("hours"), ',');
			List<Integer> hourList = new ArrayList<Integer>();
			for (String hourString : hourStrings) {
				hourList.add(NumberUtils.toInt(hourString));
			}
			timinglyPublishingBlogOperator.setHourList(hourList);

			return timinglyPublishingBlogOperator;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_operator_blog_publishing_timingly";
	}

	@Override
	public List<TiminglyPublishingBlogOperator> getTiminglyPublishingBlogOperatorList(
			int typeCode) throws DaoException {
		String sql = "select id, sn, cookies, begin_date, end_date, hours from "
				+ getTableName(typeCode) + " order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateTiminglyPublishingBlogOperator(int typeCode,
			TiminglyPublishingBlogOperator timinglyPublishingBlogOperator)
			throws DaoException {
		String sql = "update "
				+ getTableName(typeCode)
				+ " set sn = ?, cookies = ?, begin_date = ?, end_date = ?, hours = ?";

		try {
			jdbcTemplate.update(
					sql,
					timinglyPublishingBlogOperator.getSn(),
					timinglyPublishingBlogOperator.getCookies(),
					timinglyPublishingBlogOperator.getBeginDate(),
					timinglyPublishingBlogOperator.getEndDate(),
					StringUtils.join(
							timinglyPublishingBlogOperator.getHourList(), ','));
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
