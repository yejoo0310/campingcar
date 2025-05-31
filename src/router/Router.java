package router;

import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.Screen;

public class Router {
	private final JFrame frame;
	private final Map<String, Supplier<Screen>> routes;
	private final RouterContext ctx = new RouterContext();

	public Router(JFrame frame, Map<String, Supplier<Screen>> routes) {
		this.frame = frame;
		this.routes = routes;
	}

	public void navigate(String path) {
		Supplier<Screen> factory = routes.get(path);
		if (factory == null)
			throw new IllegalArgumentException("No route: " + path);
		ctx.push(path);
		render(factory.get());
	}

	public void back() {
		if (!ctx.canGoBack())
			return;
		String prev = ctx.pop();
		render(routes.get(prev).get());
	}

	public boolean canGoBack() {
		return ctx.canGoBack();
	}

	/* ---------- 내부 ---------- */
	private void render(Screen screen) {
		JPanel view = screen.render(this);
		frame.setContentPane(view);
		frame.revalidate();
		frame.repaint();
	}
}
