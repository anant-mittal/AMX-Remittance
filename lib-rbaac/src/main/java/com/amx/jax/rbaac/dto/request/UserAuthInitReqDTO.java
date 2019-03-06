/**
 * 
 */
package com.amx.jax.rbaac.dto.request;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

import com.amx.jax.rbaac.constants.RbaacServiceConstants.LOGIN_TYPE;
import com.amx.jax.rbaac.dto.UserClientDto;

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

	/** The partner identity. */
	private String partnerIdentity;

	/** The login type. */
	@NotNull(message = "Login Type Can not be Null or Empty")
	private LOGIN_TYPE loginType = LOGIN_TYPE.SELF;

	/** Security Access Code for Self user. */
	@NotBlank(message = "Self SAC Can not be Null or Empty")
	private String selfSAC;

	/** Security Access Code for partner. */
	private String partnerSAC;

	/** The user client dto. */
	@NotNull(message = "User Client Info Can not be Null or Empty")
	private UserClientDto userClientDto;

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

	/**
	 * Gets the self SAC.
	 *
	 * @return the self SAC
	 */
	public String getSelfSAC() {
		return selfSAC;
	}

	/**
	 * Sets the self SAC.
	 *
	 * @param selfSAC
	 *            the new self SAC
	 */
	public void setSelfSAC(String selfSAC) {
		this.selfSAC = selfSAC;
	}

	/**
	 * Gets the partner SAC.
	 *
	 * @return the partner SAC
	 */
	public String getPartnerSAC() {
		return partnerSAC;
	}

	/**
	 * Sets the partner SAC.
	 *
	 * @param partnerSAC
	 *            the new partner SAC
	 */
	public void setPartnerSAC(String partnerSAC) {
		this.partnerSAC = partnerSAC;
	}

	/**
	 * Gets the user client dto.
	 *
	 * @return the user client dto
	 */
	public UserClientDto getUserClientDto() {
		return userClientDto;
	}

	/**
	 * Sets the user client dto.
	 *
	 * @param userClientDto
	 *            the new user client dto
	 */
	public void setUserClientDto(UserClientDto userClientDto) {
		this.userClientDto = userClientDto;
	}

	@Override
	public String toString() {
		return "UserAuthInitReqDTO [employeeNo=" + employeeNo + ", identity=" + identity + ", partnerIdentity="
				+ partnerIdentity + ", loginType=" + loginType + ", selfSAC=" + selfSAC + ", partnerSAC=" + partnerSAC
				+ ", userClientDto=" + userClientDto.toString() + "]";
	}

}
