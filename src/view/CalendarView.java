package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CalendarView extends JDialog { // 모달 다이얼로그로 변경
	private final JPanel calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
	private final JLabel monthLabel = new JLabel("", SwingConstants.CENTER);
	private final JButton prevButton = new JButton("<");
	private final JButton nextButton = new JButton(">");

	private YearMonth currentYM = YearMonth.now();
	private Set<LocalDate> disabledDates = Set.of(); // 선택 불가 날짜
	private DateSelectionListener listener;

	public CalendarView(Window owner, Set<LocalDate> disabledDates, DateSelectionListener listener) {
		super(owner, "캘린더", ModalityType.APPLICATION_MODAL);
		this.disabledDates = disabledDates;
		this.listener = listener;
		init();
	}

	private void init() {
		setSize(350, 300);
		setLocationRelativeTo(getOwner());
		setLayout(new BorderLayout());

		JPanel top = new JPanel(new BorderLayout());
		prevButton.addActionListener(e -> {
			currentYM = currentYM.minusMonths(1);
			draw();
		});
		nextButton.addActionListener(e -> {
			currentYM = currentYM.plusMonths(1);
			draw();
		});

		top.add(prevButton, BorderLayout.WEST);
		top.add(monthLabel, BorderLayout.CENTER);
		top.add(nextButton, BorderLayout.EAST);

		add(top, BorderLayout.NORTH);
		add(calendarPanel, BorderLayout.CENTER);

		draw();
	}

	private void draw() {
		calendarPanel.removeAll();

		monthLabel.setText(
				currentYM.getMonth().getDisplayName(TextStyle.FULL, Locale.KOREAN) + " " + currentYM.getYear());

		// 요일 헤더
		for (String d : new String[] { "일", "월", "화", "수", "목", "금", "토" }) {
			JLabel l = new JLabel(d, SwingConstants.CENTER);
			l.setFont(l.getFont().deriveFont(Font.BOLD));
			calendarPanel.add(l);
		}

		int offset = currentYM.atDay(1).getDayOfWeek().getValue() % 7;
		for (int i = 0; i < offset; i++)
			calendarPanel.add(new JLabel(""));

		LocalDate today = LocalDate.now();
		for (int day = 1; day <= currentYM.lengthOfMonth(); day++) {
			LocalDate date = currentYM.atDay(day);

			JButton b = new JButton(String.valueOf(day));
			b.setFocusPainted(false);

			if (disabledDates.contains(date)) {
				b.setEnabled(false);
				b.setForeground(Color.GRAY);
			} else {
				if (date.equals(today)) {
					b.setBackground(new Color(255, 200, 200));
				}
				b.addActionListener(e -> {
					if (listener != null)
						listener.onDateSelected(date);
					dispose();
				});
			}
			calendarPanel.add(b);
		}
		calendarPanel.revalidate();
		calendarPanel.repaint();
	}
}
