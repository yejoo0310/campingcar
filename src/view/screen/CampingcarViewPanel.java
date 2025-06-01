// CampingcarViewPanel.java

package view.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entity.Campingcar;
import entity.CarExternalMaintenanceRecord;
import entity.CarSelfMaintenanceRecord;
import router.Router;
import service.MaintenanceService;
import view.Screen;

public class CampingcarViewPanel extends JPanel implements Screen {
	private final MaintenanceService maintenanceService;

	public CampingcarViewPanel(MaintenanceService maintenanceService) {
		this.maintenanceService = maintenanceService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());

		// 상단 뒤로가기
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backBtn = new JButton("뒤로가기");
		backBtn.setEnabled(router.canGoBack());
		backBtn.addActionListener(e -> router.back());
		topPanel.add(backBtn);
		add(topPanel, BorderLayout.NORTH);

		// 캠핑카 테이블
		List<Campingcar> campingcars = maintenanceService.getCampingcars();
		String[] columns = { "Id", "이름", "차량번호", "인승", "등록일" };
		Object[][] data = new Object[campingcars.size()][columns.length];

		for (int i = 0; i < campingcars.size(); i++) {
			Campingcar car = campingcars.get(i);
			data[i][0] = car.getId();
			data[i][1] = car.getName();
			data[i][2] = car.getNumber();
			data[i][3] = car.getSeatCapacity();
			data[i][4] = car.getRegisteredDate();
		}

		DefaultTableModel model = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		JTable table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// 더블클릭 시 정비기록 패널(모달)
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
					int row = table.getSelectedRow();
					Long campingcarId = (Long) table.getValueAt(row, 0);
					showMaintenanceRecordsDialog(campingcarId);
				}
			}
		});

		return this;
	}

	private void showMaintenanceRecordsDialog(Long campingcarId) {
		List<CarExternalMaintenanceRecord> externalList = maintenanceService
				.getExternalMaintenanceRecords(campingcarId);
		List<CarSelfMaintenanceRecord> selfList = maintenanceService.getSelfMaintenanceRecords(campingcarId);

		StringBuilder sb = new StringBuilder();
		sb.append("외부 정비기록\n");
		if (externalList.isEmpty()) {
			sb.append("  없음\n");
		} else {
			for (CarExternalMaintenanceRecord rec : externalList) {
				sb.append(String.format("  %s / %s / %d원 / %s\n", rec.getDate(), rec.getDescription(), rec.getCost(),
						rec.getDueDate()));
			}
		}

		sb.append("\n자체 정비기록\n");
		if (selfList.isEmpty()) {
			sb.append("  없음\n");
		} else {
			for (CarSelfMaintenanceRecord rec : selfList) {
				sb.append(String.format("  %s / %d분 / 직원ID:%d / 부품ID:%d\n", rec.getMaintenanceDate(),
						rec.getDurationMinute(), rec.getEmployeeId(), rec.getPartInventoryId()));
			}
		}

		javax.swing.JOptionPane.showMessageDialog(this, sb.toString(), "정비 기록",
				javax.swing.JOptionPane.INFORMATION_MESSAGE);
	}
}
