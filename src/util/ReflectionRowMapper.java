package util;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/** ResultSet -> T 매핑 (snake_case 지원 + @Column 지원) */
public final class ReflectionRowMapper<T> implements RowMapper<T> {

	private final Class<T> type;
	private final ColumnNameTranslator translator;
	private final Map<String, Field> columnToField; // 캐시

	/* 기본 생성자: snake_case -> camelCase 변환 사용 */
	public ReflectionRowMapper(Class<T> type) {
		this(type, ColumnNameTranslator.SNAKE_TO_CAMEL);
	}

	public ReflectionRowMapper(Class<T> type, ColumnNameTranslator translator) {
		this.type = type;
		this.translator = translator;
		this.columnToField = buildFieldCache();
	}

	@Override
	public T map(ResultSet rs) throws SQLException {
		try {
			T instance = type.getDeclaredConstructor().newInstance();
			ResultSetMetaData meta = rs.getMetaData();
			int cols = meta.getColumnCount();

			for (int i = 1; i <= cols; i++) {
				String col = meta.getColumnLabel(i); // alias 우선
				Field field = columnToField.get(col);
				if (field == null) { // 캐시에 없으면 변환 규칙 사용
					field = columnToField.get(translator.toFieldName(col));
				}
				if (field == null)
					continue; // 매핑 대상 필드 없음

				field.setAccessible(true);
				field.set(instance, rs.getObject(i));
			}
			return instance;
		} catch (ReflectiveOperationException ex) {
			throw new SQLException("Reflection mapping error", ex);
		}
	}

	/* 엔티티 필드, 컬럼명 매핑 캐시 */
	private Map<String, Field> buildFieldCache() {
		Map<String, Field> map = new HashMap<>();
		for (Field f : type.getDeclaredFields()) {
			Column colAnn = f.getAnnotation(Column.class);
			if (colAnn != null) {
				map.put(colAnn.value(), f); // 명시적 컬럼명
			}
			map.put(f.getName(), f); // 필드명 자체도 매핑
		}
		return map;
	}
}