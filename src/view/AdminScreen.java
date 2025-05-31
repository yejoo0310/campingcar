package view;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class AdminScreen extends JFrame {

	public AdminScreen() {
		init();
	}

	private void init() {
		setTitle("관리자화면");
		setSize(400, 200);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		add(new JLabel("관리자화면", SwingConstants.CENTER));
	}
}
