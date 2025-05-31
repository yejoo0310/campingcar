package view;

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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.AppSession;
import entity.Campingcar;
import service.RentalService;

public class RentalRegistrationScreen extends JFrame {
	private final RentalService rentalService;

	private final JComboBox<Campingcar> campingCarCombo = new JComboBox<>();

	private final JTextField startField = new JTextField(10);
	private final JTextField endField = new JTextField(10);

	private final JButton registerBtn = new JButton("대여등록");

	public RentalRegistrationScreen(RentalService rentalService) {
		this.rentalService = rentalService;
		init(rentalService.getCampingcars());
	}

	private void init(List<Campingcar> cars) {
		setTitle("캠핑카 대여 등록");
		setSize(500, 250);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JPanel pane = new JPanel(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(10, 10, 10, 10);
		g.fill = GridBagConstraints.HORIZONTAL;

		g.gridx = 0;
		g.gridy = 0;
		pane.add(new JLabel("캠핑카"), g);

		g.gridx = 1;
		campingCarCombo.addItem(null);
		cars.forEach(campingCarCombo::addItem);
		campingCarCombo.setRenderer((list, value, index, isSel, focus) -> {
			JLabel l = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(list, value, index, isSel,
					focus);
			l.setText(value == null ? "캠핑카를 선택하세요" : value.getDisplayName());
			return l;
		});
		pane.add(campingCarCombo, g);

		g.gridx = 0;
		g.gridy = 1;
		pane.add(new JLabel("대여일자"), g);

		g.gridx = 1;
		JPanel datePanel = new JPanel();
		startField.setEditable(false);
		endField.setEditable(false);

		datePanel.add(startField);
		datePanel.add(new JLabel(" ~ "));
		datePanel.add(endField);
		pane.add(datePanel, g);

		MouseAdapter popup = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JTextField target = (JTextField) e.getSource();
				Campingcar selCar = (Campingcar) campingCarCombo.getSelectedItem();
				if (selCar == null) {
					JOptionPane.showMessageDialog(RentalRegistrationScreen.this, "먼저 캠핑카를 선택해주세요.", "안내",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}

				Set<LocalDate> disabled = fetchUnavailableDates(selCar.getId());
				CalendarView cal = new CalendarView(RentalRegistrationScreen.this, disabled,
						date -> target.setText(date.toString()));
				cal.setVisible(true);
			}
		};
		startField.addMouseListener(popup);
		endField.addMouseListener(popup);

		g.gridx = 1;
		g.gridy = 2;
		g.anchor = GridBagConstraints.CENTER;
		registerBtn.addActionListener(e -> onRegister());
		pane.add(registerBtn, g);

		add(pane);
	}

	private Set<LocalDate> fetchUnavailableDates(Long campingcarId) {
		List<LocalDate> disableDates = rentalService.getDisableDates(campingcarId);
		return Set.copyOf(disableDates);
	}

	private void onRegister() {
		Campingcar car = (Campingcar) campingCarCombo.getSelectedItem();
		String startText = startField.getText().trim();
		String endText = endField.getText().trim();

		if (car == null || startText.isBlank() || endText.isBlank()) {
			JOptionPane.showMessageDialog(this, "캠핑카와 대여 시작·종료일을 모두 선택해주세요.", "입력 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
			LocalDate startDate = LocalDate.parse(startText);
			LocalDate endDate = LocalDate.parse(endText);

			if (endDate.isBefore(startDate)) {
				JOptionPane.showMessageDialog(this, "종료일은 시작일보다 이후여야 합니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
				return;
			}

			Set<LocalDate> disabledDates = fetchUnavailableDates(car.getId());
			for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
				if (disabledDates.contains(date)) {
					JOptionPane.showMessageDialog(this, "대여 불가능한 날짜가 포함되어 있습니다", "예약 불가", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}

			Long customerId = AppSession.getLoggedInCustomer().getId();
			rentalService.registRental(startDate, endDate, customerId, car.getId(), car.getCampingcarCompanyId());

			JOptionPane.showMessageDialog(this, "대여 등록이 완료되었습니다.");
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "날짜 형식이 잘못되었습니다. (예: 2025-06-01)", "날짜 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

}
