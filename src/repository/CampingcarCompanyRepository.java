
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
import entity.CampingcarCompany;

public class CampingcarCompanyRepository {
	private final DBConfig db;

	public CampingcarCompanyRepository(DBConfig db) {
		this.db = db;
	}

	public List<CampingcarCompany> findAll() throws SQLException {
		String sql = "SELECT * FROM campingcar_company";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<CampingcarCompany> list = new ArrayList<>();
			while (rs.next()) {
				list.add(CampingcarCompany.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<CampingcarCompany> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM campingcar_company WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(CampingcarCompany.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public CampingcarCompany save(CampingcarCompany entity) throws SQLException {
		String sql = "INSERT INTO campingcar_company (name, address, phone_number) VALUES (?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getAddress());
			ps.setString(3, entity.getPhoneNumber());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(CampingcarCompany entity) throws SQLException {
		String sql = "UPDATE campingcar_company SET name = ?, address = ?, phone_number = ? WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getName());
			ps.setString(2, entity.getAddress());
			ps.setString(3, entity.getPhoneNumber());
			ps.setLong(4, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM campingcar_company WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
