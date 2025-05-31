package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class Customer {
	@Column("id")
	private Long id;

	@Column("account_id")
	private String accountId;

	@Column("password")
	private String password;

	@Column("driver_lisence_num")
	private String driverLisenceNum;

	@Column("name")
	private String name;

	@Column("address")
	private String address;

	@Column("phone_number")
	private String phoneNumber;

	@Column("email")
	private String email;

	public Customer() {
	}

	public Customer(Long id, String accountId, String password, String driverLisenceNum, String name, String address,
			String phoneNumber, String email) {
		this.id = id;
		this.accountId = accountId;
		this.password = password;
		this.driverLisenceNum = driverLisenceNum;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public String getAccountId() {
		return accountId;
	}

	public String getPassword() {
		return password;
	}

	public String getDriverLisenceNum() {
		return driverLisenceNum;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setDriverLisenceNum(String driverLisenceNum) {
		this.driverLisenceNum = driverLisenceNum;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 Customer 객체를 생성합니다.
	 */
	public static Customer fromResultSet(ResultSet rs) throws SQLException {
		return new Customer(rs.getLong("id"), rs.getString("account_id"), rs.getString("password"),
				rs.getString("driver_lisence_num"), rs.getString("name"), rs.getString("address"),
				rs.getString("phone_number"), rs.getString("email"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
