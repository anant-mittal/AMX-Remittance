package com.amx.amxlib.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CustomerCredential {

	@NotNull
	@Pattern(regexp = "^[A-Za-z0-9]+$")
	String loginId;

	@NotNull
	String password;

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
