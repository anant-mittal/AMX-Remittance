/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;

import com.amx.jax.rbaac.constants.RbaacServiceConstants;

/**
 * The Class UserAuthInitReqDTO.
 *
 * @author abhijeet
 */
public class UserAuthInitReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;

	/** The employee no. */
	private String employeeNo;

	/** The identity. */
	private String identity;

	/** The ip address. */
	private String ipAddress;

	/** The device id. */
	private String deviceId;

	/** The device type. */
	private RbaacServiceConstants.DEVICE_TYPE deviceType;

	/**
	 * Gets the employee no.
	 *
	 * @return the employee no
	 */
	public String getEmployeeNo() {
		return employeeNo;
	}

	/**
	 * Sets the employee no.
	 *
	 * @param employeeNo
	 *            the new employee no
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * Gets the identity.
	 *
	 * @return the identity
	 */
	public String getIdentity() {
		return identity;
	}

	/**
	 * Sets the identity.
	 *
	 * @param identity
	 *            the new identity
	 */
	public void setIdentity(String identity) {
		this.identity = identity;
	}

	/**
	 * Gets the ip address.
	 *
	 * @return the ip address
	 */
	public String getIpAddress() {
		return ipAddress;
	}

	/**
	 * Sets the ip address.
	 *
	 * @param ipAddress
	 *            the new ip address
	 */
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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

	/**
	 * Gets the device type.
	 *
	 * @return the device type
	 */
	public RbaacServiceConstants.DEVICE_TYPE getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(RbaacServiceConstants.DEVICE_TYPE deviceType) {
		this.deviceType = deviceType;
	}

}