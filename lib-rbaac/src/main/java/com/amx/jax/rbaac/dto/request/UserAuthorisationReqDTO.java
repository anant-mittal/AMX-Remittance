/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The Class UserAuthorisationReqDTO.
 *
 * @author abhijeet
 */
public class UserAuthorisationReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4920057845794220028L;

	/** The employee no. */
	@NotBlank(message = "Employee Number Can not be Null or Empty")
	private String employeeNo;
	
	/** The m otp hash. */
	@NotBlank(message = "M-Otp Can not be Null or Empty")
	private String mOtp;
	
	/** The e otp. */
	private String eOtp;
	
	/** The ip address. */
	@NotBlank(message = "Ip Address Can not be Null or Empty")
	private String ipAddress;
	
	/** The device id. */
	private String deviceId;
	
	/** The transaction id. */
	private String transactionId;

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
	 * @param employeeNo the new employee no
	 */
	public void setEmployeeNo(String employeeNo) {
		this.employeeNo = employeeNo;
	}

	/**
	 * Gets the m otp.
	 *
	 * @return the m otp.
	 */
	public String getmOtp() {
		return mOtp;
	}

	/**
	 * Sets the m otp hash.
	 *
	 * @param mOtpHash the new m otp hash
	 */
	public void setmOtp(String mOtp) {
		this.mOtp = mOtp;
	}

	/**
	 * Gets the e otp.
	 *
	 * @return the e otp
	 */
	public String geteOtp() {
		return eOtp;
	}

	/**
	 * Sets the e otp.
	 *
	 * @param eOtp the new e otp
	 */
	public void seteOtp(String eOtp) {
		this.eOtp = eOtp;
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
	 * @param ipAddress the new ip address
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
	 * @param deviceId the new device id
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Gets the transaction id.
	 *
	 * @return the transaction id
	 */
	public String getTransactionId() {
		return transactionId;
	}

	/**
	 * Sets the transaction id.
	 *
	 * @param transactionId the new transaction id
	 */
	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

}
