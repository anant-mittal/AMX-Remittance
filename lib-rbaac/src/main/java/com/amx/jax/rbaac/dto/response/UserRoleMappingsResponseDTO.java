package com.amx.jax.rbaac.dto.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The Class UserRoleMappingsResponseDTO.
 */
public class UserRoleMappingsResponseDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7204846177787386980L;

	/** The employee role id map. */
	Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap;

	/** The role info map. */
	Map<BigDecimal, RoleResponseDTO> roleInfoMap;

	/** The employee info map. */
	Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap;

	/** The ip addr. */
	@NotBlank(message = "IP Address Can not be Blank.")
	private String ipAddr;

	/** The device id. */
	private String deviceId;

	/**
	 * Gets the user role mapping info map.
	 *
	 * @return the user role mapping info map
	 */
	public Map<BigDecimal, UserRoleMappingDTO> getUserRoleMappingInfoMap() {
		return userRoleMappingInfoMap;
	}

	/**
	 * Sets the user role mapping info map.
	 *
	 * @param userRoleMappingInfoMap
	 *            the user role mapping info map
	 */
	public void setUserRoleMappingInfoMap(Map<BigDecimal, UserRoleMappingDTO> userRoleMappingInfoMap) {
		this.userRoleMappingInfoMap = userRoleMappingInfoMap;
	}

	/**
	 * Gets the role info map.
	 *
	 * @return the role info map
	 */
	public Map<BigDecimal, RoleResponseDTO> getRoleInfoMap() {
		return roleInfoMap;
	}

	/**
	 * Sets the role info map.
	 *
	 * @param roleInfoMap
	 *            the role info map
	 */
	public void setRoleInfoMap(Map<BigDecimal, RoleResponseDTO> roleInfoMap) {
		this.roleInfoMap = roleInfoMap;
	}

	/**
	 * Gets the employee info map.
	 *
	 * @return the employee info map
	 */
	public Map<BigDecimal, EmployeeDetailsDTO> getEmployeeInfoMap() {
		return employeeInfoMap;
	}

	/**
	 * Sets the employee info map.
	 *
	 * @param employeeInfoMap
	 *            the employee info map
	 */
	public void setEmployeeInfoMap(Map<BigDecimal, EmployeeDetailsDTO> employeeInfoMap) {
		this.employeeInfoMap = employeeInfoMap;
	}

	/**
	 * Gets the ip addr.
	 *
	 * @return the ip addr
	 */
	public String getIpAddr() {
		return ipAddr;
	}

	/**
	 * Sets the ip addr.
	 *
	 * @param ipAddr
	 *            the new ip addr
	 */
	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	/**
	 * Gets the device id.
	 *
	 * @return the device id
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets the device id.
	 *
	 * @param deviceId
	 *            the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
