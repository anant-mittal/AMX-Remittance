package com.amx.jax.grid.views;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.amx.jax.grid.GridViewRecord;

@Entity
@Table(name = "USER_SESSION")
public class UserSessionRecord implements GridViewRecord {

	private static final long serialVersionUID = 412176528892609487L;

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

	private Integer totalRecords;

	private Integer rn;

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}

}
