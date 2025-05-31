import java.sql.SQLException;

import app.AppFrame;
import app.ScreenFactory;
import configure.DBConfig;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import repository.CustomerRepository;
import service.AuthService;
import service.RentalService;

public class Application {

	public static void main(String[] args) throws SQLException {
		DBConfig dbConfig = new DBConfig();

		CustomerRepository customerRepository = new CustomerRepository(dbConfig);
		CampingcarRepository campingcarRepository = new CampingcarRepository(dbConfig);
		CampingcarRentalRepository campingcarRentalRepository = new CampingcarRentalRepository(dbConfig);

		AuthService authService = new AuthService(customerRepository);
		RentalService rentalService = new RentalService(campingcarRepository, campingcarRentalRepository);

		ScreenFactory factory = new ScreenFactory(authService, rentalService);
		javax.swing.SwingUtilities.invokeLater(() -> new AppFrame(factory));
	}

}
