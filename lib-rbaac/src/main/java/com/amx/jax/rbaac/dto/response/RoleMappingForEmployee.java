package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

public class RoleMappingForEmployee implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7204846177787386980L;
	
	/** The ip addr. */
	@NotBlank(message = "IP Address Can not be Blank.")
	private String ipAddr;

	/** The device id. */
	private String deviceId;
	
	/** The employee info map. */
	Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap;
	
	/** The employee role id map. */
	Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap;
	
	/** The role info map. */
	Map<BigDecimal, RoleResponseDTO> roleInfoMap;
		
	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Map<BigDecimal, EmployeeDetailsDTO> getEmployeeInfoMap() {
		return employeeInfoMap;
	}

	public void setEmployeeInfoMap(Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap) {
		this.employeeInfoMap = employeeInfoMap;
	}

	public Map<BigDecimal, UserRoleMappingDTO> getUserRoleMappingInfoMap() {
		return userRoleMappingInfoMap;
	}

	public void setUserRoleMappingInfoMap(Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap) {
		this.userRoleMappingInfoMap = userRoleMappingInfoMap;
	}

	public Map<BigDecimal, RoleResponseDTO> getRoleInfoMap() {
		return roleInfoMap;
	}

	public void setRoleInfoMap(Map<BigDecimal, RoleResponseDTO> roleInfoMap) {
		this.roleInfoMap = roleInfoMap;
	}
	
	

}
