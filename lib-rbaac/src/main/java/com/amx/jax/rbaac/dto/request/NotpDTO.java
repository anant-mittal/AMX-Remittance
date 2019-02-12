/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;
import java.math.BigDecimal;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author lalittanwar
 *
 */
public class NotpDTO implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4920057845794220028L;

	/** The employee no. */
	@NotBlank(message = "Employee Id can not be Null or Empty")
	private BigDecimal employeeId;

	@NotBlank(message = "SAC cannot be Null or Empty")
	private String sac;

	private String otp;

	private boolean verfied;

	@NotBlank(message = "Ip Address Can not be Null or Empty")
	private String ipAddress;

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

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public String getSac() {
		return sac;
	}

	public void setSac(String sac) {
		this.sac = sac;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public boolean isVerfied() {
		return verfied;
	}

	public void setVerfied(boolean verfied) {
		this.verfied = verfied;
	}

}
