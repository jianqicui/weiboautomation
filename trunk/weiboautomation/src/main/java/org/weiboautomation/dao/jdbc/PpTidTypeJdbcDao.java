package org.weiboautomation.dao.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.weiboautomation.dao.PpTidTypeDao;
import org.weiboautomation.dao.exception.DaoException;
import org.weiboautomation.entity.PpTidType;
import org.weiboautomation.entity.Type;

public class PpTidTypeJdbcDao implements PpTidTypeDao {

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private RowMapper<PpTidType> rowMapper = new PpTidTypeRowMapper();

	private class PpTidTypeRowMapper implements RowMapper<PpTidType> {

		@Override
		public PpTidType mapRow(ResultSet rs, int rowNum) throws SQLException {
			PpTidType ppTidType = new PpTidType();

			ppTidType.setId(rs.getInt("pp_tid_type.id"));
			ppTidType.setPpTid(rs.getInt("pp_tid_type.pp_tid"));
			ppTidType.setPpTidPageSize(rs
					.getInt("pp_tid_type.pp_tid_page_size"));
			ppTidType.setPpTidPageIndex(rs
					.getInt("pp_tid_type.pp_tid_page_index"));

			Type type = new Type();

			type.setId(rs.getInt("type.id"));
			type.setCode(rs.getInt("type.code"));
			type.setName(rs.getString("type.name"));

			ppTidType.setType(type);

			return ppTidType;
		}

	}

	@Override
	public List<PpTidType> getPpTidTypeList() throws DaoException {
		String sql = "select pp_tid_type.id, pp_tid_type.pp_tid, pp_tid_type.pp_tid_page_size, pp_tid_type.pp_tid_page_index, type.id, type.code, type.name " +
				"from pp_tid_type, type where pp_tid_type.type_id = type.id order by pp_tid_type.id";

		try {
			return jdbcTemplate.query(sql, rowMapper);
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}

	@Override
	public void updatePpTidType(PpTidType ppTidType) throws DaoException {
		String sql = "update pp_tid_type set pp_tid = ?, pp_tid_page_size = ?, pp_tid_page_index = ? where id = ?";

		try {
			jdbcTemplate.update(sql, ppTidType.getPpTid(),
					ppTidType.getPpTidPageSize(),
					ppTidType.getPpTidPageIndex(), ppTidType.getId());
		} catch (Exception e) {
			throw new DaoException(e);
		}
	}
	
}
