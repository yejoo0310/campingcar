package view.screen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import router.Router;
import service.AdminService;
import util.ApplicationException;
import view.Screen;

public class AllTablesPanel extends JPanel implements Screen {
	// service 의존성
	private final AdminService adminService;

	// 화면 구성 요소
	private JTable resultTable;
	private JScrollPane tableScrollPane;
	private JLabel emptyLabel;
	private String lastTableName = null; // 마지막으로 선택한 테이블명

	public AllTablesPanel(AdminService adminService) {
		this.adminService = adminService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new BorderLayout());

		// 상단(뒤로가기 + 테이블 버튼 + 쿼리 버튼)
		JPanel topPanel = new JPanel(new BorderLayout());

		// 뒤로가기
		JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		JButton backBtn = new JButton("뒤로가기");
		backBtn.setEnabled(router.canGoBack());
		backBtn.addActionListener(e -> router.back());
		backPanel.add(backBtn);
		topPanel.add(backPanel, BorderLayout.WEST);

		// 테이블명 버튼들
		JPanel tableButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
		List<String> tableNames = adminService.showTables();
		for (String name : tableNames) {
			JButton btn = new JButton(name);
			btn.setPreferredSize(new Dimension(140, 32));
			btn.addActionListener(e -> {
				lastTableName = name;
				showTableRows(name);
			});
			tableButtonPanel.add(btn);
		}
		tableButtonPanel.setPreferredSize(new Dimension(9999, 42)); // 패널 높이 고정
		JScrollPane buttonScroll = new JScrollPane(tableButtonPanel, JScrollPane.VERTICAL_SCROLLBAR_NEVER,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		buttonScroll.setPreferredSize(new Dimension(9999, 48));
		buttonScroll.setBorder(BorderFactory.createEmptyBorder());
		topPanel.add(buttonScroll, BorderLayout.CENTER);

		// 쿼리 실행 버튼들
		JPanel queryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
		JButton selectBtn = new JButton("조회");
		JButton updateBtn = new JButton("수정");
		JButton deleteBtn = new JButton("삭제");
		JButton insertBtn = new JButton("삽입");
		queryPanel.add(selectBtn);
		queryPanel.add(updateBtn);
		queryPanel.add(deleteBtn);
		queryPanel.add(insertBtn);

		selectBtn.addActionListener(e -> onNativeExec("select"));
		updateBtn.addActionListener(e -> onNativeExec("update"));
		deleteBtn.addActionListener(e -> onNativeExec("delete"));
		insertBtn.addActionListener(e -> onNativeExec("insert"));

		topPanel.add(queryPanel, BorderLayout.SOUTH);

		add(topPanel, BorderLayout.NORTH);

		// 결과 테이블 및 안내 문구
		resultTable = new JTable();
		resultTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		tableScrollPane = new JScrollPane(resultTable);
		tableScrollPane.setVisible(false);

		emptyLabel = new JLabel("테이블명을 선택하세요.", SwingConstants.CENTER);

		add(emptyLabel, BorderLayout.CENTER);
		add(tableScrollPane, BorderLayout.CENTER);

		return this;
	}

	private void showTableRows(String tableName) {
		try {
			// native select 사용, where 없이 전체 조회
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> list = (List<Map<String, Object>>) adminService.exec("select", tableName, "");
			if (list.isEmpty()) {
				showEmpty("데이터가 없습니다.");
				return;
			}

			Vector<String> colNames = new Vector<>(list.get(0).keySet());
			Vector<Vector<Object>> data = new Vector<>();
			for (Map<String, Object> row : list) {
				Vector<Object> rowData = new Vector<>();
				for (String k : colNames)
					rowData.add(row.get(k));
				data.add(rowData);
			}

			resultTable.setModel(new DefaultTableModel(data, colNames) {
				@Override
				public boolean isCellEditable(int r, int c) {
					return false;
				}
			});
			resultTable.doLayout();

			tableScrollPane.setVisible(true);
			remove(emptyLabel);
			add(tableScrollPane, BorderLayout.CENTER);
			revalidate();
			repaint();

		} catch (ApplicationException ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "조회 오류", JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "알 수 없는 오류: " + ex.getMessage(), "조회 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showEmpty(String msg) {
		tableScrollPane.setVisible(false);
		remove(tableScrollPane);
		emptyLabel.setText(msg);
		add(emptyLabel, BorderLayout.CENTER);
		revalidate();
		repaint();
	}

	// 네이티브 exec 호출
	private void onNativeExec(String command) {
		String table = askTableName();
		if (table == null)
			return;

		String clause = askClause(command);
		if (clause == null)
			return;

		try {
			Object res = adminService.exec(command, table, clause);

			if ("select".equals(command)) {
				@SuppressWarnings("unchecked")
				List<Map<String, Object>> list = (List<Map<String, Object>>) res;
				if (list.isEmpty()) {
					showEmpty("결과 없음");
					return;
				}

				Vector<String> cols = new Vector<>(list.get(0).keySet());
				Vector<Vector<Object>> data = new Vector<>();
				for (Map<String, Object> r : list) {
					Vector<Object> d = new Vector<>();
					for (String k : cols)
						d.add(r.get(k));
					data.add(d);
				}
				resultTable.setModel(new DefaultTableModel(data, cols) {
					@Override
					public boolean isCellEditable(int r, int c) {
						return false;
					}
				});
				tableScrollPane.setVisible(true);
				remove(emptyLabel);
				add(tableScrollPane, BorderLayout.CENTER);
				revalidate();
				repaint();
			} else {
				JOptionPane.showMessageDialog(this, "성공: " + res + "개", "알림", JOptionPane.INFORMATION_MESSAGE);
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, ex.getMessage(), "실행 오류", JOptionPane.ERROR_MESSAGE);
		}
	}

	private String askTableName() {
		List<String> names = adminService.showTables();
		return (String) JOptionPane.showInputDialog(this, "대상 테이블을 선택하세요", "테이블 선택", JOptionPane.PLAIN_MESSAGE, null,
				names.toArray(), lastTableName != null ? lastTableName : names.get(0));
	}

	private String askClause(String cmd) {
		switch (cmd) {
		case "select":
			return JOptionPane.showInputDialog(this, "clause: \"WHERE id=...", "");
		case "delete":
			return JOptionPane.showInputDialog(this, "clause: \"WHERE id=...", "");
		case "update":
			return JOptionPane.showInputDialog(this, "clause: \"col1=..., col2=... WHERE id=...\"", "");
		case "insert":
			return JOptionPane.showInputDialog(this, "clause: \"(col1,col2) VALUES (...,...)", "");
		default:
			return null;
		}
	}
}
