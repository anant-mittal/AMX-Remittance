package com.amx.jax.dbmodel.tpc;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_TPC_CLIENT_MASTER")
public class TpcClientMaster {

	@Id
	@Column(name = "TPC_CLIENT_MASTER_ID")
	int id;

	@Column(name = "CLIENT_ID")
	String clientId;

	@Column(name = "CLIENT_SECRET")
	String clientSecret;

	@Column(name = "COUNTRY_BRANCH_ID")
	BigDecimal countryBranchId;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

}
