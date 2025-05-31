package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import configure.DBConfig;
import entity.Campingcar;

public class CampingcarRepository {
	private final DBConfig db;

	public CampingcarRepository(DBConfig db) {
		this.db = db;
	}

	public List<Campingcar> findAll() throws SQLException {
		String sql = "SELECT * FROM campingcar";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<Campingcar> list = new ArrayList<>();
			while (rs.next()) {
				list.add(Campingcar.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<Campingcar> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM campingcar WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(Campingcar.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public List<Campingcar> findByCampingcarId(Long id) throws SQLException {
		String sql = "SELECT * FROM campingcar WHERE id = ?";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<Campingcar> list = new ArrayList<>();
			while (rs.next()) {
				list.add(Campingcar.fromResultSet(rs));
			}
			return list;
		}
	}

	public Campingcar save(Campingcar entity) throws SQLException {
		String sql = "INSERT INTO campingcar "
				+ "(name, number, seat_capacity, image, information, registered_date, campingcar_company_id) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getNumber());
			ps.setInt(3, entity.getSeatCapacity());
			ps.setString(4, entity.getImage());
			ps.setString(5, entity.getInformation());
			ps.setDate(6, entity.getRegisteredDate());
			if (entity.getCampingcarCompanyId() != null) {
				ps.setLong(7, entity.getCampingcarCompanyId());
			} else {
				ps.setNull(7, Types.BIGINT);
			}

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(Campingcar entity) throws SQLException {
		String sql = "UPDATE campingcar SET " + "name = ?, number = ?, seat_capacity = ?, image = ?, information = ?, "
				+ "registered_date = ?, campingcar_company_id = ? " + "WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getNumber());
			ps.setInt(3, entity.getSeatCapacity());
			ps.setString(4, entity.getImage());
			ps.setString(5, entity.getInformation());
			ps.setDate(6, entity.getRegisteredDate());
			if (entity.getCampingcarCompanyId() != null) {
				ps.setLong(7, entity.getCampingcarCompanyId());
			} else {
				ps.setNull(7, Types.BIGINT);
			}
			ps.setLong(8, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM campingcar WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
