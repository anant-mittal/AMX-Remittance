package com.amx.jax.dbmodel.remittance;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "VW_EX_DEBIT_AUTH")
public class StaffAuthorizationView {
	private String userName;
	private String password;
	private BigDecimal id;

	@Id
	@Column(name = "IDNO")
	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "USER_NAME")
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	

	public StaffAuthorizationView() {
		super();
	}

	public StaffAuthorizationView(String userName, String password, BigDecimal id) {
		super();
		this.userName = userName;
		this.password = password;
		this.id = id;
	}
}
