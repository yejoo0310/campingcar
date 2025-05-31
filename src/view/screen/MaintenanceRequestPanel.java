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
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import entity.CarExternalMaintenanceCenter;
import router.Router;
import service.MaintenanceService;
import service.RentalService;
import view.Screen;
import view.dialog.MaintenanceRequestDialog;

public class MaintenanceRequestPanel extends JPanel implements Screen {
	private final MaintenanceService maintenanceService;
	private final RentalService rentalService;

	public MaintenanceRequestPanel(MaintenanceService maintenanceService, RentalService rentalService) {
		this.maintenanceService = maintenanceService;
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());

		// 좌측 상단 뒤로가기 버튼
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(e -> router.back());
		northPanel.add(backButton);
		add(northPanel, BorderLayout.NORTH);

		// 정비센터 목록
		List<CarExternalMaintenanceCenter> centerList = maintenanceService.getExternalCenters();
		String[] columns = { "정비센터명", "주소", "연락처" };
		Object[][] data = new Object[centerList.size()][3];
		for (int i = 0; i < centerList.size(); i++) {
			CarExternalMaintenanceCenter c = centerList.get(i);
			data[i][0] = c.getCenterName();
			data[i][1] = c.getCenterAddress();
			data[i][2] = c.getCenterPhone();
		}
		DefaultTableModel model = new DefaultTableModel(data, columns) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};
		JTable table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		// 더블클릭 이벤트
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int row = table.rowAtPoint(e.getPoint());
				if (row == -1)
					return;
				if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
					CarExternalMaintenanceCenter center = centerList.get(row);
					MaintenanceRequestDialog dialog = new MaintenanceRequestDialog(
							SwingUtilities.getWindowAncestor(MaintenanceRequestPanel.this), rentalService, center,
							maintenanceService);
					dialog.setVisible(true);
				}
			}
		});

		return this;
	}
}
