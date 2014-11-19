package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.UserBaseDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserBase;

public class UserBaseJdbcDao implements UserBaseDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<UserBase> rowMapper = new UserBaseRowMapper();

	private class UserBaseRowMapper implements RowMapper<UserBase> {

		@Override
		public UserBase mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserBase userBase = new UserBase();

			userBase.setId(rs.getInt("id"));
			userBase.setSn(rs.getString("sn"));
			userBase.setName(rs.getString("name"));

			return userBase;
		}

	}

	@Override
	public List<UserBase> getUserBaseList(String tableName, int index, int size)
			throws DaoException {
		String sql = "select id, sn, name from " + tableName
				+ " order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
