package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import context.AppSession;
import entity.Customer;
import service.AuthService;
import service.RentalService;
import util.ApplicationException;

public class LoginScreen extends JFrame {
	private final AuthService authService;
	private final RentalService rentalService;
	private final JCheckBox adminCheckBox = new JCheckBox("관리자");
	private final JCheckBox userCheckBox = new JCheckBox("일반 회원");

	private final JTextField idField = new JTextField(15);
	private final JPasswordField passwordField = new JPasswordField(15);

	public LoginScreen(AuthService authService, RentalService rentalService) {
		this.authService = authService;
		this.rentalService = rentalService;

		init();
	}

	private void init() {
		setTitle("로그인");
		setSize(400, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		// 상단 체크박스 그룹
		ButtonGroup roleGroup = new ButtonGroup(); // JCheckBox는 group을 무시하므로 수동 처리
		userCheckBox.setSelected(true);
		adminCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				userCheckBox.setSelected(false);
				idField.setText("root");
				passwordField.setText("1234");
			} else if (!userCheckBox.isSelected()) {
				userCheckBox.setSelected(true);
			}
		});

		userCheckBox.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				adminCheckBox.setSelected(false);
				idField.setText("");
				passwordField.setText("");
			} else if (!adminCheckBox.isSelected()) {
				adminCheckBox.setSelected(true);
			}
		});

		JPanel rolePanel = new JPanel();
		rolePanel.add(adminCheckBox);
		rolePanel.add(userCheckBox);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10, 0, 10, 0);
		panel.add(rolePanel, gbc);

		// 아이디
		gbc.gridwidth = 1;
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.anchor = GridBagConstraints.LINE_END;
		panel.add(new JLabel("아이디 "), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		panel.add(idField, gbc);

		// 비밀번호
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.anchor = GridBagConstraints.LINE_END;
		panel.add(new JLabel("비밀번호 "), gbc);

		gbc.gridx = 1;
		gbc.anchor = GridBagConstraints.LINE_START;
		panel.add(passwordField, gbc);

		// 로그인 버튼
		JButton loginButton = new JButton("로그인");
		loginButton.addActionListener(e -> onLogin());

		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(15, 0, 0, 0);
		panel.add(loginButton, gbc);

		add(panel);
	}

	private void onLogin() {
		String id = idField.getText().trim();
		String pw = new String(passwordField.getPassword()).trim();

		if (id.isEmpty() || pw.isEmpty()) {
			JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}

		boolean isAdmin = adminCheckBox.isSelected();

		try {
			boolean loginSuccess = isAdmin ? authService.loginAdmin(id, pw) : authService.loginUser(id, pw) != null;
			Customer customer = isAdmin ? null : authService.loginUser(id, pw); // 공통 고객 정보 조회

			if (loginSuccess) {
				// 세션에 사용자 정보 저장
				AppSession.setLoggedInCustomer(customer);
				AppSession.setIsAdmin(isAdmin);

				JOptionPane.showMessageDialog(this, "로그인 성공");

				SwingUtilities.invokeLater(() -> {
					if (isAdmin) {
						AdminScreen adminScreen = new AdminScreen();
						adminScreen.setVisible(true);
					} else {
						RentalRegistrationScreen rentalScreen = new RentalRegistrationScreen(rentalService);
						rentalScreen.setVisible(true);
					}
				});

				this.dispose(); // 로그인 화면 닫기
				return;
			}

			throw ApplicationException.of("로그인 실패: 아이디 또는 비밀번호 오류");

		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "로그인 실패: 아이디 또는 비밀번호 오류", "오류", JOptionPane.ERROR_MESSAGE);
		}

	}
}
