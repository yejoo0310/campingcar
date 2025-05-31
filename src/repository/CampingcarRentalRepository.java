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
import dto.CampingcarRentalHistory;
import entity.Campingcar;
import entity.CampingcarRental;

public class CampingcarRentalRepository {
	private final DBConfig db;

	public CampingcarRentalRepository(DBConfig db) {
		this.db = db;
	}

	public List<CampingcarRental> findAll() throws SQLException {
		String sql = "SELECT * FROM campingcar_rental";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CampingcarRental> list = new ArrayList<>();
			while (rs.next()) {
				list.add(CampingcarRental.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<CampingcarRental> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM campingcar_rental WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(CampingcarRental.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public List<CampingcarRental> findByCampingcarId(Long campingcarId) throws SQLException {
		String sql = "SELECT * FROM campingcar_rental WHERE campingcar_id = ?";
		List<CampingcarRental> rentals = new ArrayList<>();

		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, campingcarId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rentals.add(CampingcarRental.fromResultSet(rs));
				}
			}
		}

		return rentals;
	}

	public List<CampingcarRental> findByCustomerId(Long customerId) throws SQLException {
		String sql = "SELECT * FROM campingcar_rental WHERE customer_id = ?";
		List<CampingcarRental> rentals = new ArrayList<>();

		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, customerId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					rentals.add(CampingcarRental.fromResultSet(rs));
				}
			}
		}

		return rentals;
	}

	public List<CampingcarRentalHistory> findByCustomerIdWithCampingcar(Long customerId) throws SQLException {
		String sql = "SELECT cr.*, c.id AS c_id, c.name AS c_name, c.number AS c_number, "
				+ "c.seat_capacity AS c_seat_capacity, c.image AS c_image, "
				+ "c.information AS c_information, c.registered_date AS c_registered_date, "
				+ "c.campingcar_company_id AS c_campingcar_company_id " + "FROM campingcar_rental cr "
				+ "JOIN campingcar c ON cr.campingcar_id = c.id " + "WHERE cr.customer_id = ?";
		List<CampingcarRentalHistory> historyList = new ArrayList<>();

		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setLong(1, customerId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					CampingcarRental rental = CampingcarRental.fromResultSet(rs);

					Campingcar campingcar = new Campingcar(rs.getLong("c_id"), rs.getString("c_name"),
							rs.getString("c_number"), rs.getInt("c_seat_capacity"), rs.getString("c_image"),
							rs.getString("c_information"), rs.getDate("c_registered_date"),
							rs.getLong("c_campingcar_company_id"));

					historyList.add(new CampingcarRentalHistory(rental, campingcar));
				}
			}
		}

		return historyList;
	}

	public CampingcarRental save(CampingcarRental entity) throws SQLException {
		String sql = "INSERT INTO campingcar_rental "
				+ "(start_date, period_days, fee, payment_due_date, additional_payment_notes, additional_fee, customer_id, campingcar_id, campingcar_company_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setDate(1, entity.getStartDate());
			ps.setInt(2, entity.getPeriodDays());
			ps.setInt(3, entity.getFee());
			ps.setDate(4, entity.getPaymentDueDate());
			ps.setString(5, entity.getAdditionalPaymentNotes());
			ps.setInt(6, entity.getAdditionalFee());
			ps.setLong(7, entity.getCustomerId());
			ps.setLong(8, entity.getCampingcarId());
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

	public void update(CampingcarRental entity) throws SQLException {
		String sql = "UPDATE campingcar_rental SET "
				+ "start_date = ?, period_days = ?, fee = ?, payment_due_date = ?, "
				+ "additional_payment_notes = ?, additional_fee = ?, customer_id = ?, "
				+ "campingcar_id = ?, campingcar_company_id = ? WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setDate(1, entity.getStartDate());
			ps.setInt(2, entity.getPeriodDays());
			ps.setInt(3, entity.getFee());
			ps.setDate(4, entity.getPaymentDueDate());
			ps.setString(5, entity.getAdditionalPaymentNotes());
			ps.setInt(6, entity.getAdditionalFee());
			ps.setLong(7, entity.getCustomerId());
			ps.setLong(8, entity.getCampingcarId());
			ps.setLong(9, entity.getCampingcarCompanyId());
			ps.setLong(10, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM campingcar_rental WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
