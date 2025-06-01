// ScreenFactory.java
package app;

import service.AdminService;
import service.AuthService;
import service.InitService;
import service.MaintenanceService;
import service.RentalService;
import view.Screen;
import view.screen.AdminPanel;
import view.screen.AllTablesPanel;
import view.screen.CampingcarViewPanel;
import view.screen.LoginPanel;
import view.screen.MaintenanceRequestPanel;
import view.screen.RentalHistoryPanel;
import view.screen.RentalRegistrationPanel;
import view.screen.UserPanel;

public class ScreenFactory {
	private final AuthService authService;
	private final RentalService rentalService;
	private final MaintenanceService maintenanceService;
	private final InitService initService;
	private final AdminService adminService;

	public ScreenFactory(AuthService authService, RentalService rentalService, MaintenanceService maintenanceService,
			InitService initService, AdminService adminService) {
		this.authService = authService;
		this.rentalService = rentalService;
		this.maintenanceService = maintenanceService;
		this.initService = initService;
		this.adminService = adminService;
	}

	// 각 경로별 화면 생성 메서드
	public Screen login() {
		return new LoginPanel(authService, rentalService);
	}

	public Screen admin() {
		return new AdminPanel(initService);
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

	public Screen allTables() {
		return new AllTablesPanel(adminService);
	}

	public Screen campingcarView() {
		return new CampingcarViewPanel(maintenanceService);
	}
}
