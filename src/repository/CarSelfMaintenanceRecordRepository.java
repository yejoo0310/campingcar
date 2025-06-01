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
import entity.CarSelfMaintenanceRecord;

public class CarSelfMaintenanceRecordRepository {
	private final DBConfig db;

	public CarSelfMaintenanceRecordRepository(DBConfig db) {
		this.db = db;
	}

	public List<CarSelfMaintenanceRecord> findAll() throws SQLException {
		String sql = "SELECT * FROM car_self_maintenance_record";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CarSelfMaintenanceRecord> list = new ArrayList<>();
			while (rs.next()) {
				list.add(CarSelfMaintenanceRecord.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<CarSelfMaintenanceRecord> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM car_self_maintenance_record WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(CarSelfMaintenanceRecord.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public List<CarSelfMaintenanceRecord> findByCampingcarId(Long campingcarId) throws SQLException {
		String sql = "SELECT * FROM car_self_maintenance_record WHERE campingcar_id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, campingcarId);

			try (ResultSet rs = ps.executeQuery()) {
				List<CarSelfMaintenanceRecord> list = new ArrayList<>();
				while (rs.next()) {
					list.add(CarSelfMaintenanceRecord.fromResultSet(rs));
				}
				return list;
			}
		}
	}

	public CarSelfMaintenanceRecord save(CarSelfMaintenanceRecord entity) throws SQLException {
		String sql = "INSERT INTO car_self_maintenance_record "
				+ "(maintenance_date, duration_minute, campingcar_id, employee_id, part_inventory_id) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setDate(1, entity.getMaintenanceDate());
			ps.setInt(2, entity.getDurationMinute());
			ps.setLong(3, entity.getCampingcarId());
			ps.setLong(4, entity.getEmployeeId());
			ps.setLong(5, entity.getPartInventoryId());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(CarSelfMaintenanceRecord entity) throws SQLException {
		String sql = "UPDATE car_self_maintenance_record SET "
				+ "maintenance_date = ?, duration_minute = ?, campingcar_id = ?, employee_id = ?, part_inventory_id = ? "
				+ "WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, entity.getMaintenanceDate());
			ps.setInt(2, entity.getDurationMinute());
			ps.setLong(3, entity.getCampingcarId());
			ps.setLong(4, entity.getEmployeeId());
			ps.setLong(5, entity.getPartInventoryId());
			ps.setLong(6, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM car_self_maintenance_record WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
