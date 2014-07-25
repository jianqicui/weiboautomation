package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.FollowingUserOperatorDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.FollowingUserOperator;

public class FollowingUserOperatorJdbcDao implements FollowingUserOperatorDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<FollowingUserOperator> rowMapper = new FollowingUserOperatorRowMapper();

	private class FollowingUserOperatorRowMapper implements
			RowMapper<FollowingUserOperator> {

		@Override
		public FollowingUserOperator mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			FollowingUserOperator followingUserOperator = new FollowingUserOperator();

			followingUserOperator.setId(rs.getInt("id"));
			followingUserOperator.setCode(rs.getInt("code"));
			followingUserOperator.setCookies(rs.getBytes("cookies"));
			followingUserOperator.setUserIndex(rs.getInt("user_index"));

			return followingUserOperator;
		}

	}

	@Override
	public List<FollowingUserOperator> getFollowingUserOperatorList()
			throws DaoException {
		String sql = "select id, code, cookies, user_index from operator_user_following order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updateFollowingUserOperator(
			FollowingUserOperator followingUserOperator) throws DaoException {
		String sql = "update operator_user_following set code = ?, cookies = ?, user_index = ? where id = ?";

		try {
			jdbcTemplate.update(sql, followingUserOperator.getCode(),
					followingUserOperator.getCookies(),
					followingUserOperator.getUserIndex(),
					followingUserOperator.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	private String getTableName(int typeCode) {
		String tableName = "type" + typeCode + "_operator_user_following";

		return tableName;
	}

	@Override
	public List<FollowingUserOperator> getFollowingUserOperatorList(int typeCode)
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
	public void updateFollowingUserOperator(int typeCode,
			FollowingUserOperator followingUserOperator) throws DaoException {
		String sql = "update " + getTableName(typeCode)
				+ " set code = ?, cookies = ?, user_index = ? where id = ?";

		try {
			jdbcTemplate.update(sql, followingUserOperator.getCode(),
					followingUserOperator.getCookies(),
					followingUserOperator.getUserIndex(),
					followingUserOperator.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
