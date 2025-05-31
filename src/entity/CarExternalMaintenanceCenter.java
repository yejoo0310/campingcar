package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class CarExternalMaintenanceCenter {
	@Column("id")
	private Long id;

	@Column("cetner_name")
	private String centerName;

	@Column("center_address")
	private String centerAddress;

	@Column("center_phone")
	private String centerPhone;

	@Column("manager_name")
	private String managerName;

	@Column("manager_email")
	private String managerEmail;

	public CarExternalMaintenanceCenter() {
	}

	public CarExternalMaintenanceCenter(Long id, String centerName, String centerAddress, String centerPhone,
			String managerName, String managerEmail) {
		this.id = id;
		this.centerName = centerName;
		this.centerAddress = centerAddress;
		this.centerPhone = centerPhone;
		this.managerName = managerName;
		this.managerEmail = managerEmail;
	}

	public Long getId() {
		return id;
	}

	public String getCenterName() {
		return centerName;
	}

	public String getCenterAddress() {
		return centerAddress;
	}

	public String getCenterPhone() {
		return centerPhone;
	}

	public String getManagerName() {
		return managerName;
	}

	public String getManagerEmail() {
		return managerEmail;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setCenterName(String centerName) {
		this.centerName = centerName;
	}

	public void setCenterAddress(String centerAddress) {
		this.centerAddress = centerAddress;
	}

	public void setCenterPhone(String centerPhone) {
		this.centerPhone = centerPhone;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public void setManagerEmail(String managerEmail) {
		this.managerEmail = managerEmail;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 CarExternalMaintenanceCenter 객체를 생성합니다.
	 */
	public static CarExternalMaintenanceCenter fromResultSet(ResultSet rs) throws SQLException {
		return new CarExternalMaintenanceCenter(rs.getLong("id"), rs.getString("cetner_name"),
				rs.getString("center_address"), rs.getString("center_phone"), rs.getString("manager_name"),
				rs.getString("manager_email"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
