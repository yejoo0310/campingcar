package dto;

import entity.Campingcar;
import entity.CampingcarRental;

public class CampingcarRentalHistory {
	private CampingcarRental rental;
	private Campingcar campingcar;

	public CampingcarRentalHistory(CampingcarRental rental, Campingcar campingcar) {
		this.rental = rental;
		this.campingcar = campingcar;
	}

	public CampingcarRental getRental() {
		return rental;
	}

	public Campingcar getCampingcar() {
		return campingcar;
	}
}
