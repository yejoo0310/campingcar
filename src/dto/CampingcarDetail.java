package dto;

import entity.Campingcar;
import entity.CampingcarCompany;
import util.ToString;

public class CampingcarDetail {
	private final Campingcar campingcar;
	private final CampingcarCompany company;

	public CampingcarDetail(Campingcar campingcar, CampingcarCompany company) {
		this.campingcar = campingcar;
		this.company = company;
	}

	public Campingcar getCampingcar() {
		return campingcar;
	}

	public CampingcarCompany getCompany() {
		return company;
	}

	@Override
	public String toString() {
		return ToString.autoToString(this);
	}
}
