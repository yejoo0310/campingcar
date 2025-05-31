package view.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import context.AppSession;
import entity.Campingcar;
import router.Router;
import service.RentalService;
import view.Screen;

public class RentalRegistrationPanel extends JPanel implements Screen {
	private final RentalService rentalService;

	// UI 컴포넌트 인스턴스 필드로 선언
	private JComboBox<Campingcar> campingcarComboBox;
	private JTextField startDateField;
	private JTextField endDateField;

	public RentalRegistrationPanel(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10, 10, 10, 10);
		constraints.fill = GridBagConstraints.HORIZONTAL;

		// 좌상단 뒤로가기 버튼
		JButton backButton = createBackButton(router);
		constraints.gridx = 0;
		constraints.gridy = 0;
		constraints.anchor = GridBagConstraints.NORTHWEST;
		add(backButton, constraints);

		// 캠핑카 선택 콤보박스
		JLabel campingcarLabel = new JLabel("캠핑카");
		campingcarComboBox = createCampingcarComboBox();
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(campingcarLabel, constraints);

		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(campingcarComboBox, constraints);

		// 대여일자 입력 패널
		JLabel dateLabel = new JLabel("대여일자");
		JPanel datePanel = createDatePanel();
		constraints.gridx = 0;
		constraints.gridy = 2;
		constraints.anchor = GridBagConstraints.LINE_END;
		add(dateLabel, constraints);

		constraints.gridx = 1;
		constraints.anchor = GridBagConstraints.LINE_START;
		add(datePanel, constraints);

		// 대여 등록 버튼
		JButton registerButton = new JButton("대여 등록");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		add(registerButton, constraints);

		// 이벤트 연결
		addCalendarPopupListener();
		registerButton.addActionListener(e -> onRegister(router));

		return this;
	}

	private JButton createBackButton(Router router) {
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(event -> router.back());
		return backButton;
	}

	private JComboBox<Campingcar> createCampingcarComboBox() {
		JComboBox<Campingcar> comboBox = new JComboBox<>();
		List<Campingcar> campingcarList = rentalService.getCampingcars();
		comboBox.addItem(null);
		for (Campingcar campingcar : campingcarList) {
			comboBox.addItem(campingcar);
		}
		comboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel label = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			label.setText(value == null ? "캠핑카 선택" : value.getDisplayName());
			return label;
		});
		return comboBox;
	}

	private JPanel createDatePanel() {
		startDateField = new JTextField(10);
		startDateField.setEditable(false);
		endDateField = new JTextField(10);
		endDateField.setEditable(false);

		JPanel datePanel = new JPanel();
		datePanel.add(startDateField);
		datePanel.add(new JLabel(" ~ "));
		datePanel.add(endDateField);
		return datePanel;
	}

	private void addCalendarPopupListener() {
		MouseAdapter calendarPopupMouseListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (campingcarComboBox.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(RentalRegistrationPanel.this, "캠핑카를 먼저 선택하세요.", "안내",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Campingcar selectedCampingcar = (Campingcar) campingcarComboBox.getSelectedItem();
				Set<LocalDate> disabledDates = Set.copyOf(rentalService.getDisableDates(selectedCampingcar.getId()));
				view.CalendarView calendarDialog = new view.CalendarView(
						SwingUtilities.getWindowAncestor(RentalRegistrationPanel.this), disabledDates,
						selectedDate -> ((JTextField) event.getSource()).setText(selectedDate.toString()));
				calendarDialog.setVisible(true);
			}
		};
		startDateField.addMouseListener(calendarPopupMouseListener);
		endDateField.addMouseListener(calendarPopupMouseListener);
	}

	private void onRegister(Router router) {
		Campingcar selectedCampingcar = (Campingcar) campingcarComboBox.getSelectedItem();
		String startDateText = startDateField.getText().trim();
		String endDateText = endDateField.getText().trim();

		String validationError = validateForm(selectedCampingcar, startDateText, endDateText);
		if (validationError != null) {
			JOptionPane.showMessageDialog(this, validationError, "입력 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}

		LocalDate startDate = LocalDate.parse(startDateText);
		LocalDate endDate = LocalDate.parse(endDateText);

		Set<LocalDate> unavailableDates = Set.copyOf(rentalService.getDisableDates(selectedCampingcar.getId()));
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			if (unavailableDates.contains(date)) {
				JOptionPane.showMessageDialog(this, "대여 불가능한 날짜가 포함되어 있습니다.", "예약 불가", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		rentalService.registRental(startDate, endDate, AppSession.getLoggedInCustomer().getId(),
				selectedCampingcar.getId(), selectedCampingcar.getCampingcarCompanyId());

		JOptionPane.showMessageDialog(this, "대여 등록이 완료되었습니다.");
		router.back();
	}

	private String validateForm(Campingcar selectedCampingcar, String startDateText, String endDateText) {
		if (selectedCampingcar == null || startDateText.isBlank() || endDateText.isBlank()) {
			return "캠핑카와 대여 시작·종료일을 모두 선택해주세요.";
		}
		LocalDate startDate;
		LocalDate endDate;
		try {
			startDate = LocalDate.parse(startDateText);
			endDate = LocalDate.parse(endDateText);
		} catch (Exception ex) {
			return "날짜 형식이 잘못되었습니다. (예: 2025-06-01)";
		}
		if (endDate.isBefore(startDate)) {
			return "종료일은 시작일보다 이후여야 합니다.";
		}
		return null;
	}
}
