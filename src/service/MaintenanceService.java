package service;

import java.util.List;

import entity.CarExternalMaintenanceCenter;
import entity.CarExternalMaintenanceRecord;
import repository.CampingcarRentalRepository;
import repository.CarExternalMaintenanceCenterRepository;
import repository.CarExternalMaintenanceRecordRepository;
import util.ApplicationException;

public class MaintenanceService {
	private final CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository;
	private final CampingcarRentalRepository campingcarRentalRepository;
	private final CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository;

	public MaintenanceService(CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository,
			CampingcarRentalRepository campingcarRentalRepository,
			CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository) {
		this.carExternalMaintenanceCenterRepository = carExternalMaintenanceCenterRepository;
		this.campingcarRentalRepository = campingcarRentalRepository;
		this.carExternalMaintenanceRecordRepository = carExternalMaintenanceRecordRepository;
	}

	public List<CarExternalMaintenanceCenter> getExternalCenters() {
		try {
			return carExternalMaintenanceCenterRepository.findAll();
		} catch (Exception e) {
			return List.of();
		}
	}

	public CarExternalMaintenanceRecord addRecord(CarExternalMaintenanceRecord carExternalMaintenanceRecord) {
		try {
			return carExternalMaintenanceRecordRepository.save(carExternalMaintenanceRecord);
		} catch (Exception e) {
			throw ApplicationException.of("정비의뢰 실패");
		}
	}

}
