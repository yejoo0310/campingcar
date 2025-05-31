package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class Employee {
	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("phone_number")
	private String phoneNumber;

	@Column("address")
	private String address;

	@Column("salary")
	private int salary;

	@Column("dependants")
	private int dependants;

	@Column("team_name")
	private String teamName;

	@Column("responsibility")
	private String responsibility;

	@Column("email")
	private String email;

	@Column("campingcar_company_id")
	private Long campingcarCompanyId;

	public Employee() {
	}

	public Employee(Long id, String name, String phoneNumber, String address, int salary, int dependants,
			String teamName, String responsibility, String email, Long campingcarCompanyId) {
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.address = address;
		this.salary = salary;
		this.dependants = dependants;
		this.teamName = teamName;
		this.responsibility = responsibility;
		this.email = email;
		this.campingcarCompanyId = campingcarCompanyId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getAddress() {
		return address;
	}

	public int getSalary() {
		return salary;
	}

	public int getDependants() {
		return dependants;
	}

	public String getTeamname() {
		return teamName;
	}

	public String getEmail() {
		return email;
	}

	public String getResponsibility() {
		return responsibility;
	}

	public Long getCampingcarCompanyId() {
		return campingcarCompanyId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public void setDependants(int dependants) {
		this.dependants = dependants;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setResponsibility(String responsibility) {
		this.responsibility = responsibility;
	}

	public void setCampingcarCompanyId(Long campingcarCompanyId) {
		this.campingcarCompanyId = campingcarCompanyId;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 Employee 객체를 생성합니다.
	 */
	public static Employee fromResultSet(ResultSet rs) throws SQLException {
		return new Employee(rs.getLong("id"), rs.getString("name"), rs.getString("phone_number"),
				rs.getString("address"), rs.getInt("salary"), rs.getInt("dependants"), rs.getString("team_name"),
				rs.getString("responsibility"), rs.getString("email"), rs.getLong("campingcar_company_id"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
