package view.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import router.Router;
import service.RentalService;
import view.Screen;

public class UserPanel extends JPanel implements Screen {
	private final RentalService rentalService;

	public UserPanel(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(15, 10, 15, 10);

		// 좌측 상단에 뒤로가기 버튼
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(event -> router.back());

		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		constraints.weightx = 1.0;
		constraints.weighty = 0.0;
		add(backButton, constraints);

		// 주요 버튼들은 중앙(중앙정렬) 세로 배치
		JButton rentalButton = new JButton("캠핑카 대여하기");
		JButton rentalHistoryButton = new JButton("대여 기록 보기");
		JButton maintenanceButton = new JButton("정비 의뢰하기");

		rentalButton.addActionListener(event -> router.navigate("/rental/register"));
		rentalHistoryButton.addActionListener(event -> router.navigate("/rental/history"));
		maintenanceButton.addActionListener(event -> router.navigate("/maintenance"));

		constraints.gridx = 0;
		constraints.anchor = GridBagConstraints.CENTER;
		constraints.weightx = 1.0;
		constraints.weighty = 1.0;

		constraints.gridy = 1;
		add(rentalButton, constraints);
		constraints.gridy = 2;
		add(rentalHistoryButton, constraints);
		constraints.gridy = 3;
		add(maintenanceButton, constraints);

		return this;
	}
}
