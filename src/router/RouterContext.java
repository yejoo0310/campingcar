/* src/router/RouterContext.java */
package router;

import java.util.Stack;

class RouterContext {
	private final Stack<String> history = new Stack<>();
	private String currentPath;

	void push(String path) {
		if (currentPath != null) {
			history.push(currentPath);
		}
		currentPath = path;
	}

	/** 이전 경로로 이동하면서 currentPath 도 같이 갱신 */
	String pop() {
		if (history.isEmpty()) {
			return null;
		}
		currentPath = history.pop();
		return currentPath;
	}

	boolean canGoBack() {
		return !history.isEmpty();
	}

	String current() {
		return currentPath;
	}
}
