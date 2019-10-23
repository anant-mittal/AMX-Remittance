package com.amx.jax.model.request;

import java.sql.Timestamp;

public class LastLoginDetails {

	private Timestamp logoutTime;
	private Timestamp loginTime;


	public Timestamp getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(Timestamp logoutTime) {
		this.logoutTime = logoutTime;
	}

	public Timestamp getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Timestamp loginTime) {
		this.loginTime = loginTime;
	}

	
}
