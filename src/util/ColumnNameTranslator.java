package util;

@FunctionalInterface
public interface ColumnNameTranslator {
	String toFieldName(String columnLabel);

	/** 기본 snake_case ➜ camelCase 구현 */
	static ColumnNameTranslator SNAKE_TO_CAMEL = label -> {
		StringBuilder sb = new StringBuilder();
		boolean upperNext = false;
		for (char ch : label.toCharArray()) {
			if (ch == '_') {
				upperNext = true;
			} else {
				sb.append(upperNext ? Character.toUpperCase(ch) : ch);
				upperNext = false;
			}
		}
		return sb.toString();
	};
}