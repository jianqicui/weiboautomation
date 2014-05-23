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

	@Override
	public List<FollowingUser> getFollowingUserList() throws DaoException {
		String sql = "select id, code, cookies, user_index from user_following order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateFollowingUser(FollowingUser followingUser)
			throws DaoException {
		String sql = "update user_following set code = ?, cookies = ?, user_index = ? where id = ?";

		try {
			jdbcTemplate.update(sql, followingUser.getCode(),
					followingUser.getCookies(), followingUser.getUserIndex(),
					followingUser.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
