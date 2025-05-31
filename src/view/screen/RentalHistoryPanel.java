package view.screen;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import context.AppSession;
import dto.CampingcarRentalHistory;
import entity.Campingcar;
import entity.CampingcarRental;
import router.Router;
import service.RentalService;
import view.Screen;
import view.dialog.RentalHistoryEditDialog;

public class RentalHistoryPanel extends JPanel implements Screen {
	private final RentalService rentalService;
	private Router router; // router 인스턴스를 필드로 저장

	public RentalHistoryPanel(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		this.router = router; // render에서 router를 필드에 저장
		removeAll(); // 여러번 render될 경우를 위해 안전하게 클리어

		setLayout(new BorderLayout());

		// 좌측 상단 뒤로가기 버튼
		JPanel northPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JButton backButton = new JButton("뒤로가기");
		backButton.setEnabled(router.canGoBack());
		backButton.addActionListener(event -> router.back());
		northPanel.add(backButton);
		add(northPanel, BorderLayout.NORTH);

		// 대여기록 테이블 생성
		JTable rentalTable = createRentalHistoryTable();
		JScrollPane tableScrollPane = new JScrollPane(rentalTable);

		add(tableScrollPane, BorderLayout.CENTER);

		revalidate();
		repaint();

		return this;
	}

	private JTable createRentalHistoryTable() {
		String[] columnNames = { "캠핑카 이름", "차량번호", "대여기간" };

		Long customerId = AppSession.getLoggedInCustomer().getId();
		List<CampingcarRentalHistory> rentalHistories = rentalService.getRentalHistoryWithDetail(customerId);

		Object[][] data = new Object[rentalHistories.size()][3];

		for (int i = 0; i < rentalHistories.size(); i++) {
			CampingcarRentalHistory history = rentalHistories.get(i);
			Campingcar campingcar = history.getCampingcar();
			CampingcarRental rental = history.getRental();

			String name = campingcar.getName();
			String number = campingcar.getNumber();
			LocalDate startDate = rental.getStartDate().toLocalDate();
			LocalDate endDate = startDate.plusDays(rental.getPeriodDays() - 1);
			String period = String.format("%s ~ %s", startDate, endDate);

			data[i][0] = name;
			data[i][1] = number;
			data[i][2] = period;
		}

		DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; // 수정불가
			}
		};

		JTable table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// 더블클릭: 캠핑카 상세
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				int row = table.rowAtPoint(event.getPoint());
				if (row == -1)
					return;

				if (event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event)) {
					CampingcarRentalHistory selectedHistory = rentalHistories.get(row);
					showCampingcarInfoDialog(selectedHistory.getCampingcar());
				}
				if (SwingUtilities.isRightMouseButton(event) && event.getClickCount() == 1) {
					table.setRowSelectionInterval(row, row); //
					showPopupMenu(table, event.getX(), event.getY(), rentalHistories.get(row));
				}
			}
		});

		return table;
	}

	private void showCampingcarInfoDialog(Campingcar campingcar) {
		StringBuilder info = new StringBuilder();
		info.append("캠핑카 이름: ").append(campingcar.getName()).append("\n").append("차량번호: ")
				.append(campingcar.getNumber()).append("\n").append("인승: ").append(campingcar.getSeatCapacity())
				.append("인승\n").append("등록일: ").append(campingcar.getRegisteredDate()).append("\n").append("상세정보: ")
				.append(campingcar.getInformation());

		JOptionPane.showMessageDialog(this, info.toString(), "캠핑카 상세 정보", JOptionPane.INFORMATION_MESSAGE);
	}

	private void showPopupMenu(JTable table, int x, int y, CampingcarRentalHistory history) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem deleteItem = new JMenuItem("삭제하기");
		JMenuItem updateItem = new JMenuItem("변경하기");

		deleteItem.addActionListener(event -> onDeleteRental(history));
		updateItem.addActionListener(event -> onUpdateRental(history));

		popupMenu.add(deleteItem);
		popupMenu.add(updateItem);

		popupMenu.show(table, x, y);
	}

	private void onDeleteRental(CampingcarRentalHistory history) {
		try {
			rentalService.deleteHistory(history.getRental().getId());
			JOptionPane.showMessageDialog(this, "해당 대여 정보를 삭제하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			refreshPanel();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "해당 대여 정보 삭제를 실패하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void onUpdateRental(CampingcarRentalHistory history) {
		RentalHistoryEditDialog dialog = new RentalHistoryEditDialog(SwingUtilities.getWindowAncestor(this),
				rentalService, history, this::refreshPanel // 수정 성공 시 새로고침
		);
		dialog.setVisible(true);
	}

	private void refreshPanel() {
		if (router != null) {
			router.navigate("/rental/history");
		}
	}
}
