package view.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import context.AppSession;
import entity.Customer;
import router.Router;
import service.AuthService;
import service.RentalService;
import util.ApplicationException;
import view.Screen;

public class LoginPanel extends JPanel implements Screen {
	private final AuthService authService;
	private final RentalService rentalService;

	public LoginPanel(AuthService authService, RentalService rentalService) {
		this.authService = authService;
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);

		// 아이디와 비밀번호 입력 필드
		final JTextField idField = new JTextField(15);
		final JPasswordField passwordField = new JPasswordField(15);

		// 관리자 체크박스
		final JCheckBox adminCheckBox = new JCheckBox("관리자");
		// 로그인 버튼
		final JButton loginButton = new JButton("로그인");

		// 개발자용 기본값 설정: 일반 회원 세팅
		idField.setText("alice01");
		passwordField.setText("pwAlice01");

		// 관리자 체크박스 리스너
		adminCheckBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent event) {
				boolean adminSelected = (event.getStateChange() == ItemEvent.SELECTED);
				if (adminSelected) {
					idField.setText("root");
					passwordField.setText("1234");
				} else {
					idField.setText("alice01");
					passwordField.setText("pwAlice01");
				}
			}
		});

		// 레이아웃 배치
		constraints.gridx = 0;
		constraints.gridy = 0;
		add(new JLabel("아이디"), constraints);

		constraints.gridx = 1;
		add(idField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		add(new JLabel("비밀번호"), constraints);

		constraints.gridx = 1;
		add(passwordField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.gridwidth = 2;
		add(adminCheckBox, constraints);

		constraints.gridy = 3;
		add(loginButton, constraints);

		// 로그인 버튼 동작
		loginButton.addActionListener(event -> {
			String userId = idField.getText().trim();
			String userPassword = new String(passwordField.getPassword()).trim();

			if (userId.isEmpty() || userPassword.isEmpty()) {
				JOptionPane.showMessageDialog(this, "아이디와 비밀번호를 모두 입력하세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			boolean isAdmin = adminCheckBox.isSelected();
			try {
				boolean loginSuccess;
				if (isAdmin) {
					loginSuccess = authService.loginAdmin(userId, userPassword);
				} else {
					loginSuccess = authService.loginUser(userId, userPassword) != null;
				}

				if (!loginSuccess) {
					throw ApplicationException.of("로그인 정보가 일치하지 않습니다.");
				}

				Customer customer = isAdmin ? null : authService.loginUser(userId, userPassword);
				AppSession.setLoggedInCustomer(customer);
				AppSession.setIsAdmin(isAdmin);

				router.navigate(isAdmin ? "/admin" : "/user");
			} catch (Exception exception) {
				JOptionPane.showMessageDialog(this, "로그인 실패: " + exception.getMessage(), "로그인 오류",
						JOptionPane.ERROR_MESSAGE);
			}
		});

		return this;
	}
}
