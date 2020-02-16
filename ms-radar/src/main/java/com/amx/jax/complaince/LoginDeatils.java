package com.amx.jax.complaince;

import java.math.BigDecimal;

public class LoginDeatils {

	private String userName;
	private String password;
	private BigDecimal tokenLife;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BigDecimal getTokenLife() {
		return tokenLife;
	}
	public void setTokenLife(BigDecimal tokenLife) {
		this.tokenLife = tokenLife;
	}
	
	
}
