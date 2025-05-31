package app;

import java.util.Map;

import javax.swing.JFrame;

import router.Router;

public class AppFrame extends JFrame {

	public AppFrame(ScreenFactory factory) {
		setTitle("Campingcar Rental System");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

		/* 경로 <-> 화면 팩토리 매핑 */
		Router router = new Router(this,
				Map.of("/login", factory::login, "/admin", factory::admin, "/user", factory::user, "/rental/register",
						factory::rentalRegister, "/rental/history", factory::rentalHistory, "/maintenance",
						factory::maintenance));

		/* 초기 화면 */
		router.navigate("/login");
		setVisible(true);
	}
}
