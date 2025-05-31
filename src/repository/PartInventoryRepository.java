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
import entity.PartInventory;

public class PartInventoryRepository {
	private final DBConfig db;

	public PartInventoryRepository(DBConfig db) {
		this.db = db;
	}

	public List<PartInventory> findAll() throws SQLException {
		String sql = "SELECT * FROM part_inventory";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql);
				ResultSet rs = ps.executeQuery()) {

			List<PartInventory> list = new ArrayList<>();
			while (rs.next()) {
				list.add(PartInventory.fromResultSet(rs));
			}
			return list;
		}
	}

	public Optional<PartInventory> findById(Long id) throws SQLException {
		String sql = "SELECT * FROM part_inventory WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return Optional.of(PartInventory.fromResultSet(rs));
				}
				return Optional.empty();
			}
		}
	}

	public PartInventory save(PartInventory entity) throws SQLException {
		String sql = "INSERT INTO part_inventory " + "(name, price, quantity, stocked_date, provider_name) "
				+ "VALUES (?, ?, ?, ?, ?)";
		try (Connection conn = db.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setString(1, entity.getName());
			ps.setInt(2, entity.getPrice());
			ps.setInt(3, entity.getQuantity());
			ps.setDate(4, entity.getStockedDate());
			ps.setString(5, entity.getProviderName());

			ps.executeUpdate();
			try (ResultSet keys = ps.getGeneratedKeys()) {
				if (keys.next()) {
					entity.setId(keys.getLong(1));
				}
			}
			return entity;
		}
	}

	public void update(PartInventory entity) throws SQLException {
		String sql = "UPDATE part_inventory SET "
				+ "name = ?, price = ?, quantity = ?, stocked_date = ?, provider_name = ? " + "WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, entity.getName());
			ps.setInt(2, entity.getPrice());
			ps.setInt(3, entity.getQuantity());
			ps.setDate(4, entity.getStockedDate());
			ps.setString(5, entity.getProviderName());
			ps.setLong(6, entity.getId());

			ps.executeUpdate();
		}
	}

	public void delete(Long id) throws SQLException {
		String sql = "DELETE FROM part_inventory WHERE id = ?";
		try (Connection conn = db.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setLong(1, id);
			ps.executeUpdate();
		}
	}
}
