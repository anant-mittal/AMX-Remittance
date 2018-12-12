package com.amx.jax.rbaac.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rbaac.dao.EmployeeSystemDao;
import com.amx.jax.rbaac.dao.RbaacDao;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dto.request.EmployeeDetailsRequestDTO;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;

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

	public BoolRespModel createEmployeeSystemMapping(BigDecimal employeeId, Integer countryBranchSystemInventoryId) {
		return null;
		// TODO Auto-generated method stub
		
	}

	
}
