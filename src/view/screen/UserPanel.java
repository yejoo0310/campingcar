package view.screen;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import router.Router;
import service.RentalService;
import view.Screen;

public class UserPanel extends JPanel implements Screen {
	private final RentalService rentalService;

	public UserPanel(RentalService rentalService) {
		this.rentalService = rentalService;
	}

	@Override
	public JPanel render(Router router) {
		setLayout(new GridBagLayout());
		GridBagConstraints g = new GridBagConstraints();
		g.insets = new Insets(15, 10, 15, 10);

		JButton rental = new JButton("캠핑카 대여하기");
		JButton history = new JButton("대여 기록 보기");
		JButton maintain = new JButton("정비 의뢰하기");
		JButton back = new JButton("뒤로가기");

		g.gridy = 0;
		add(rental, g);
		g.gridy = 1;
		add(history, g);
		g.gridy = 2;
		add(maintain, g);
		g.gridy = 3;
		add(back, g);

		rental.addActionListener(e -> router.navigate("/rental/register"));
		history.addActionListener(e -> router.navigate("/rental/history"));
		maintain.addActionListener(e -> router.navigate("/maintenance"));
		back.setEnabled(router.canGoBack());
		back.addActionListener(e -> router.back());

		return this;
	}
}
