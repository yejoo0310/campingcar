package service;

import java.util.List;

import entity.Campingcar;
import entity.CarExternalMaintenanceCenter;
import entity.CarExternalMaintenanceRecord;
import entity.CarSelfMaintenanceRecord;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import repository.CarExternalMaintenanceCenterRepository;
import repository.CarExternalMaintenanceRecordRepository;
import repository.CarSelfMaintenanceRecordRepository;
import repository.PartInventoryRepository;
import util.ApplicationException;

public class MaintenanceService {
	private final CampingcarRepository campingcarRepository;
	private final CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository;
	private final CampingcarRentalRepository campingcarRentalRepository;
	private final CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository;
	private final CarSelfMaintenanceRecordRepository carSelfMaintenanceRecordRepository;
	private final PartInventoryRepository partInventoryRepository;

	public MaintenanceService(CampingcarRepository campingcarRepository,
			CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository,
			CampingcarRentalRepository campingcarRentalRepository,
			CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository,
			CarSelfMaintenanceRecordRepository carSelfMaintenanceRecordRepository,
			PartInventoryRepository partInventoryRepository) {
		this.campingcarRepository = campingcarRepository;
		this.carExternalMaintenanceCenterRepository = carExternalMaintenanceCenterRepository;
		this.campingcarRentalRepository = campingcarRentalRepository;
		this.carExternalMaintenanceRecordRepository = carExternalMaintenanceRecordRepository;
		this.carSelfMaintenanceRecordRepository = carSelfMaintenanceRecordRepository;
		this.partInventoryRepository = partInventoryRepository;
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

	public List<Campingcar> getCampingcars() {
		try {
			return campingcarRepository.findAll();
		} catch (Exception e) {
			return List.of();
		}
	}

	public List<CarExternalMaintenanceRecord> getExternalMaintenanceRecords(Long campingcarId) {
		try {
			return carExternalMaintenanceRecordRepository.findByCampingcarId(campingcarId);
		} catch (Exception e) {
			e.printStackTrace();
			return List.of();
		}
	}

	public List<CarSelfMaintenanceRecord> getSelfMaintenanceRecords(Long campingcarId) {
		try {
			return carSelfMaintenanceRecordRepository.findByCampingcarId(campingcarId);
		} catch (Exception e) {
			return List.of();
		}
	}

}
