package com.amx.jax;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.libjax.model.CustomerModelInterface.ICustomerCredential;

public class CustomerCredential implements Serializable, ICustomerCredential {

	private static final long serialVersionUID = 1L;

	@NotNull(message="User Id may not be null")
	@Size(min=6, max=12, message="User ID should be between 6 and 12 characters")
	@Pattern(regexp = "^\\S+", message="The User ID you have entered is not valid. Please enter the User ID with minimum 6 alphanumeric characters.")
	String loginId;

	@NotNull(message="Password may not be null")
	String password;
	
	String referralId;

	public CustomerCredential() {
		super();
	}

	public CustomerCredential(String loginId, String password) {
		super();
		this.loginId = loginId;
		this.password = password;
	}

	@Override
	public String getLoginId() {
		return loginId;
	}

	@Override
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public void setPassword(String password) {
		this.password = password;
	}

	public String getReferralId() {
		return referralId;
	}

	public void setReferralId(String referralId) {
		this.referralId = referralId;
	}
		
}
