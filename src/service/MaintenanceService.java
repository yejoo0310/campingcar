package service;

import java.util.List;

import entity.Campingcar;
import entity.CarExternalMaintenanceCenter;
import entity.CarExternalMaintenanceRecord;
import entity.CarSelfMaintenanceRecord;
import entity.PartInventory;
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
			e.printStackTrace();
			return List.of();
		}
	}

	public CarExternalMaintenanceRecord addRecord(CarExternalMaintenanceRecord carExternalMaintenanceRecord) {
		try {
			return carExternalMaintenanceRecordRepository.save(carExternalMaintenanceRecord);
		} catch (Exception e) {
			e.printStackTrace();
			throw ApplicationException.of("정비의뢰 실패");
		}
	}

	public List<Campingcar> getCampingcars() {
		try {
			return campingcarRepository.findAll();
		} catch (Exception e) {
			e.printStackTrace();
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
			e.printStackTrace();
			return List.of();
		}
	}

	public PartInventory getPartInventoryById(Long partInventoryId) {
		try {
			return partInventoryRepository.findById(partInventoryId)
					.orElseThrow(() -> ApplicationException.of("해당 부품 정보가 존재하지 않습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			throw ApplicationException.of("부품 정보 조회 중 오류가 발생했습니다.");
		}
	}

	public CarExternalMaintenanceCenter getExternalCenterById(Long centerId) {
		try {
			return carExternalMaintenanceCenterRepository.findById(centerId)
					.orElseThrow(() -> ApplicationException.of("해당 정비센터가 존재하지 않습니다."));
		} catch (Exception e) {
			e.printStackTrace();
			throw ApplicationException.of("정비센터 정보 조회 중 오류가 발생했습니다.");
		}
	}

}
