package context;

import entity.Customer;

public class AppSession {
	private static Customer loggedInCustomer;
	private static Boolean isAdmin;

	public static void setLoggedInCustomer(Customer customer) {
		loggedInCustomer = customer;
	}

	public static Customer getLoggedInCustomer() {
		return loggedInCustomer;
	}

	public static boolean isAuthenticated() {
		return loggedInCustomer != null;
	}

	public static void clear() {
		loggedInCustomer = null;
	}

	public static Boolean getIsAdmin() {
		return isAdmin;
	}

	public static void setIsAdmin(Boolean isAdmin) {
		AppSession.isAdmin = isAdmin;
	}
}
