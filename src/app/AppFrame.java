package app;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import javax.swing.JFrame;

import router.Router;

public class AppFrame extends JFrame {

	public AppFrame(ScreenFactory factory) {
		setTitle("Campingcar Rental System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

		// 경로 <-> 화면 팩토리 매핑
		Map<String, Supplier<view.Screen>> routeMap = new HashMap<>();
		routeMap.put("/login", factory::login);
		routeMap.put("/admin", factory::admin);
		routeMap.put("/user", factory::user);
		routeMap.put("/rental/register", factory::rentalRegister);
		routeMap.put("/rental/history", factory::rentalHistory);
		routeMap.put("/maintenance", factory::maintenance);

		Router router = new Router(this, routeMap);

		// 초기 화면
		router.navigate("/login");
		setVisible(true);
	}
}
