package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class CarExternalMaintenanceRecord {
	@Column("id")
	private Long id;

	@Column("description")
	private String description;

	@Column("date")
	private Date date;

	@Column("cost")
	private int cost;

	@Column("due_date")
	private Date dueDate;

	@Column("additional_notes")
	private String additionalNotes;

	@Column("campingcar_id")
	private Long campingcarId;

	@Column("customer_id")
	private Long customerId;

	@Column("car_external_maintenance_center_id")
	private Long carExternalMaintenanceCenterId;

	@Column("campigcar_company_id")
	private Long campingcarCompanyId;

	public CarExternalMaintenanceRecord() {
	}

	public CarExternalMaintenanceRecord(Long id, String description, Date date, int cost, Date dueDate,
			String additionalNotes, Long campingcarId, Long customerId, Long carExternalMaintenanceCenterId,
			Long campingcarCompanyId) {
		this.id = id;
		this.description = description;
		this.date = date;
		this.cost = cost;
		this.dueDate = dueDate;
		this.additionalNotes = additionalNotes;
		this.campingcarId = campingcarId;
		this.customerId = customerId;
		this.carExternalMaintenanceCenterId = carExternalMaintenanceCenterId;
		this.campingcarCompanyId = campingcarCompanyId;
	}

	public Long getId() {
		return id;
	}

	public String getDescription() {
		return description;
	}

	public Date getDate() {
		return date;
	}

	public int getCost() {
		return cost;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getAdditionalNotes() {
		return additionalNotes;
	}

	public Long getCampingcarId() {
		return campingcarId;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getCarExternalMaintenanceCenterId() {
		return carExternalMaintenanceCenterId;
	}

	public Long getCampingcarCompanyId() {
		return campingcarCompanyId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public void setAdditionalNote(String addtionalNotes) {
		this.additionalNotes = addtionalNotes;
	}

	public void setCampingcarId(Long campingcarId) {
		this.campingcarId = campingcarId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setCarExternalMaintenanceCenterId(Long carExternalMaintenanceCenterId) {
		this.carExternalMaintenanceCenterId = carExternalMaintenanceCenterId;
	}

	public void setCampingcarCompanyId(Long campingcarCompanyId) {
		this.campingcarCompanyId = campingcarCompanyId;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 CarExternalMaintenanceRecord 객체를 생성합니다.
	 */
	public static CarExternalMaintenanceRecord fromResultSet(ResultSet rs) throws SQLException {
		return new CarExternalMaintenanceRecord(rs.getLong("id"), rs.getString("description"), rs.getDate("date"),
				rs.getInt("cost"), rs.getDate("due_date"), rs.getString("additional_notes"),
				rs.getLong("campingcar_id"), rs.getLong("customer_id"),
				rs.getLong("car_external_maintenance_center_id"), rs.getLong("campingcar_company_id"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
