package app;

import service.AuthService;
import service.RentalService;
import view.Screen;
import view.screen.*;

public class ScreenFactory {
	private final AuthService authService;
	private final RentalService rentalService;

	public ScreenFactory(AuthService authService, RentalService rentalService) {
		this.authService = authService;
		this.rentalService = rentalService;
	}

	/* Supplier<Screen> 형태로 Router에 제공할 메서드들 */
	public Screen login() {
		return new LoginPanel(authService, rentalService);
	}

	public Screen admin() {
		return new AdminPanel();
	}

	public Screen user() {
		return new UserPanel(rentalService);
	}

	public Screen rentalRegister() {
		return new RentalRegistrationPanel(rentalService);
	}

	public Screen rentalHistory() {
		return new RentalHistoryPanel();
	}

	public Screen maintenance() {
		return new MaintenanceRequestPanel();
	}
}
