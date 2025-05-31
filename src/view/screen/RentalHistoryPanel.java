package view.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import router.Router;
import view.Screen;

public class RentalHistoryPanel extends JPanel implements Screen {

	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());

		// 중앙: 안내 텍스트
		JLabel titleLabel = new JLabel("대여 기록 화면", SwingConstants.CENTER);
		add(titleLabel, BorderLayout.CENTER);

		// 좌측 상단: 뒤로가기 버튼을 별도의 패널로 좌측 정렬
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(event -> router.back());
		northPanel.add(backButton);
		add(northPanel, BorderLayout.NORTH);

		return this;
	}
}
