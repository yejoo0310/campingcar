package view.dialog;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import context.AppSession;
import dto.CampingcarRentalHistory;
import entity.Campingcar;
import entity.CarExternalMaintenanceCenter;
import entity.CarExternalMaintenanceRecord;
import service.MaintenanceService;
import service.RentalService;

public class MaintenanceRequestDialog extends JDialog {
	private final RentalService rentalService;
	private final CarExternalMaintenanceCenter center;
	private final MaintenanceService maintenanceService;

	private JComboBox<CampingcarRentalHistory> carCombo;
	private JTextField dateField;

	public MaintenanceRequestDialog(Window owner, RentalService rentalService, CarExternalMaintenanceCenter center,
			MaintenanceService maintenanceService) {
		super(owner, "정비 요청", ModalityType.APPLICATION_MODAL);
		this.rentalService = rentalService;
		this.center = center;
		this.maintenanceService = maintenanceService;
		setLayout(new GridBagLayout());
		setSize(420, 220);
		setLocationRelativeTo(owner);

		initComponents();
	}

	private void initComponents() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);
		c.fill = GridBagConstraints.HORIZONTAL;

		// 정비센터명
		JLabel centerLabel = new JLabel("정비센터명");
		JLabel centerNameLabel = new JLabel(center.getCenterName());
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		add(centerLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(centerNameLabel, c);

		// 차량 콤보박스 (CampingcarRentalHistory 직접 넣기)
		JLabel carLabel = new JLabel("차량 선택");
		carCombo = new JComboBox<>();
		List<CampingcarRentalHistory> histories = rentalService
				.getRentalHistoryWithDetail(AppSession.getLoggedInCustomer().getId());
		for (CampingcarRentalHistory history : histories) {
			carCombo.addItem(history);
		}
		carCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel lbl = new JLabel();
			if (value == null) {
				lbl.setText("차량 선택");
			} else {
				Campingcar car = value.getCampingcar();
				lbl.setText(car.getNumber() + " (" + car.getName() + ")");
			}
			return lbl;
		});
		c.gridx = 0;
		c.gridy = 1;
		c.anchor = GridBagConstraints.LINE_END;
		add(carLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(carCombo, c);

		// 정비 요청일(캘린더)
		JLabel dateLabel = new JLabel("정비 요청일");
		dateField = new JTextField(10);
		dateField.setEditable(false);
		c.gridx = 0;
		c.gridy = 2;
		c.anchor = GridBagConstraints.LINE_END;
		add(dateLabel, c);
		c.gridx = 1;
		c.anchor = GridBagConstraints.LINE_START;
		add(dateField, c);

		dateField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				view.CalendarView cal = new view.CalendarView(MaintenanceRequestDialog.this, java.util.Set.of(),
						d -> dateField.setText(d.toString()));
				cal.setVisible(true);
			}
		});

		// 버튼
		JPanel btnPanel = new JPanel();
		JButton okBtn = new JButton("정비 요청");
		JButton cancelBtn = new JButton("취소");
		btnPanel.add(okBtn);
		btnPanel.add(cancelBtn);

		okBtn.addActionListener(e -> onSubmit());
		cancelBtn.addActionListener(e -> dispose());

		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 2;
		c.anchor = GridBagConstraints.CENTER;
		add(btnPanel, c);
	}

	private void onSubmit() {
		CampingcarRentalHistory selectedHistory = (CampingcarRentalHistory) carCombo.getSelectedItem();
		String date = dateField.getText().trim();
		if (selectedHistory == null || date.isBlank()) {
			JOptionPane.showMessageDialog(this, "차량과 정비 요청일을 모두 선택하세요.", "입력오류", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Campingcar car = selectedHistory.getCampingcar();
		Long campingcarId = car.getId();
		Long campingcarCompanyId = car.getCampingcarCompanyId();
		Long customerId = AppSession.getLoggedInCustomer().getId();
		LocalDate selectDate = LocalDate.parse(date);

		CarExternalMaintenanceRecord record = new CarExternalMaintenanceRecord(null, "",
				java.sql.Date.valueOf(selectDate), 100000, java.sql.Date.valueOf(selectDate), "", campingcarId,
				customerId, center.getId(), campingcarCompanyId);

		maintenanceService.addRecord(record);

		JOptionPane.showMessageDialog(this,
				String.format("정비 요청 완료\n차량번호: %s\n요청일: %s\n정비센터: %s", car.getNumber(), date, center.getCenterName()));
		dispose();
	}
}
