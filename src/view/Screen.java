package view;

import javax.swing.JPanel;

import router.Router;

public interface Screen {
	/** Router를 받아 자신의 UI 패널을 반환 */
	JPanel render(Router router);
}
