package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class CarSelfMaintenanceRecord {
	@Column("id")
	private Long id;

	@Column("maintenance_date")
	private Date maintenanceDate;

	@Column("duration_minute")
	private int durationMinute;

	@Column("camingcar_id")
	private Long campingcarId;

	@Column("employee_id")
	private Long employeeId;

	@Column("part_inventory_id")
	private Long partInventoryId;

	public CarSelfMaintenanceRecord() {
	}

	public CarSelfMaintenanceRecord(Long id, Date maintenanceDate, int durationMinute, Long campingcarId,
			Long employeeId, Long partInventoryId) {
		this.id = id;
		this.maintenanceDate = maintenanceDate;
		this.durationMinute = durationMinute;
		this.campingcarId = campingcarId;
		this.employeeId = employeeId;
		this.partInventoryId = partInventoryId;
	}

	public Long getId() {
		return id;
	}

	public Date getMaintenanceDate() {
		return maintenanceDate;
	}

	public int getDurationMinute() {
		return durationMinute;
	}

	public Long getCampingcarId() {
		return campingcarId;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public Long getPartInventoryId() {
		return partInventoryId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setMaintenanceDate(Date maintenanceDate) {
		this.maintenanceDate = maintenanceDate;
	}

	public void setDurationMinute(int durationMinute) {
		this.durationMinute = durationMinute;
	}

	public void setCampingcarId(Long campingcarId) {
		this.campingcarId = campingcarId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public void setPartInventoryId(Long partInventoryId) {
		this.partInventoryId = partInventoryId;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 CarSelfMaintenanceRecord 객체를 생성합니다.
	 */
	public static CarSelfMaintenanceRecord fromResultSet(ResultSet rs) throws SQLException {
		return new CarSelfMaintenanceRecord(rs.getLong("id"), rs.getDate("maintenance_date"),
				rs.getInt("duration_minute"), rs.getLong("camingcar_id"), rs.getLong("employee_id"),
				rs.getLong("part_inventory_id"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
