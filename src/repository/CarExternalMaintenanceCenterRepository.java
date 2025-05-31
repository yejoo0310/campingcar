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
import entity.CarExternalMaintenanceCenter;

public class CarExternalMaintenanceCenterRepository {
	private final DBConfig db;

	public CarExternalMaintenanceCenterRepository(DBConfig db) {
		this.db = db;
	}

	public List<CarExternalMaintenanceCenter> findAll() throws SQLException {
		String sql = "SELECT * FROM car_external_maintenance_center";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CarExternalMaintenanceCenter> list = new ArrayList<>();
			while (rs.next()) {
				list.add(CarExternalMaintenanceCenter.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<CarExternalMaintenanceCenter> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM car_external_maintenance_center WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(CarExternalMaintenanceCenter.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public CarExternalMaintenanceCenter save(CarExternalMaintenanceCenter entity) throws SQLException {
		String sql = "INSERT INTO car_external_maintenance_center "
				+ "(center_name, center_address, center_phone, manager_name, manager_email) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getCenterName());
			ps.setString(2, entity.getCenterAddress());
			ps.setString(3, entity.getCenterPhone());
			ps.setString(4, entity.getManagerName());
			ps.setString(5, entity.getManagerEmail());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(CarExternalMaintenanceCenter entity) throws SQLException {
		String sql = "UPDATE car_external_maintenance_center SET "
				+ "center_name = ?, center_address = ?, center_phone = ?, "
				+ "manager_name = ?, manager_email = ? WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getCenterName());
			ps.setString(2, entity.getCenterAddress());
			ps.setString(3, entity.getCenterPhone());
			ps.setString(4, entity.getManagerName());
			ps.setString(5, entity.getManagerEmail());
			ps.setLong(6, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM car_external_maintenance_center WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
