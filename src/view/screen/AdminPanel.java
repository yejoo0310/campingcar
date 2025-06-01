package view.screen;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import router.Router;
import service.InitService;
import view.Screen;

public class AdminPanel extends JPanel implements Screen {
	private final InitService initService;

	public AdminPanel(InitService initService) {
		this.initService = initService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(20, 20, 20, 20);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 뒤로가기 버
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(e -> router.back());
		topPanel.add(backButton);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		add(topPanel, gbc);

		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.gridx = 0;
		gbc.gridy = 1;
		JButton initButton = new JButton("DB 초기화");
		add(initButton, gbc);

		gbc.gridy = 2;
		JButton viewAllTablesButton = new JButton("전체 테이블 조회");
		viewAllTablesButton.addActionListener(e -> router.navigate("/all-tables"));
		add(viewAllTablesButton, gbc);

		gbc.gridy = 3;
		JButton viewCampingcarsButton = new JButton("캠핑카 조회");
		viewCampingcarsButton.addActionListener(e -> router.navigate("/campingcar-view"));
		add(viewCampingcarsButton, gbc);

		// 이벤트
		initButton.addActionListener(e -> onInitDB());

		return this;
	}

	private void onInitDB() {
		int result = JOptionPane.showConfirmDialog(this, "정말로 DB를 초기화하시겠습니까?\n모든 데이터가 삭제됩니다.", "DB 초기화 확인",
				JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
		if (result == JOptionPane.YES_OPTION) {
			try {
				initService.init();
				JOptionPane.showMessageDialog(this, "DB가 성공적으로 초기화되었습니다.", "완료", JOptionPane.INFORMATION_MESSAGE);
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(this, "DB 초기화 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

}
