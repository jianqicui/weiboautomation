package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.FollowingUserDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUser;

public class FollowingUserJdbcDao implements FollowingUserDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<FollowingUser> rowMapper = new FollowingUserRowMapper();

	private class FollowingUserRowMapper implements RowMapper<FollowingUser> {

		@Override
		public FollowingUser mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FollowingUser followingUser = new FollowingUser();

			followingUser.setId(rs.getInt("id"));
			followingUser.setCode(rs.getInt("code"));
			followingUser.setCookies(rs.getBytes("cookies"));
			followingUser.setUserIndex(rs.getInt("user_index"));

			return followingUser;
		}

	}

	private String getTableName(int typeCode) {
		return "type" + typeCode + "_user_following";
	}

	@Override
	public List<FollowingUser> getFollowingUserList(int typeCode)
			throws DaoException {
		String sql = "select id, code, cookies, user_index from "
				+ getTableName(typeCode) + " order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateFollowingUser(int typeCode, FollowingUser followingUser)
			throws DaoException {
		String sql = "update " + getTableName(typeCode)
				+ " set code = ?, cookies = ?, user_index = ? where id = ?";

		try {
			jdbcTemplate.update(sql, followingUser.getCode(),
					followingUser.getCookies(), followingUser.getUserIndex(),
					followingUser.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
