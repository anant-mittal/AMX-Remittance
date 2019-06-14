/**
 * 
 */
package com.amx.jax.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import com.amx.jax.rbaac.constants.RbaacServiceConstants;
import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.utils.JsonUtil;

/**
 * The Class ObjectConverter.
 *
 * @author abhijeet
 */
public final class ObjectConverter {

	/**
	 * Convert employee to emp details DTO.
	 *
	 * @param employee
	 *            the employee
	 * @return the employee details DTO
	 */
	public static EmployeeDetailsDTO convertEmployeeToEmpDetailsDTO(Employee employee) {

		EmployeeDetailsDTO empDetail = new EmployeeDetailsDTO();

		empDetail.setCivilId(employee.getCivilId());
		empDetail.setCountryId(employee.getCountryId());

		empDetail.setDesignation(employee.getDesignation());
		empDetail.setEmail(employee.getEmail());
		empDetail.setEmployeeId(employee.getEmployeeId());
		empDetail.setEmployeeName(employee.getEmployeeName());
		empDetail.setEmployeeNumber(employee.getEmployeeNumber());
		empDetail.setLocation(employee.getLocation());
		empDetail.setTelephoneNumber(employee.getTelephoneNumber());
		empDetail.setUserName(employee.getUserName());

		empDetail.setStatus(employee.getStatus());

		if (employee.getIsActive().equalsIgnoreCase("Y") || employee.getIsActive().equalsIgnoreCase("YES")) {
			empDetail.setIsActive(Boolean.TRUE);
		} else {
			empDetail.setIsActive(Boolean.FALSE);
		}

		if (employee.getLockCount() != null
				&& employee.getLockCount().intValue() >= RbaacServiceConstants.EMPLOYEE_MAX_LOCK_COUNT) {
			empDetail.setIsLocked(Boolean.TRUE);
		} else {
			empDetail.setIsActive(Boolean.FALSE);
		}

		if (employee.getLastLogin() == null) {
			empDetail.setLastLoginDate(new Date());
		} else {
			empDetail.setLastLoginDate(employee.getLastLogin());
		}

		// Country Branch Details
		empDetail.setCountryBranchId(employee.getCountryBranch().getCountryBranchId());
		empDetail.setBranchId(employee.getCountryBranch().getBranchId());
		empDetail.setBranchName(employee.getCountryBranch().getBranchName());
		empDetail.setAreaCode(employee.getCountryBranch().getAreaCode());
		empDetail.setArea(employee.getCountryBranch().getArea());

		return empDetail;
	}

	/**
	 * Convert role to role response DTO.
	 *
	 * @param role
	 *            the role
	 * @return the role response DTO
	 */
	@SuppressWarnings("unchecked")
	public static RoleResponseDTO convertRoleToRoleResponseDTO(Role role) {

		RoleResponseDTO roleResponseDTO = new RoleResponseDTO();

		roleResponseDTO.setId(role.getId());
		roleResponseDTO.setRole(role.getRole());
		roleResponseDTO.setPermissionMap(JsonUtil.fromJson(role.getPermissionsJson(), Map.class));
		roleResponseDTO.setSuspended(role.getSuspended());
		roleResponseDTO.setFlags(role.getFlags());
		roleResponseDTO.setInfo(role.getInfo());
		roleResponseDTO.setCreatedDate(role.getCreatedDate());
		roleResponseDTO.setUpdatedDate(role.getUpdatedDate());

		return roleResponseDTO;

	}

	/**
	 * Convert urm to urm DTO.
	 *
	 * @param userRoleMapping
	 *            the user role mapping
	 * @return the user role mapping DTO
	 */
	public static UserRoleMappingDTO convertUrmToUrmDTO(UserRoleMapping userRoleMapping) {

		UserRoleMappingDTO userRoleMappingDTO = new UserRoleMappingDTO();

		userRoleMappingDTO.setId(userRoleMapping.getId());
		userRoleMappingDTO.setEmployeeId(userRoleMapping.getEmployeeId());
		userRoleMappingDTO.setRoleId(userRoleMapping.getRoleId());
		userRoleMappingDTO.setSuspended(userRoleMapping.getSuspended());
		userRoleMappingDTO.setFlags(userRoleMapping.getFlags());
		userRoleMappingDTO.setInfo(userRoleMapping.getInfo());
		userRoleMappingDTO.setCreatedDate(userRoleMapping.getCreatedDate());
		userRoleMappingDTO.setUpdatedDate(userRoleMapping.getUpdatedDate());

		return userRoleMappingDTO;

	}

	/**
	 * Convert urm DTO to urm.
	 *
	 * @param urMappingDTO
	 *            the ur mapping DTO
	 * @return the user role mapping
	 */
	public static UserRoleMapping convertUrmDTOToUserRoleMapping(UserRoleMappingDTO urMappingDTO) {

		UserRoleMapping userRoleMapping = new UserRoleMapping();

		userRoleMapping.setId(urMappingDTO.getId());
		userRoleMapping.setEmployeeId(urMappingDTO.getEmployeeId());
		userRoleMapping.setRoleId(urMappingDTO.getRoleId());
		userRoleMapping.setSuspended("N");
		userRoleMapping.setFlags(new BigDecimal(0));
		userRoleMapping.setInfo("{}");
		userRoleMapping.setCreatedDate(new Date());
		userRoleMapping.setUpdatedDate(new Date());

		return userRoleMapping;

	}

}
