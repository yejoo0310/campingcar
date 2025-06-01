package service;

import java.util.List;

import repository.CampingcarCompanyRepository;
import repository.CampingcarRentalRepository;
import repository.CampingcarRepository;
import repository.CarExternalMaintenanceCenterRepository;
import repository.CarExternalMaintenanceRecordRepository;
import repository.CarSelfMaintenanceRecordRepository;
import repository.CustomerRepository;
import repository.EmployeeRepository;
import repository.NativeQueryRepository;
import repository.PartInventoryRepository;
import util.ApplicationException;

public class AdminService {
	public static final List<String> SUPPORTED_TABLES = List.of("customer", "part_inventory", "campingcar",
			"campingcar_company", "campingcar_rental", "employee", "car_external_maintenance_center",
			"car_external_maintenance_record", "car_self_maintenance_record");

	private final CustomerRepository customerRepository;
	private final PartInventoryRepository partInventoryRepository;
	private final CampingcarRepository campingcarRepository;
	private final CampingcarCompanyRepository campingcarCompanyRepository;
	private final CampingcarRentalRepository campingcarRentalRepository;
	private final EmployeeRepository employeeRepository;
	private final CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository;
	private final CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository;
	private final CarSelfMaintenanceRecordRepository carSelfMaintenanceRecordRepository;

	private final NativeQueryRepository nativeQueryRepository;

	public AdminService(CustomerRepository customerRepository, PartInventoryRepository partInventoryRepository,
			CampingcarRepository campingcarRepository, CampingcarCompanyRepository campingcarCompanyRepository,
			CampingcarRentalRepository campingcarRentalRepository, EmployeeRepository employeeRepository,
			CarExternalMaintenanceCenterRepository carExternalMaintenanceCenterRepository,
			CarExternalMaintenanceRecordRepository carExternalMaintenanceRecordRepository,
			CarSelfMaintenanceRecordRepository carSelfMaintenanceRecordRepository,
			NativeQueryRepository nativeRepsiRepository) {
		this.customerRepository = customerRepository;
		this.partInventoryRepository = partInventoryRepository;
		this.campingcarRepository = campingcarRepository;
		this.campingcarCompanyRepository = campingcarCompanyRepository;
		this.campingcarRentalRepository = campingcarRentalRepository;
		this.employeeRepository = employeeRepository;
		this.carExternalMaintenanceCenterRepository = carExternalMaintenanceCenterRepository;
		this.carExternalMaintenanceRecordRepository = carExternalMaintenanceRecordRepository;
		this.carSelfMaintenanceRecordRepository = carSelfMaintenanceRecordRepository;

		this.nativeQueryRepository = nativeRepsiRepository;
	}

	/**
	 * 관리자가 볼 수 있는 테이블 이름 리스트를 반환합니다.
	 */
	public List<String> showTables() {
		return SUPPORTED_TABLES;
	}

	/**
	 * 지정된 테이블의 전체 row를 반환합니다. 결과는 List<?>(각 엔티티의 리스트)로 반환됩니다.
	 * 
	 * @param tableName SUPPORTED_TABLES의 값이어야 함
	 */
	public List<?> findAllRows(String tableName) {
		if (!SUPPORTED_TABLES.contains(tableName)) {
			throw ApplicationException.of("지원하지 않는 테이블입니다.");
		}
		try {
			switch (tableName) {
			case "customer":
				return customerRepository.findAll();
			case "part_inventory":
				return partInventoryRepository.findAll();
			case "campingcar":
				return campingcarRepository.findAll();
			case "campingcar_company":
				return campingcarCompanyRepository.findAll();
			case "campingcar_rental":
				return campingcarRentalRepository.findAll();
			case "employee":
				return employeeRepository.findAll();
			case "car_external_maintenance_center":
				return carExternalMaintenanceCenterRepository.findAll();
			case "car_external_maintenance_record":
				return carExternalMaintenanceRecordRepository.findAll();
			case "car_self_maintenance_record":
				return carSelfMaintenanceRecordRepository.findAll();
			default:
				throw ApplicationException.of("지원하지 않는 테이블입니다.");
			}
		} catch (Exception e) {
			throw ApplicationException.of("테이블 데이터 조회 실패");
		}
	}

	public Object exec(String command, String table, String clause) {
		// 테이블 화이트리스트 검증
		if (!SUPPORTED_TABLES.contains(table.toLowerCase())) {
			throw ApplicationException.of("지원하지 않는 테이블");
		}

		// SQL 문자열 조립
		String sql;
		switch (command.toLowerCase()) {
		case "select":
			sql = "SELECT * FROM " + table + " " + clause; // clause: "WHERE id=..."
			break;
		case "delete":
			sql = "DELETE FROM " + table + " " + clause; // clause: "WHERE id=..."
			break;
		case "update":
			sql = "UPDATE " + table + " SET " + clause; // clause: "col1=..., col2=... WHERE id=..."
			break;
		case "insert":
			sql = "INSERT INTO " + table + " " + clause; // clause: "(col1,col2) VALUES (...,...)"
			break;
		default:
			throw ApplicationException.of("지원하지 않는 명령어");
		}

		// Repository 호출
		System.out.println("실행된 SQL: " + sql);
		return nativeQueryRepository.execute(sql);
	}
}
