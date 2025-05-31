package util;

import java.sql.ResultSet;
import java.sql.SQLException;

/** ResultSet ➔ 도메인 객체 매핑용 전략 인터페이스 */
@FunctionalInterface
public interface RowMapper<T> {
	T map(ResultSet rs) throws SQLException;
}