package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.UserProfileDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.UserProfile;

public class UserProfileJdbcDao implements UserProfileDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<UserProfile> rowMapper = new UserProfileRowMapper();

	private class UserProfileRowMapper implements RowMapper<UserProfile> {

		@Override
		public UserProfile mapRow(ResultSet rs, int rowNum) throws SQLException {
			UserProfile userProfile = new UserProfile();

			userProfile.setId(rs.getInt("id"));
			userProfile.setSn(rs.getString("sn"));
			userProfile.setName(rs.getString("name"));
			userProfile.setGender(rs.getString("gender"));
			userProfile.setLocation(rs.getString("location"));

			String[] tags = StringUtils.split(rs.getString("tags"), ',');
			userProfile.setTagList(Arrays.asList(tags));

			return userProfile;
		}

	}

	@Override
	public List<UserProfile> getDescendingUserProfileList(int index, int size)
			throws DaoException {
		String sql = "select id, sn, name, gender, location, tags from user_profile order by id desc limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void addUserProfile(UserProfile userProfile) throws DaoException {
		String sql = "insert into user_profile (sn, name, gender, location, tags) values (?, ?, ?, ?, ?)";

		try {
			jdbcTemplate.update(sql, userProfile.getSn(),
					userProfile.getName(), userProfile.getGender(),
					userProfile.getLocation(),
					StringUtils.join(userProfile.getTagList(), ','));
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public List<UserProfile> getUserProfileList(int index, int size)
			throws DaoException {
		String sql = "select id, sn, name, gender, location, tags from user_profile order by id limit ?, ?";

		try {
			return jdbcTemplate.query(sql, rowMapper, index, size);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
