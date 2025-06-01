package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import configure.DBConfig;
import util.ApplicationException;

public class NativeQueryRepository {

	private final DBConfig db;

	public NativeQueryRepository(DBConfig db) {
		this.db = db;
	}

	public List<Map<String, Object>> select(String sql) {
		guard(sql, "select");
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<Map<String, Object>> list = new ArrayList<>();
			ResultSetMetaData meta = rs.getMetaData();
			int colCnt = meta.getColumnCount();

			while (rs.next()) {
				Map<String, Object> row = new LinkedHashMap<>();
				for (int i = 1; i <= colCnt; i++) {
					row.put(meta.getColumnLabel(i), rs.getObject(i));
				}
				list.add(row);
			}
			return list;

		} catch (SQLException e) {
			throw ApplicationException.of("Native SELECT 실행 실패: " + e.getMessage());
		}
	}

	public int update(String sql) {
		guard(sql, "update", "delete");
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			return ps.executeUpdate();

		} catch (SQLException e) {
			throw ApplicationException.of("Native UPDATE/DELETE 실행 실패: " + e.getMessage());
		}
	}

	public long insert(String sql) {
		guard(sql, "insert");
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			int affected = ps.executeUpdate();
			if (affected == 0)
				return 0;

			try (ResultSet keys = ps.getGeneratedKeys()) {
				return keys.next() ? keys.getLong(1) : 0;
			}

		} catch (SQLException e) {
			throw ApplicationException.of("Native INSERT 실행 실패: " + e.getMessage());
		}
	}

	public Object execute(String sql) {
		String cmd = firstToken(sql);
		switch (cmd) {
		case "select":
			return select(sql);
		case "insert":
			return insert(sql);
		case "update":
		case "delete":
			return update(sql);
		default:
			throw ApplicationException.of("허용되지 않은 명령어: " + cmd);
		}
	}

	private static String firstToken(String sql) {
		return sql.trim().toLowerCase(Locale.ROOT).split("\\s+")[0];
	}

	private static void guard(String sql, String... allowed) {
		String cmd = firstToken(sql);
		for (String a : allowed)
			if (cmd.equals(a))
				return;
		throw ApplicationException.of("허용되지 않은 SQL 명령입니다: " + cmd);
	}
}
