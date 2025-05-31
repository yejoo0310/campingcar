package view.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import dto.CampingcarRentalHistory;
import entity.Campingcar;
import service.RentalService;

public class RentalHistoryEditDialog extends JDialog {
	private final RentalService rentalService;
	private final CampingcarRentalHistory originalHistory;
	private final Runnable onSuccess; // 새로고침 콜백

	private JComboBox<Campingcar> campingcarComboBox;
	private JTextField startDateField;
	private JTextField endDateField;

	public RentalHistoryEditDialog(Window owner, RentalService rentalService, CampingcarRentalHistory history,
			Runnable onSuccess) {
		super(owner, "대여 정보 수정", ModalityType.APPLICATION_MODAL);
		this.rentalService = rentalService;
		this.originalHistory = history;
		this.onSuccess = onSuccess;

		setLayout(new GridBagLayout());
		setSize(420, 260);
		setLocationRelativeTo(owner);

		initComponents();
	}

	private void initComponents() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;

		// 캠핑카 콤보박스
		JLabel campingcarLabel = new JLabel("캠핑카");
		campingcarComboBox = new JComboBox<>();
		List<Campingcar> campingcarList = rentalService.getCampingcars();
		for (Campingcar car : campingcarList) {
			campingcarComboBox.addItem(car);
		}
		// 초기 선택
		campingcarComboBox.setSelectedItem(originalHistory.getCampingcar());

		campingcarComboBox.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel label = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(list, value, index,
					isSelected, cellHasFocus);
			label.setText(value == null ? "캠핑카 선택" : value.getDisplayName());
			return label;
		});

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(campingcarLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(campingcarComboBox, c);

		// 날짜 패널
		JLabel dateLabel = new JLabel("대여일자");
		startDateField = new JTextField(10);
		endDateField = new JTextField(10);
		startDateField.setEditable(false);
		endDateField.setEditable(false);

		// 기존 값 세팅
		LocalDate start = originalHistory.getRental().getStartDate().toLocalDate();
		int days = originalHistory.getRental().getPeriodDays();
		LocalDate end = start.plusDays(days - 1);
		startDateField.setText(start.toString());
		endDateField.setText(end.toString());

		JPanel datePanel = new JPanel();
		datePanel.add(startDateField);
		datePanel.add(new JLabel(" ~ "));
		datePanel.add(endDateField);

		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(dateLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(datePanel, c);

		// 날짜 선택 리스너
		MouseAdapter calendarListener = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				Campingcar selectedCar = (Campingcar) campingcarComboBox.getSelectedItem();
				if (selectedCar == null) {
					JOptionPane.showMessageDialog(RentalHistoryEditDialog.this, "캠핑카를 먼저 선택하세요.", "안내",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				LocalDate originalStartDate = originalHistory.getRental().getStartDate().toLocalDate();
				LocalDate originalEndDate = originalStartDate.plusDays(originalHistory.getRental().getPeriodDays() - 1);

				// 전체 비활성 날짜 Set 복사
				Set<LocalDate> allDisabledDates = new HashSet<>(rentalService.getDisableDates(selectedCar.getId()));

				// 기존 예약 날짜를 제거
				for (LocalDate date = originalStartDate; !date.isAfter(originalEndDate); date = date.plusDays(1)) {
					allDisabledDates.remove(date);
				}

				// 제거된 Set을 캘린더에 전달
				view.CalendarView calendarDialog = new view.CalendarView(RentalHistoryEditDialog.this, allDisabledDates,
						selectedDate -> ((JTextField) event.getSource()).setText(selectedDate.toString()));
				calendarDialog.setVisible(true);
			}
		};
		startDateField.addMouseListener(calendarListener);
		endDateField.addMouseListener(calendarListener);

		// 저장/취소 버튼
		JPanel btnPanel = new JPanel();
		JButton saveButton = new JButton("저장");
		JButton cancelButton = new JButton("취소");

		saveButton.addActionListener(e -> onSave());
		cancelButton.addActionListener(e -> dispose());
		btnPanel.add(saveButton);
		btnPanel.add(cancelButton);

		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		add(btnPanel, c);
	}

	private void onSave() {
		Campingcar selectedCar = (Campingcar) campingcarComboBox.getSelectedItem();
		String startText = startDateField.getText().trim();
		String endText = endDateField.getText().trim();

		// 입력값 유효성
		String msg = validate(selectedCar, startText, endText);
		if (msg != null) {
			JOptionPane.showMessageDialog(this, msg, "입력 오류", JOptionPane.ERROR_MESSAGE);
			return;
		}

		LocalDate startDate = LocalDate.parse(startText);
		LocalDate endDate = LocalDate.parse(endText);
		LocalDate originalStartDate = originalHistory.getRental().getStartDate().toLocalDate();
		LocalDate originalEndDate = originalStartDate.plusDays(originalHistory.getRental().getPeriodDays() - 1);

		// 전체 비활성 날짜 Set 복사
		Set<LocalDate> unavailableDates = new HashSet<>(rentalService.getDisableDates(selectedCar.getId()));

		// 기존 예약 날짜를 제거
		for (LocalDate date = originalStartDate; !date.isAfter(originalEndDate); date = date.plusDays(1)) {
			unavailableDates.remove(date);
		}
		for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
			if (!date.isBefore(originalStartDate) && !date.isAfter(originalEndDate)) {
				continue;
			}

			if (unavailableDates.contains(date)
					&& !(date.equals(originalHistory.getRental().getStartDate().toLocalDate())
							&& selectedCar.getId().equals(originalHistory.getCampingcar().getId()))) {
				JOptionPane.showMessageDialog(this, "대여 불가능한 날짜가 포함되어 있습니다.", "예약 불가", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		try {
			rentalService.updateHistory(originalHistory.getRental(), selectedCar.getId(),
					selectedCar.getCampingcarCompanyId(), startDate, endDate);
			JOptionPane.showMessageDialog(this, "대여 정보가 수정되었습니다.");
			if (onSuccess != null)
				onSuccess.run();
			dispose();
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "수정 실패: " + ex.getMessage(), "오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String validate(Campingcar selectedCar, String startText, String endText) {
		if (selectedCar == null || startText.isBlank() || endText.isBlank()) {
			return "캠핑카와 대여 시작·종료일을 모두 선택해주세요.";
		}
		LocalDate startDate, endDate;
		try {
			startDate = LocalDate.parse(startText);
			endDate = LocalDate.parse(endText);
		} catch (Exception ex) {
			return "날짜 형식이 잘못되었습니다. (예: 2025-06-01)";
		}
		if (endDate.isBefore(startDate)) {
			return "종료일은 시작일보다 이후여야 합니다.";
		}
		return null;
	}
}
