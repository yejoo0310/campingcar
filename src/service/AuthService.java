package service;

import entity.Customer;
import repository.CustomerRepository;
import util.ApplicationException;

public class AuthService {
	private final static String ROOT_USERNAME = "root";
	private final static String ROOT_PASSWORD = "1234";

	private final CustomerRepository customerRepository;

	public AuthService(CustomerRepository customerRepository) {
		this.customerRepository = customerRepository;
	};

	public boolean loginAdmin(String accountId, String password) {
		return accountId.equals(ROOT_USERNAME) && password.equals(ROOT_PASSWORD);
	}

	public Customer loginUser(String accountId, String password) throws ApplicationException {
		try {
			Customer customer = customerRepository.findByAccountId(accountId)
					.orElseThrow(() -> ApplicationException.of("login 오류"));
			if (customer.getPassword().equals(password)) {
				return customer;
			}
			throw ApplicationException.of("login 오류");
		} catch (Exception e) {
			throw ApplicationException.of("login 오류");
		}
	}
}
