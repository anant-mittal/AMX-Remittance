/**
 * 
 */
package com.amx.jax.rbaac.trnx;

import java.io.Serializable;

import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.dbmodel.Employee;

/**
 * The Class UserOtpData.
 *
 * @author abhijeet
 */
public final class UserOtpData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7221219115941595018L;

	/** The auth transaction id. */
	private String authTransactionId;

	/** The employee. */
	private Employee employee;

	/** The otp data. */
	private OtpData otpData;

	/** The otp attempt count. */
	private int otpAttemptCount;

	/**
	 * Gets the auth transaction id.
	 *
	 * @return the auth transaction id
	 */
	public String getAuthTransactionId() {
		return authTransactionId;
	}

	/**
	 * Sets the auth transaction id.
	 *
	 * @param authTransactionId
	 *            the new auth transaction id
	 */
	public void setAuthTransactionId(String authTransactionId) {
		this.authTransactionId = authTransactionId;
	}

	/**
	 * Gets the employee.
	 *
	 * @return the employee
	 */
	public Employee getEmployee() {
		return employee;
	}

	/**
	 * Sets the employee.
	 *
	 * @param employee
	 *            the new employee
	 */
	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	/**
	 * Gets the otp data.
	 *
	 * @return the otp data
	 */
	public OtpData getOtpData() {
		return otpData;
	}

	/**
	 * Sets the otp data.
	 *
	 * @param otpData
	 *            the new otp data
	 */
	public void setOtpData(OtpData otpData) {
		this.otpData = otpData;
	}

	/**
	 * Gets the otp attempt count.
	 *
	 * @return the otp attempt count
	 */
	public int getOtpAttemptCount() {
		return otpAttemptCount;
	}

	/**
	 * Sets the otp attempt count.
	 *
	 * @param otpAttemptCount
	 *            the new otp attempt count
	 */
	public void setOtpAttemptCount(int otpAttemptCount) {
		this.otpAttemptCount = otpAttemptCount;
	}

	/**
	 * Instantiates a new user otp data.
	 */
	public UserOtpData() {
		this.otpAttemptCount = 0;
	}
	
	public void incrementOtpAttemptCount() {
		this.otpAttemptCount++;
	}

}
