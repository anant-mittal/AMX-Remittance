/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.amx.jax.dict.UserClient.DeviceType;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;

/**
 * The Class UserAuthInitReqDTO.
 *
 * @author abhijeet
 */
public class UserAuthInitReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -2594412775985894857L;

	/** The employee no. */
	@NotBlank(message = "Employee Number Can not be Null or Empty")
	private String employeeNo;

	/** The identity. */
	@NotBlank(message = "Employee Identity Can not be Null or Empty")
	private String identity;

	/** The ip address. */
	@NotBlank(message = "Ip Address Can not be Null or Empty")
	private String ipAddress;

	/** The device id. */
	private String deviceId;

	/** The terminal id. */
	private String terminalId;

	/** The partner identity. */
	private String partnerIdentity;

	/** The device type. */
	@NotNull(message = "Device Type Can not be Null or Empty")
	private DeviceType deviceType;

	/** The login type. */
	private LOGIN_TYPE loginType = LOGIN_TYPE.SELF;

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
	public DeviceType getDeviceType() {
		return deviceType;
	}

	/**
	 * Sets the device type.
	 *
	 * @param deviceType
	 *            the new device type
	 */
	public void setDeviceType(DeviceType deviceType) {
		this.deviceType = deviceType;
	}

	/**
	 * Gets the Terminal Id.
	 *
	 * @return the terminal id
	 */
	public String getTerminalId() {
		return terminalId;
	}

	/**
	 * Sets the Terminal Id.
	 *
	 * @param terminalId
	 *            the new terminal id
	 */
	public void setTerminalId(String terminalId) {
		this.terminalId = terminalId;
	}

	/**
	 * Gets the login type.
	 *
	 * @return the login type
	 */
	public LOGIN_TYPE getLoginType() {
		return loginType;
	}

	/**
	 * Sets the login type.
	 *
	 * @param loginType
	 *            the new login type
	 */
	public void setLoginType(LOGIN_TYPE loginType) {
		this.loginType = loginType;
	}

	/**
	 * Gets the partner identity.
	 *
	 * @return the partner identity
	 */
	public String getPartnerIdentity() {
		return partnerIdentity;
	}

	/**
	 * Sets the partner identity.
	 *
	 * @param partnerIdentity
	 *            the new partner identity
	 */
	public void setPartnerIdentity(String partnerIdentity) {
		this.partnerIdentity = partnerIdentity;
	}

}
