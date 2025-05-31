package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class CampingcarRental {
	@Column("id")
	private Long id;

	@Column("start_date")
	private Date startDate;

	@Column("period_days")
	private int periodDays;

	@Column("fee")
	private int fee;

	@Column("payment_due_date")
	private Date paymentDueDate;

	@Column("additional_payment_notes")
	private String additionalPaymentNotes;

	@Column("additional_fee")
	private int additionalFee;

	@Column("customer_id")
	private Long customerId;

	@Column("campingcar_id")
	private Long campingcarId;

	@Column("campingcar_company_id")
	private Long campingcarCompanyId;

	public CampingcarRental() {
	}

	public CampingcarRental(Long id, Date startDate, int periodDays, int fee, Date paymentDueDate,
			String additionalPaymentNotes, Integer addtionalFee, Long customerId, Long campingcarId,
			Long campingcarCompanyId) {
		this.id = id;
		this.startDate = startDate;
		this.periodDays = periodDays;
		this.fee = fee;
		this.paymentDueDate = paymentDueDate;
		this.additionalPaymentNotes = additionalPaymentNotes;
		this.additionalFee = addtionalFee == null ? 0 : additionalFee;
		this.customerId = customerId;
		this.campingcarId = campingcarId;
		this.campingcarCompanyId = campingcarCompanyId;
	}

	public Long getId() {
		return id;
	}

	public Date getStartDate() {
		return startDate;
	}

	public int getPeriodDays() {
		return periodDays;
	}

	public int getFee() {
		return fee;
	}

	public Date getPaymentDueDate() {
		return paymentDueDate;
	}

	public String getAdditionalPaymentNotes() {
		return additionalPaymentNotes;
	}

	public int getAdditionalFee() {
		return additionalFee;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public Long getCampingcarId() {
		return campingcarId;
	}

	public Long getCampingcarCompanyId() {
		return campingcarCompanyId;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public void setPeriodDays(int periodDays) {
		this.periodDays = periodDays;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}

	public void setPaymentDueDate(Date paymentDueDate) {
		this.paymentDueDate = paymentDueDate;
	}

	public void setAdditionalPaymentNotes(String additionalPaymentNotes) {
		this.additionalPaymentNotes = additionalPaymentNotes;
	}

	public void setAdditionalFee(int additionalFee) {
		this.additionalFee = additionalFee;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public void setCampingcarId(Long campingcarId) {
		this.campingcarId = campingcarId;
	}

	public void setCampingcarCompanyId(Long campingcarCompanyId) {
		this.campingcarCompanyId = campingcarCompanyId;
	}

	/**
	 * ResultSet의 현재 행을 기반으로 CampingcarRental 객체를 생성합니다.
	 */
	public static CampingcarRental fromResultSet(ResultSet rs) throws SQLException {
		return new CampingcarRental(rs.getLong("id"), rs.getDate("start_date"), rs.getInt("period_days"),
				rs.getInt("fee"), rs.getDate("payment_due_date"), rs.getString("additional_payment_notes"),
				rs.getInt("additional_fee"), rs.getLong("customer_id"), rs.getLong("campingcar_id"),
				rs.getLong("campingcar_company_id"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
