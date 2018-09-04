/**
 * 
 */
package com.amx.jax.util;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import javax.persistence.Column;

import com.amx.jax.rbaac.dbmodel.Employee;
import com.amx.jax.rbaac.dbmodel.Role;
import com.amx.jax.rbaac.dbmodel.UserRoleMapping;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.rbaac.dto.response.RoleResponseDTO;
import com.amx.jax.rbaac.dto.response.UserRoleMappingDTO;
import com.amx.utils.JsonUtil;

/**
 * @author abhijeet
 *
 */
public final class ObjectConverter {

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
		empDetail.setRoleId(new BigDecimal("1"));

		return empDetail;
	}

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

	public static UserRoleMappingDTO convertUrmToUrmDTO(UserRoleMapping userRoleMapping) {

		UserRoleMappingDTO userRoleMappingDTO = new UserRoleMappingDTO();

		userRoleMapping.setId(userRoleMapping.getId());
		userRoleMapping.setEmployeeId(userRoleMapping.getEmployeeId());
		userRoleMapping.setRoleId(userRoleMapping.getRoleId());
		userRoleMapping.setSuspended(userRoleMapping.getSuspended());
		userRoleMapping.setFlags(userRoleMapping.getFlags());
		userRoleMapping.setInfo(userRoleMapping.getInfo());
		userRoleMapping.setCreatedDate(userRoleMapping.getCreatedDate());
		userRoleMapping.setUpdatedDate(userRoleMapping.getUpdatedDate());

		return userRoleMappingDTO;

	}

}
