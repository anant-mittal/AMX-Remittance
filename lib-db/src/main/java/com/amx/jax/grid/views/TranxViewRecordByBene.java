package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.grid.GridGroup;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "VW_KIBANA_TRNX")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TranxViewRecordByBene implements Serializable {

	private static final long serialVersionUID = 1L;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Id
	// Bene
	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	@Column(name = "BENE_BANKBRANCH_ID")
	private BigDecimal beneBankBranchId;

	@Column(name = "BENE_BANKBRANCH_NAME")
	private String beneBankBranchName;

	@GridGroup("sum(local_tranx_amount)")
	@Column(name = "LOCAL_TRANX_AMOUNT_TOTAL")
	private BigDecimal totalLocalTranxAmount;

	public BigDecimal getBeneBankId() {
		return beneBankId;
	}

	public void setBeneBankId(BigDecimal beneBankId) {
		this.beneBankId = beneBankId;
	}

	public String getBeneBankName() {
		return beneBankName;
	}

	public void setBeneBankName(String beneBankName) {
		this.beneBankName = beneBankName;
	}

	public BigDecimal getBeneBankBranchId() {
		return beneBankBranchId;
	}

	public void setBeneBankBranchId(BigDecimal beneBankBranchId) {
		this.beneBankBranchId = beneBankBranchId;
	}

	public String getBeneBankBranchName() {
		return beneBankBranchName;
	}

	public void setBeneBankBranchName(String beneBankBranchName) {
		this.beneBankBranchName = beneBankBranchName;
	}

	public BigDecimal getCustmerId() {
		return custmerId;
	}

	public void setCustmerId(BigDecimal custmerId) {
		this.custmerId = custmerId;
	}

	public BigDecimal getTotalLocalTranxAmount() {
		return totalLocalTranxAmount;
	}

	public void setTotalLocalTranxAmount(BigDecimal totalLocalTranxAmount) {
		this.totalLocalTranxAmount = totalLocalTranxAmount;
	}

}
