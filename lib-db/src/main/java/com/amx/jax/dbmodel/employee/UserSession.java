package com.amx.jax.dbmodel.employee;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "USER_SESSION")
public class UserSession implements Serializable {

	private static final long serialVersionUID = -4842537006134875403L;

	@Id
	@Column(name = "USER_NAME")
	String userName;

	@Column(name = "LOGIN_TIME")
	Date loginTime;

	@Column(name = "USER_TOKEN")
	BigDecimal userToken;

	@Column(name = "IP_ADDRESS")
	String ipAddress;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	public BigDecimal getUserToken() {
		return userToken;
	}

	public void setUserToken(BigDecimal userToken) {
		this.userToken = userToken;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

}
