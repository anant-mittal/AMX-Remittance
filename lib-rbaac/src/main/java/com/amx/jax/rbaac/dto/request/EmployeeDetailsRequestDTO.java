package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;
import java.util.List;

import org.hibernate.validator.constraints.NotBlank;

import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;

/**
 * The Class EmployeeDetailsRequestDTO.
 */
public class EmployeeDetailsRequestDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7767061362173332318L;

	/** The employee details DTO list. */
	private List<EmployeeDetailsDTO> employeeDetailsDTOList;

	/** The ip addr. */
	@NotBlank(message = "IP Address Can not be Blank.")
	private String ipAddr;

	/** The device id. */
	private String deviceId;

	/**
	 * Gets the employee details DTO list.
	 *
	 * @return the employee details DTO list
	 */
	public List<EmployeeDetailsDTO> getEmployeeDetailsDTOList() {
		return employeeDetailsDTOList;
	}

	/**
	 * Sets the employee details DTO list.
	 *
	 * @param employeeDetailsDTOList the new employee details DTO list
	 */
	public void setEmployeeDetailsDTOList(List<EmployeeDetailsDTO> employeeDetailsDTOList) {
		this.employeeDetailsDTOList = employeeDetailsDTOList;
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
	 * @param ipAddr the new ip addr
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
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
