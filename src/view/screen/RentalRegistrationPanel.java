package view.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
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

	public RentalRegistrationPanel(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(10, 10, 10, 10);
		g.fill = GridBagConstraints.HORIZONTAL;

		JComboBox<Campingcar> carCombo = new JComboBox<>();
		List<Campingcar> cars = rentalService.getCampingcars();
		carCombo.addItem(null);
		cars.forEach(carCombo::addItem);
		carCombo.setRenderer((list, v, i, s, f) -> {
			JLabel l = (JLabel) new DefaultListCellRenderer().getListCellRendererComponent(list, v, i, s, f);
			l.setText(v == null ? "캠핑카 선택" : v.getDisplayName());
			return l;
		});

		JTextField startF = new JTextField(10);
		startF.setEditable(false);
		JTextField endF = new JTextField(10);
		endF.setEditable(false);

		JButton regBtn = new JButton("대여 등록");
		JButton backBtn = new JButton("뒤로");

		/* --- 레이아웃 --- */
		g.gridx = 0;
		g.gridy = 0;
		add(new JLabel("캠핑카"), g);
		g.gridx = 1;
		add(carCombo, g);

		g.gridx = 0;
		g.gridy = 1;
		add(new JLabel("대여일자"), g);
		g.gridx = 1;
		JPanel datePanel = new JPanel();
		datePanel.add(startF);
		datePanel.add(new JLabel(" ~ "));
		datePanel.add(endF);
		add(datePanel, g);

		g.gridy = 2;
		g.anchor = GridBagConstraints.CENTER;
		add(regBtn, g);
		g.gridy = 3;
		add(backBtn, g);

		/* --- 날짜 팝업 --- */
		MouseAdapter pop = new MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent e) {
				if (carCombo.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(RentalRegistrationPanel.this, "캠핑카 먼저 선택");
					return;
				}
				Campingcar sel = (Campingcar) carCombo.getSelectedItem();
				Set<LocalDate> disabled = Set.copyOf(rentalService.getDisableDates(sel.getId()));
				view.CalendarView cal = new view.CalendarView(
						SwingUtilities.getWindowAncestor(RentalRegistrationPanel.this), disabled,
						d -> ((JTextField) e.getSource()).setText(d.toString()));
				cal.setVisible(true);
			}
		};
		startF.addMouseListener(pop);
		endF.addMouseListener(pop);

		/* --- 등록 --- */
		regBtn.addActionListener(e -> {
			Campingcar car = (Campingcar) carCombo.getSelectedItem();
			String s = startF.getText().trim(), e2 = endF.getText().trim();
			if (car == null || s.isBlank() || e2.isBlank()) {
				JOptionPane.showMessageDialog(this, "모두 입력", "err", JOptionPane.ERROR_MESSAGE);
				return;
			}
			LocalDate st = LocalDate.parse(s), ed = LocalDate.parse(e2);
			if (ed.isBefore(st)) {
				JOptionPane.showMessageDialog(this, "종료일 확인", "err", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Set<LocalDate> dis = Set.copyOf(rentalService.getDisableDates(car.getId()));
			for (LocalDate d = st; !d.isAfter(ed); d = d.plusDays(1))
				if (dis.contains(d)) {
					JOptionPane.showMessageDialog(this, "불가 날짜 포함", "err", JOptionPane.ERROR_MESSAGE);
					return;
				}

			rentalService.registRental(st, ed, AppSession.getLoggedInCustomer().getId(), car.getId(),
					car.getCampingcarCompanyId());

			JOptionPane.showMessageDialog(this, "등록 완료");
			router.back();
		});

		backBtn.setEnabled(router.canGoBack());
		backBtn.addActionListener(e -> router.back());
		return this;
	}
}
