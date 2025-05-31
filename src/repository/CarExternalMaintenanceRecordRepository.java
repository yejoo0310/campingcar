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
import entity.CarExternalMaintenanceRecord;

public class CarExternalMaintenanceRecordRepository {
	private final DBConfig db;

	public CarExternalMaintenanceRecordRepository(DBConfig db) {
		this.db = db;
	}

	public List<CarExternalMaintenanceRecord> findAll() throws SQLException {
		String sql = "SELECT * FROM car_external_maintenance_record";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CarExternalMaintenanceRecord> list = new ArrayList<>();
			while (rs.next()) {
				list.add(CarExternalMaintenanceRecord.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<CarExternalMaintenanceRecord> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM car_external_maintenance_record WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(CarExternalMaintenanceRecord.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public CarExternalMaintenanceRecord save(CarExternalMaintenanceRecord entity) throws SQLException {
		String sql = "INSERT INTO car_external_maintenance_record "
				+ "(description, date, cost, due_date, additional_notes, campingcar_id, customer_id, car_external_maintenance_center_id, campingcar_company_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getDescription());
			ps.setDate(2, entity.getDate());
			ps.setInt(3, entity.getCost());
			ps.setDate(4, entity.getDueDate());
			ps.setString(5, entity.getAdditionalNotes());
			ps.setLong(6, entity.getCampingcarId());
			ps.setLong(7, entity.getCustomerId());
			ps.setLong(8, entity.getCarExternalMaintenanceCenterId());
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

	public void update(CarExternalMaintenanceRecord entity) throws SQLException {
		String sql = "UPDATE car_external_maintenance_record SET "
				+ "description = ?, date = ?, cost = ?, due_date = ?, additional_notes = ?, "
				+ "campingcar_id = ?, customer_id = ?, car_external_maintenance_center_id = ?, campingcar_company_id = ? "
				+ "WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getDescription());
			ps.setDate(2, entity.getDate());
			ps.setInt(3, entity.getCost());
			ps.setDate(4, entity.getDueDate());
			ps.setString(5, entity.getAdditionalNotes());
			ps.setLong(6, entity.getCampingcarId());
			ps.setLong(7, entity.getCustomerId());
			ps.setLong(8, entity.getCarExternalMaintenanceCenterId());
			ps.setLong(9, entity.getCampingcarCompanyId());
			ps.setLong(10, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM car_external_maintenance_record WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
