package app;

import service.AuthService;
import service.MaintenanceService;
import service.RentalService;
import view.Screen;
import view.screen.AdminPanel;
import view.screen.LoginPanel;
import view.screen.MaintenanceRequestPanel;
import view.screen.RentalHistoryPanel;
import view.screen.RentalRegistrationPanel;
import view.screen.UserPanel;

public class ScreenFactory {
	private final AuthService authService;
	private final RentalService rentalService;
	private final MaintenanceService maintenanceService;

	public ScreenFactory(AuthService authService, RentalService rentalService, MaintenanceService maintenanceService) {
		this.authService = authService;
		this.rentalService = rentalService;
		this.maintenanceService = maintenanceService;
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
		return new RentalHistoryPanel(rentalService);
	}

	public Screen maintenance() {
		return new MaintenanceRequestPanel(maintenanceService, rentalService);
	}
}
