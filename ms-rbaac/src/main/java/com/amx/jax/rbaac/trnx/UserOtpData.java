/**
 * 
 */
package com.amx.jax.rbaac.trnx;

import java.io.Serializable;
import java.math.BigDecimal;

import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;
import com.amx.jax.rbaac.dbmodel.FSEmployee;

/**
 * The Class UserOtpData.
 *
 * @author abhijeet
 */
public class UserOtpData implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 7221219115941595018L;

	/** The auth transaction id. */
	private String authTransactionId;

	/** The employee. */
	private FSEmployee employee;

	private BigDecimal partnerEmployeeId;

	/** The login type. */
	private LOGIN_TYPE loginType;

	/** The otp data. */
	private OtpData otpData;

	/** The partner otp data. */
	private OtpData partnerOtpData;

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
	public FSEmployee getEmployee() {
		return employee;
	}

	/**
	 * Sets the employee.
	 *
	 * @param employee
	 *            the new employee
	 */
	public void setEmployee(FSEmployee employee) {
		this.employee = employee;
	}

	public BigDecimal getPartnerEmployeeId() {
		return partnerEmployeeId;
	}

	public void setPartnerEmployeeId(BigDecimal partnerEmployeeId) {
		this.partnerEmployeeId = partnerEmployeeId;
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
	 * Gets the partner otp data.
	 *
	 * @return the partner otp data
	 */
	public OtpData getPartnerOtpData() {
		return partnerOtpData;
	}

	/**
	 * Sets the partner otp data.
	 *
	 * @param partnerOtpData
	 *            the new partner otp data
	 */
	public void setPartnerOtpData(OtpData partnerOtpData) {
		this.partnerOtpData = partnerOtpData;
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

	/**
	 * Increment otp attempt count.
	 */
	public void incrementOtpAttemptCount() {
		this.otpAttemptCount++;
	}

}
