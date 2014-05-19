package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.TransferingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.TransferingUser;

public class TransferingUserJdbcDao implements TransferingUserDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<TransferingUser> rowMapper = new TransferingUserRowMapper();

	private class TransferingUserRowMapper implements
			RowMapper<TransferingUser> {

		@Override
		public TransferingUser mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			TransferingUser transferingUser = new TransferingUser();

			transferingUser.setId(rs.getInt("id"));
			transferingUser.setCookies(rs.getBytes("cookies"));
			transferingUser.setBlogIndex(rs.getInt("blog_index"));

			return transferingUser;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_user_transfering";
	}

	@Override
	public TransferingUser getTransferingUser(int typeCode) throws DaoException {
		String sql = "select id, cookies, blog_index from "
				+ getTableName(typeCode) + " order by id";

		try {
			return jdbcTemplate.queryForObject(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateTransferingUser(int typeCode,
			TransferingUser transferingUser) throws DaoException {
		String sql = "update " + getTableName(typeCode)
				+ " set cookies = ?, blog_index = ?";

		try {
			jdbcTemplate.update(sql, transferingUser.getCookies(),
					transferingUser.getBlogIndex());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
