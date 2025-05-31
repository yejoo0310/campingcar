package util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import configure.DBConfig;

public final class QueryExecutor {

	private final DBConfig db;

	public QueryExecutor(DBConfig db) {
		this.db = db;
	}

	/* SELECT 다건 */
	public <T> List<T> query(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
		try (Connection c = db.getConnection();
				PreparedStatement ps = prepare(c, sql, params);
				ResultSet rs = ps.executeQuery()) {

			List<T> list = new ArrayList<>();
			while (rs.next())
				list.add(mapper.map(rs));
			return list;
		}
	}

	/* SELECT 단건 */
	public <T> Optional<T> queryForObject(String sql, RowMapper<T> mapper, Object... params) throws SQLException {
		List<T> rows = query(sql, mapper, params);
		return rows.isEmpty() ? Optional.empty() : Optional.of(rows.get(0));
	}

	/* INSERT / UPDATE / DELETE */
	public int update(String sql, Object... params) throws SQLException {
		try (Connection c = db.getConnection(); PreparedStatement ps = prepare(c, sql, params)) {
			return ps.executeUpdate();
		}
	}

	/* 공통 PreparedStatement 생성 */
	private PreparedStatement prepare(Connection c, String sql, Object... params) throws SQLException {
		PreparedStatement ps = c.prepareStatement(sql);
		for (int i = 0; i < params.length; i++)
			ps.setObject(i + 1, params[i]);
		return ps;
	}
}