package service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import entity.Campingcar;
import entity.CampingcarRental;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import util.ApplicationException;

public class RentalService {
	private final CampingcarRepository campingcarRepository;
	private final CampingcarRentalRepository campingcarRentalRepository;

	public RentalService(CampingcarRepository campingcarRepository,
			CampingcarRentalRepository campingcarRentalRepository) {
		this.campingcarRepository = campingcarRepository;
		this.campingcarRentalRepository = campingcarRentalRepository;
	};

	public List<Campingcar> getCampingcars() {
		try {
			return campingcarRepository.findAll();
		} catch (Exception e) {
			return List.of();
		}
	}

	public List<LocalDate> getDisableDates(Long campingcarId) {
		try {
			List<CampingcarRental> campingcarRentals = campingcarRentalRepository.findByCampingcarId(campingcarId);
			List<LocalDate> disableDates = new ArrayList<>();

			for (CampingcarRental rental : campingcarRentals) {
				LocalDate startDate = rental.getStartDate().toLocalDate();
				int period = rental.getPeriodDays();

				for (int i = 0; i < period; i++) {
					LocalDate currentDate = startDate.plusDays(i);
					disableDates.add(currentDate);
				}
			}

			return disableDates;
		} catch (Exception e) {
			return List.of();
		}
	}

	public CampingcarRental registRental(LocalDate startDate, LocalDate endDate, Long customerId, Long campingcarId,
			Long campingcarCompanyId) {
		int periods = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;
		CampingcarRental campingcarRental = new CampingcarRental(null, Date.valueOf(startDate), periods,
				periods * 10000, Date.valueOf(startDate.minusDays(1)), "", null, customerId, campingcarId,
				campingcarCompanyId);

		try {
			return campingcarRentalRepository.save(campingcarRental);
		} catch (Exception e) {
			throw ApplicationException.of("저장실패");
		}
	}

	public List<CampingcarRental> getRentalHistory(Long customerId) {
		try {
			return campingcarRentalRepository.findByCustomerId(customerId);
		} catch (Exception e) {
			throw ApplicationException.of("대여기록을 가져올 수 없음");
		}
	}
}
