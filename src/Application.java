import java.sql.SQLException;

import app.AppFrame;
import app.ScreenFactory;
import configure.DBConfig;
import repository.CampingcarCompanyRepository;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import repository.CarExternalMaintenanceCenterRepository;
import repository.CarExternalMaintenanceRecordRepository;
import repository.CarSelfMaintenanceRecordRepository;
import repository.CustomerRepository;
import repository.EmployeeRepository;
import repository.NativeQueryRepository;
import repository.PartInventoryRepository;
import service.AdminService;
import service.AuthService;
import service.InitService;
import service.MaintenanceService;
import service.RentalService;

public class Application {

	public static void main(String[] args) throws SQLException {
		DBConfig dbConfig = new DBConfig();

		CustomerRepository customerRepository = new CustomerRepository(dbConfig);
		PartInventoryRepository partInventoryRepository = new PartInventoryRepository(dbConfig);
		CampingcarRepository campingcarRepository = new CampingcarRepository(dbConfig);
		CampingcarCompanyRepository campingcarCompanyRepository = new CampingcarCompanyRepository(dbConfig);
		CampingcarRentalRepository campingcarRentalRepository = new CampingcarRentalRepository(dbConfig);
		EmployeeRepository employeeRepository = new EmployeeRepository(dbConfig);
		CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository = new CarExternalMaintenanceCenterRepository(
				dbConfig);
		CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository = new CarExternalMaintenanceRecordRepository(
				dbConfig);
		CarSelfMaintenanceRecordRepository carSelfMaintenanceRecordRepository = new CarSelfMaintenanceRecordRepository(
				dbConfig);

		NativeQueryRepository nativeQueryRepository = new NativeQueryRepository(dbConfig);

		AdminService adminService = new AdminService(customerRepository, partInventoryRepository, campingcarRepository,
				campingcarCompanyRepository, campingcarRentalRepository, employeeRepository,
				carExternalMaintenanceCenterRepository, carExternalMaintenanceRecordRepository,
				carSelfMaintenanceRecordRepository, nativeQueryRepository);
		InitService initService = new InitService();
		AuthService authService = new AuthService(customerRepository);
		RentalService rentalService = new RentalService(campingcarRepository, campingcarRentalRepository);
		MaintenanceService maintenanceService = new MaintenanceService(campingcarRepository,
				carExternalMaintenanceCenterRepository, campingcarRentalRepository,
				carExternalMaintenanceRecordRepository, carSelfMaintenanceRecordRepository, partInventoryRepository);

		ScreenFactory factory = new ScreenFactory(authService, rentalService, maintenanceService, initService,
				adminService);
		javax.swing.SwingUtilities.invokeLater(() -> new AppFrame(factory));
	}
}
