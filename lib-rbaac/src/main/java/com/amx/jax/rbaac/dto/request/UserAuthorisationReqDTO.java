/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;

/**
 * The Class UserAuthorisationReqDTO.
 *
 * @author abhijeet
 */
public class UserAuthorisationReqDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4920057845794220028L;

	/** The employee no. */
	private String employeeNo;
	
	/** The m otp hash. */
	private String mOtpHash;
	
	/** The e otp hash. */
	private String eOtpHash;
	
	/** The ip address. */
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
	 * Gets the m otp hash.
	 *
	 * @return the m otp hash
	 */
	public String getmOtpHash() {
		return mOtpHash;
	}

	/**
	 * Sets the m otp hash.
	 *
	 * @param mOtpHash the new m otp hash
	 */
	public void setmOtpHash(String mOtpHash) {
		this.mOtpHash = mOtpHash;
	}

	/**
	 * Gets the e otp hash.
	 *
	 * @return the e otp hash
	 */
	public String geteOtpHash() {
		return eOtpHash;
	}

	/**
	 * Sets the e otp hash.
	 *
	 * @param eOtpHash the new e otp hash
	 */
	public void seteOtpHash(String eOtpHash) {
		this.eOtpHash = eOtpHash;
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
