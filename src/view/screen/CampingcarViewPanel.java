package view.screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import entity.Campingcar;
import entity.CarExternalMaintenanceCenter;
import entity.CarExternalMaintenanceRecord;
import entity.CarSelfMaintenanceRecord;
import entity.PartInventory;
import router.Router;
import service.MaintenanceService;
import view.Screen;

public class CampingcarViewPanel extends JPanel implements Screen {
	private final MaintenanceService maintenanceService;

	private JList<Campingcar> campingcarList;
	private DefaultListModel<Campingcar> campingcarListModel;
	private JTable externalTable;
	private JTable selfTable;

	private List<CarExternalMaintenanceRecord> currentExternalRecords = List.of();
	private List<CarSelfMaintenanceRecord> currentSelfRecords = List.of();

	public CampingcarViewPanel(MaintenanceService maintenanceService) {
		this.maintenanceService = maintenanceService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());

		// 상단 뒤로가기 버튼
		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		JButton backBtn = new JButton("뒤로가기");
		backBtn.setEnabled(router.canGoBack());
		backBtn.addActionListener(e -> router.back());
		topPanel.add(backBtn);
		add(topPanel, BorderLayout.NORTH);

		// 좌측 캠핑카 리스트
		campingcarListModel = new DefaultListModel<>();
		List<Campingcar> campingcars = maintenanceService.getCampingcars();
		for (Campingcar car : campingcars) {
			campingcarListModel.addElement(car);
		}
		campingcarList = new JList<>(campingcarListModel);
		campingcarList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		campingcarList.setFixedCellWidth(180);
		campingcarList.setPreferredSize(new Dimension(200, 400));
		campingcarList.setCellRenderer((list, value, index, isSelected, cellHasFocus) -> {
			JLabel label = new JLabel(value.getDisplayName());
			label.setOpaque(true);
			if (isSelected)
				label.setBackground(list.getSelectionBackground());
			if (isSelected)
				label.setForeground(list.getSelectionForeground());
			return label;
		});

		JScrollPane leftScroll = new JScrollPane(campingcarList);

		// 우측 상: 외부정비 기록, 하: 자체정비 기록
		externalTable = new JTable();
		JScrollPane externalScroll = new JScrollPane(externalTable);
		externalScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("외부 정비기록"));

		selfTable = new JTable();
		JScrollPane selfScroll = new JScrollPane(selfTable);
		selfScroll.setBorder(javax.swing.BorderFactory.createTitledBorder("자체 정비기록"));

		// 우측 수직 분할
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, externalScroll, selfScroll);
		splitPane.setResizeWeight(0.5);
		splitPane.setDividerLocation(180);

		// 전체 패널 수평 분할
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(leftScroll, BorderLayout.WEST);
		centerPanel.add(splitPane, BorderLayout.CENTER);
		add(centerPanel, BorderLayout.CENTER);

		// 캠핑카 더블클릭 시 기록 출력
		campingcarList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					Campingcar selected = campingcarList.getSelectedValue();
					if (selected != null) {
						showExternalMaintenance(selected.getId());
						showSelfMaintenance(selected.getId());
					}
				}
			}
		});

		// 외부 정비 테이블 더블 클릭: 정비센터 정보 모달
		externalTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && externalTable.getSelectedRow() != -1) {
					int idx = externalTable.getSelectedRow();
					if (idx >= 0 && idx < currentExternalRecords.size()) {
						CarExternalMaintenanceRecord record = currentExternalRecords.get(idx);
						try {
							CarExternalMaintenanceCenter center = maintenanceService
									.getExternalCenterById(record.getCarExternalMaintenanceCenterId());
							String msg = String.format("정비센터명: %s\n주소: %s\n연락처: %s\n담당자: %s (%s)",
									center.getCenterName(), center.getCenterAddress(), center.getCenterPhone(),
									center.getManagerName(), center.getManagerEmail());
							JOptionPane.showMessageDialog(CampingcarViewPanel.this, msg, "정비센터 정보",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(CampingcarViewPanel.this, ex.getMessage(), "오류",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		// 자체 정비 테이블 더블 클릭: 부품 정보 모달
		selfTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && selfTable.getSelectedRow() != -1) {
					int idx = selfTable.getSelectedRow();
					if (idx >= 0 && idx < currentSelfRecords.size()) {
						CarSelfMaintenanceRecord record = currentSelfRecords.get(idx);
						Long partId = record.getPartInventoryId();
						try {
							PartInventory part = maintenanceService.getPartInventoryById(partId);
							String msg = String.format("부품명: %s\n단가: %d원\n수량: %d개\n입고일: %s\n공급자: %s", part.getName(),
									part.getPrice(), part.getQuantity(), part.getStockedDate(), part.getProviderName());
							JOptionPane.showMessageDialog(CampingcarViewPanel.this, msg, "부품 정보",
									JOptionPane.INFORMATION_MESSAGE);
						} catch (Exception ex) {
							JOptionPane.showMessageDialog(CampingcarViewPanel.this, ex.getMessage(), "오류",
									JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		// 초기 데이터(첫 번째 캠핑카) 자동 표시
		if (!campingcars.isEmpty()) {
			campingcarList.setSelectedIndex(0);
			Campingcar selected = campingcars.get(0);
			showExternalMaintenance(selected.getId());
			showSelfMaintenance(selected.getId());
		}

		return this;
	}

	private void showExternalMaintenance(Long campingcarId) {
		currentExternalRecords = maintenanceService.getExternalMaintenanceRecords(campingcarId);

		Vector<String> columns = new Vector<>();
		columns.add("일자");
		columns.add("설명");
		columns.add("비용");
		columns.add("센터ID");
		columns.add("고객ID");

		Vector<Vector<Object>> data = new Vector<>();
		for (CarExternalMaintenanceRecord rec : currentExternalRecords) {
			Vector<Object> row = new Vector<>();
			row.add(rec.getDate());
			row.add(rec.getDescription());
			row.add(rec.getCost());
			row.add(rec.getCarExternalMaintenanceCenterId());
			row.add(rec.getCustomerId());
			data.add(row);
		}

		externalTable.setModel(new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		});
	}

	private void showSelfMaintenance(Long campingcarId) {
		currentSelfRecords = maintenanceService.getSelfMaintenanceRecords(campingcarId);

		Vector<String> columns = new Vector<>();
		columns.add("일자");
		columns.add("소요시간(분)");
		columns.add("직원ID");
		columns.add("부품ID");

		Vector<Vector<Object>> data = new Vector<>();
		for (CarSelfMaintenanceRecord rec : currentSelfRecords) {
			Vector<Object> row = new Vector<>();
			row.add(rec.getMaintenanceDate());
			row.add(rec.getDurationMinute());
			row.add(rec.getEmployeeId());
			row.add(rec.getPartInventoryId());
			data.add(row);
		}

		selfTable.setModel(new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		});
	}
}
