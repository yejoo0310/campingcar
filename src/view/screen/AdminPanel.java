package view.screen;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import router.Router;
import view.Screen;

public class AdminPanel extends JPanel implements Screen {
	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());
		add(new JLabel("관리자 화면", SwingConstants.CENTER), BorderLayout.CENTER);

		JButton back = new JButton("뒤로가기");
		back.setEnabled(router.canGoBack());
		back.addActionListener(e -> router.back());
		add(back, BorderLayout.SOUTH);

		return this;
	}
}
