package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class PartInventory {
	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("price")
	private int price;

	@Column("quantity")
	private int quantity;

	@Column("stocked_date")
	private Date stockedDate;

	@Column("provider_name")
	private String providerName;

	public PartInventory() {
	}

	public PartInventory(Long id, String name, int price, int quantity, Date stockedDate, String providerName) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.quantity = quantity;
		this.stockedDate = stockedDate;
		this.providerName = providerName;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getPrice() {
		return price;
	}

	public int getQuantity() {
		return quantity;
	}

	public Date getStockedDate() {
		return stockedDate;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setStockedDate(Date stockedDate) {
		this.stockedDate = stockedDate;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 PartInventory 객체를 생성합니다.
	 */
	public static PartInventory fromResultSet(ResultSet rs) throws SQLException {
		return new PartInventory(rs.getLong("id"), rs.getString("name"), rs.getInt("price"), rs.getInt("quantity"),
				rs.getDate("stocked_date"), rs.getString("provider_name"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
