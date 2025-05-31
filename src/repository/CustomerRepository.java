package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import configure.DBConfig;
import entity.Customer;

public class CustomerRepository {
	private final DBConfig db;

	public CustomerRepository(DBConfig db) {
		this.db = db;
	}

	public List<Customer> findAll() throws SQLException {
		String sql = "SELECT * FROM customer";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<Customer> list = new ArrayList<>();
			while (rs.next()) {
				list.add(Customer.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<Customer> findByAccountId(String accountId) throws SQLException {
		String sql = "SELECT * FROM customer WHERE account_id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, accountId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(Customer.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public Optional<Customer> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM customer WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(Customer.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public Customer save(Customer entity) throws SQLException {
		String sql = "INSERT INTO customer "
				+ "(account_id, password, driver_lisence_num, name, address, phone_number, email) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getAccountId());
			ps.setString(2, entity.getPassword());
			ps.setString(3, entity.getDriverLisenceNum());
			ps.setString(4, entity.getName());
			ps.setString(5, entity.getAddress());
			ps.setString(6, entity.getPhoneNumber());
			ps.setString(7, entity.getEmail());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(Customer entity) throws SQLException {
		String sql = "UPDATE customer SET account_id = ?, password = ?, driver_lisence_num = ?, "
				+ "name = ?, address = ?, phone_number = ?, email = ? WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getAccountId());
			ps.setString(2, entity.getPassword());
			ps.setString(3, entity.getDriverLisenceNum());
			ps.setString(4, entity.getName());
			ps.setString(5, entity.getAddress());
			ps.setString(6, entity.getPhoneNumber());
			ps.setString(7, entity.getEmail());
			ps.setLong(8, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM customer WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
