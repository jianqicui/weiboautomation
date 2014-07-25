package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.FollowedUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;

public class FollowedUserJdbcDao implements FollowedUserDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<User> rowMapper = new UserRowMapper();

	private class UserRowMapper implements RowMapper<User> {

		@Override
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();

			user.setId(rs.getInt("id"));
			user.setSn(rs.getString("sn"));

			return user;
		}

	}

	private String getTableName(int followingUserCode) {
		String tableName = "user" + followingUserCode + "_followed";

		return tableName;
	}

	@Override
	public void addUser(int followingUserCode, User user) throws DaoException {
		String sql = "insert into " + getTableName(followingUserCode)
				+ " (sn, created_timestamp) values (?, ?)";

		try {
			jdbcTemplate.update(sql, user.getSn(), new Date());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<User> getUserListBeforeDays(int followingUserCode, int days,
			int index, int size) throws DaoException {
		String sql = "select id, sn from "
				+ getTableName(followingUserCode)
				+ " where datediff(now(), created_timestamp) >= ?  order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, days, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteUser(int followingUserCode, int id) throws DaoException {
		String sql = "delete from " + getTableName(followingUserCode)
				+ " where id = ?";

		try {
			jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	private String getTableName(int typeCode, int followingUserCode) {
		String tableName = "type" + typeCode + "_user" + followingUserCode
				+ "_followed";

		return tableName;
	}

	@Override
	public void addUser(int typeCode, int followingUserCode, User user)
			throws DaoException {
		String sql = "insert into " + getTableName(typeCode, followingUserCode)
				+ " (sn, created_timestamp) values (?, ?)";

		try {
			jdbcTemplate.update(sql, user.getSn(), new Date());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<User> getUserListBeforeDays(int typeCode,
			int followingUserCode, int days, int index, int size)
			throws DaoException {
		String sql = "select id, sn from "
				+ getTableName(typeCode, followingUserCode)
				+ " where datediff(now(), created_timestamp) >= ?  order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, days, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void deleteUser(int typeCode, int followingUserCode, int id)
			throws DaoException {
		String sql = "delete from " + getTableName(typeCode, followingUserCode)
				+ " where id = ?";

		try {
			jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
