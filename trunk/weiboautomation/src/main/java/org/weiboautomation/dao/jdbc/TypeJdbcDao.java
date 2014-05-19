package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.TypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.Type;

public class TypeJdbcDao implements TypeDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<Type> rowMapper = new TypeRowMapper();

	private class TypeRowMapper implements RowMapper<Type> {

		@Override
		public Type mapRow(ResultSet rs, int rowNum) throws SQLException {
			Type type = new Type();

			type.setId(rs.getInt("id"));
			type.setCode(rs.getInt("code"));
			type.setName(rs.getString("name"));

			return type;
		}

	}

	@Override
	public List<Type> getTypeList() throws DaoException {
		String sql = "select id, code, name from type order by id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

}
