package entity;

import java.sql.ResultSet;
import java.sql.SQLException;

import util.Column;
import util.ToString;

/**
 * 캠핑카 업체 엔티티
 */
public class CampingcarCompany {
	@Column("id")
	private Long id;

	@Column("name")
	private String name;

	@Column("address")
	private String address;

	@Column("phone_number") // 칼럼 맵핑
	private String phoneNumber;

	/** 기본 생성자(필수) ― RowMapper가 리플렉션으로 인스턴스 생성할 때 사용 */
	public CampingcarCompany() {
	}

	/** 풀 파라미터 생성자 ― Repository에서 직접 new 할 때 편의용 */
	public CampingcarCompany(Long id, String name, String address, String phoneNumber) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.phoneNumber = phoneNumber;
	}

	public Long getId() {
		return id;
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

	public void setId(Long id) {
		this.id = id;
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

	public static CampingcarCompany fromResultSet(ResultSet rs) throws SQLException {
		return new CampingcarCompany(rs.getLong("id"), rs.getString("name"), rs.getString("address"),
				rs.getString("phone_number"));
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
