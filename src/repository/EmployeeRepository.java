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
import entity.Employee;

public class EmployeeRepository {
	private final DBConfig db;

	public EmployeeRepository(DBConfig db) {
		this.db = db;
	}

	public List<Employee> findAll() throws SQLException {
		String sql = "SELECT * FROM employee";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<Employee> list = new ArrayList<>();
			while (rs.next()) {
				list.add(Employee.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<Employee> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM employee WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(Employee.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public Employee save(Employee entity) throws SQLException {
		String sql = "INSERT INTO employee "
				+ "(name, phone_number, address, salary, dependants, team_name, responsibility, email, campingcar_company_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getPhoneNumber());
			ps.setString(3, entity.getAddress());
			ps.setInt(4, entity.getSalary());
			ps.setInt(5, entity.getDependants());
			ps.setString(6, entity.getTeamname());
			ps.setString(7, entity.getResponsibility());
			ps.setString(8, entity.getEmail());
			ps.setLong(9, entity.getCampingcarCompanyId());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(Employee entity) throws SQLException {
		String sql = "UPDATE employee SET " + "name = ?, phone_number = ?, address = ?, salary = ?, dependants = ?, "
				+ "team_name = ?, responsibility = ?, email = ?, campingcar_company_id = ? " + "WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getPhoneNumber());
			ps.setString(3, entity.getAddress());
			ps.setInt(4, entity.getSalary());
			ps.setInt(5, entity.getDependants());
			ps.setString(6, entity.getTeamname());
			ps.setString(7, entity.getResponsibility());
			ps.setString(8, entity.getEmail());
			ps.setLong(9, entity.getCampingcarCompanyId());
			ps.setLong(10, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM employee WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
