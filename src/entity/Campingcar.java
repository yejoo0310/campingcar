package entity;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

public class Campingcar {

	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("number")
	private String number;

	@Column("seat_capacity")
	private int seatCapacity;

	@Column("image")
	private String image;

	@Column("information")
	private String information;

	@Column("registered_date")
	private Date registeredDate;

	@Column("campingcar_company_id")
	private Long campingcarCompanyId;

	public Campingcar() {
	}

	public Campingcar(Long id, String name, String number, int seatCapacity, String image, String information,
			Date registeredDate, Long campingcarCompanyId) {
		this.id = id;
		this.name = name;
		this.number = number;
		this.seatCapacity = seatCapacity;
		this.image = image;
		this.information = information;
		this.registeredDate = registeredDate;
		this.campingcarCompanyId = campingcarCompanyId;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getNumber() {
		return number;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public String getImage() {
		return image;
	}

	public String getInformation() {
		return information;
	}

	public Date getRegisteredDate() {
		return registeredDate;
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

	public void setNumber(String number) {
		this.number = number;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public void setInformation(String information) {
		this.information = information;
	}

	public void setRegisteredDate(Date registeredDate) {
		this.registeredDate = registeredDate;
	}

	public void setCampingcarCompanyId(Long campingcarCompanyId) {
		this.campingcarCompanyId = campingcarCompanyId;
	}

	public static Campingcar fromResultSet(ResultSet rs) throws SQLException {
		return new Campingcar(rs.getLong("id"), rs.getString("name"), rs.getString("number"),
				rs.getInt("seat_capacity"), rs.getString("image"), rs.getString("information"),
				rs.getDate("registered_date"), rs.getLong("campingcar_company_id"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}

	public String getDisplayName() {
		return String.format("%s (%d인승)", name, seatCapacity);
	}

}
