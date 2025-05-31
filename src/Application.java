import java.sql.SQLException;

import app.AppFrame;
import app.ScreenFactory;
import configure.DBConfig;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import repository.CarExternalMaintenanceCenterRepository;
import repository.CarExternalMaintenanceRecordRepository;
import repository.CustomerRepository;
import service.AuthService;
import service.MaintenanceService;
import service.RentalService;

public class Application {

	public static void main(String[] args) throws SQLException {
		DBConfig dbConfig = new DBConfig();

		CustomerRepository customerRepository = new CustomerRepository(dbConfig);
		CampingcarRepository campingcarRepository = new CampingcarRepository(dbConfig);
		CampingcarRentalRepository campingcarRentalRepository = new CampingcarRentalRepository(dbConfig);
		CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository = new CarExternalMaintenanceCenterRepository(
				dbConfig);
		CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository = new CarExternalMaintenanceRecordRepository(
				dbConfig);

		AuthService authService = new AuthService(customerRepository);
		RentalService rentalService = new RentalService(campingcarRepository, campingcarRentalRepository);
		MaintenanceService maintenanceService = new MaintenanceService(carExternalMaintenanceCenterRepository,
				campingcarRentalRepository, carExternalMaintenanceRecordRepository);

		ScreenFactory factory = new ScreenFactory(authService, rentalService, maintenanceService);
		javax.swing.SwingUtilities.invokeLater(() -> new AppFrame(factory));
	}

}
