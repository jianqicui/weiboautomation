package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.UserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.User;
import org.weiboautomation.entity.UserPhase;

public class UserJdbcDao implements UserDao {

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

	private String getTableName(UserPhase userPhase) {
		String tableName = "user_" + userPhase;

		return tableName;
	}

	@Override
	public void addUser(UserPhase userPhase, User user) throws DaoException {
		String sql = "insert into " + getTableName(userPhase)
				+ " (sn) values (?)";

		try {
			jdbcTemplate.update(sql, user.getSn());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<User> getUserList(UserPhase userPhase, int index, int size)
			throws DaoException {
		String sql = "select id, sn from " + getTableName(userPhase)
				+ " order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isSameUserExisting(UserPhase userPhase, User user)
			throws DaoException {
		String sql = "select count(*) from " + getTableName(userPhase)
				+ " where sn = ?";

		int size;

		try {
			size = jdbcTemplate.queryForObject(sql,
					new Object[] { user.getSn() }, Integer.class);
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return size > 0;
	}

	@Override
	public void deleteUser(UserPhase userPhase, int id) throws DaoException {
		String sql = "delete from " + getTableName(userPhase) + " where id = ?";

		try {
			jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	private String getTableName(int typeCode, UserPhase userPhase) {
		String tableName = "type" + typeCode + "_user_" + userPhase;

		return tableName;
	}

	@Override
	public void addUser(int typeCode, UserPhase userPhase, User user)
			throws DaoException {
		String sql = "insert into " + getTableName(typeCode, userPhase)
				+ " (sn) values (?)";

		try {
			jdbcTemplate.update(sql, user.getSn());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<User> getUserList(int typeCode, UserPhase userPhase, int index,
			int size) throws DaoException {
		String sql = "select id, sn from " + getTableName(typeCode, userPhase)
				+ " order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public boolean isSameUserExisting(int typeCode, UserPhase userPhase,
			User user) throws DaoException {
		String sql = "select count(*) from "
				+ getTableName(typeCode, userPhase) + " where sn = ?";

		int size;

		try {
			size = jdbcTemplate.queryForObject(sql,
					new Object[] { user.getSn() }, Integer.class);
		} catch (Exception e) {
			throw new DaoException(e);
		}

		return size > 0;
	}

	@Override
	public void deleteUser(int typeCode, UserPhase userPhase, int id)
			throws DaoException {
		String sql = "delete from " + getTableName(typeCode, userPhase)
				+ " where id = ?";

		try {
			jdbcTemplate.update(sql, id);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
