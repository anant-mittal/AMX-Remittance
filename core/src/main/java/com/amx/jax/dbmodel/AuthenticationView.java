package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "V_AUTH")
public class AuthenticationView {

	@Column(name = "AUTH_TYP")
	@Id
	BigDecimal authType;

	@Column(name = "FUDESC")
	String description;

	@Column(name = "AUTH_LIMIT")
	BigDecimal authLimit;

	public BigDecimal getAuthType() {
		return authType;
	}

	public void setAuthType(BigDecimal authType) {
		this.authType = authType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAuthLimit() {
		return authLimit;
	}

	public void setAuthLimit(BigDecimal authLimit) {
		this.authLimit = authLimit;
	}
}
