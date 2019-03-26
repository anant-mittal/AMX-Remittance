package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.BranchSystemDetail;
import com.amx.jax.dbmodel.EmployeeSystemsAssigned;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.constants.RbaacServiceConstants;
import com.amx.jax.rbaac.dao.BranchDetailDao;
import com.amx.jax.rbaac.dao.EmployeeSystemDao;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.error.RbaacServiceError;
import com.amx.jax.rbaac.exception.AuthServiceException;
import com.amx.utils.CollectionUtil;

/**
 * The Class UserAccountService.
 */
@Service
public class UserAccountService {
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerService.getLogger(UserRoleService.class);

	/** The login dao. */
	@Autowired
	RbaacDao rbaacDao;
	@Autowired
	EmployeeSystemDao employeeSystemDao ; 
	@Autowired
	BranchDetailDao branchDetailDao;

	/**
	 * Update employee.
	 *
	 * @param employeeDtoList
	 *            the employee dto list
	 * @return the list
	 */
	public List<EmployeeDetailsDTO> updateEmployee(EmployeeDetailsRequestDTO edRequestDTO) {

		List<EmployeeDetailsDTO> employeeDtoList = edRequestDTO.getEmployeeDetailsDTOList();

		synchronized (this) {

			if (employeeDtoList == null || employeeDtoList.isEmpty()) {
				return employeeDtoList;
			}

			List<Employee> updatedEmployeeList = new ArrayList<Employee>();

			for (EmployeeDetailsDTO empDetailsDTO : employeeDtoList) {

				Employee employee = rbaacDao.getEmployeeByEmployeeId(empDetailsDTO.getEmployeeId());

				if (employee != null) {

					// Take Decision : To be or not To be :)

					boolean isDirty = false;

					// Check if Active Info is changed.
					if (empDetailsDTO.getIsActive() != null) {

						if (empDetailsDTO.getIsActive()) {
							if (!employee.getIsActive().equalsIgnoreCase("Y")
									|| !employee.getIsActive().equalsIgnoreCase("YES")) {

								employee.setIsActive("Y");
								isDirty = true;
							}
						} else {
							if (employee.getIsActive().equalsIgnoreCase("Y")
									|| employee.getIsActive().equalsIgnoreCase("YES")) {

								employee.setIsActive("N");
								isDirty = true;
							}
						}

					}

					if (empDetailsDTO.getIsLocked() != null && !empDetailsDTO.getIsLocked()) {
						employee.setLockCount(new BigDecimal(0));
						isDirty = true;
					}

					if (isDirty) {
						updatedEmployeeList.add(employee);
					}

				}
			} // for

			if (updatedEmployeeList.isEmpty()) {
				LOGGER.info("No User / Employee Info is Updated");
			} else {
				rbaacDao.saveEmployees(updatedEmployeeList);
			}

		} // synchronized

		return employeeDtoList;

	}

	/**
	 * Maps employee with branch system
	 * 
	 * @param employeeId
	 * @param countryBranchSystemInventoryId
	 * @return
	 */
	public BoolRespModel createEmployeeSystemMapping(BigDecimal employeeId, BigDecimal countryBranchSystemInventoryId) {
		LOGGER.debug("creating employee system mapping. employeeId {}, countryBranchSystemInventoryId {}", employeeId,
				countryBranchSystemInventoryId);
		Employee employee = rbaacDao.getEmployeeByEmployeeId(employeeId);
		if (employee == null) {
			throw new AuthServiceException(RbaacServiceError.EMPLOYEE_NOT_FOUND, "No employee found with given id");
		}
		BranchSystemDetail branchSystemInventory = branchDetailDao
				.getBranchSystemDetailByInventoryId(countryBranchSystemInventoryId);
		if (branchSystemInventory == null) {
			throw new AuthServiceException(RbaacServiceError.BRANCH_SYSTEM_NOT_FOUND, "No branch system with given id");
		}
		List<EmployeeSystemsAssigned> existingRecords = employeeSystemDao.findByEmployeeIdAndCountryBranchId(employeeId,
				branchSystemInventory.getCountryBranchId(), branchSystemInventory.getSystemName());
		if (CollectionUtils.isNotEmpty(existingRecords)) {
			throw new AuthServiceException(RbaacServiceError.ALREADY_EXIST, "Mapping already exists");
		}
		EmployeeSystemsAssigned employeeSystemsAssigned = new EmployeeSystemsAssigned(employee.getEmployeeId(),
				employee.getEmployeeName(), branchSystemInventory.getCountryBranchId(),
				branchSystemInventory.getBranchName(), branchSystemInventory.getSystemName(), "JOMAX_ONLINE",
				RbaacServiceConstants.YES);
		employeeSystemDao.saveEmployeeSystemsAssigned(employeeSystemsAssigned);
		return new BoolRespModel(true);
	}

}
