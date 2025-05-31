package util;

import java.lang.reflect.Field;

public class ToString {
	public static String autoToString(Object obj) {
		if (obj == null)
			return "null";

		Class<?> clazz = obj.getClass();
		StringBuilder sb = new StringBuilder();
		sb.append(clazz.getSimpleName()).append("(");

		Field[] fields = clazz.getDeclaredFields();
		boolean first = true;

		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if (!first)
					sb.append(", ");
				sb.append(field.getName()).append("=").append(field.get(obj));
				first = false;
			} catch (IllegalAccessException e) {
				sb.append(field.getName()).append("=<access denied>");
			}
		}

		sb.append(")");
		return sb.toString();
	}
}
