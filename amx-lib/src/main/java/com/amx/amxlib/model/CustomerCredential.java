package com.amx.amxlib.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.amx.amxlib.model.CustomerModelInterface.ICustomerCredential;

public class CustomerCredential implements Serializable, ICustomerCredential {

	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min=6, max=12)
	@Pattern(regexp = "^\\S+", message="The User ID you have entered is not valid. Please enter the User ID with minimum 6 alphanumeric characters.")
	String loginId;

	@NotNull
	String password;

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
}
