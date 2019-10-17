package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.grid.GridGroup;

@Entity
@Table(name = "JAX_VW_ONEVIEW_TRNX")
public class OneViewTrnxRecordByBene implements Serializable {

	private static final long serialVersionUID = -7797281945216488011L;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal beneCountryId;

	@Column(name = "BENE_COUNTRY_NAME")
	private String beneCountryName;

	@Id
	@Column(name = "BENEFICIARY_ID")
	private BigDecimal beneId;

	@Column(name = "BENEFICIARY_NAME")
	private String beneName;

	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	@Column(name = "BENE_BANK_BRANCH_NAME")
	private String beneBranchName;

	@Column(name = "FOREIGN_CURRENCY")
	private String foreignCurrency;
	
/*	@Column(name = "DOCUMENT_DATE")
	private Date trnxDate;
*/
	@GridGroup("sum(LOCAL_TRANX_AMOUNT)")
	@Column(name = "LOCAL_TRANX_AMOUNT_TOTAL")
	private BigDecimal totalLocalTranxAmount;

	@GridGroup("count(BENEFICIARY_ID)")
	@Column(name = "BENEFICIARY_ID_COUNT")
	private BigDecimal localTrnxCount;

	public BigDecimal getCustmerId() {
		return custmerId;
	}

	public void setCustmerId(BigDecimal custmerId) {
		this.custmerId = custmerId;
	}

	public BigDecimal getBeneCountryId() {
		return beneCountryId;
	}

	public void setBeneCountryId(BigDecimal beneCountryId) {
		this.beneCountryId = beneCountryId;
	}

	public String getBeneCountryName() {
		return beneCountryName;
	}

	public void setBeneCountryName(String beneCountryName) {
		this.beneCountryName = beneCountryName;
	}

	public BigDecimal getBeneId() {
		return beneId;
	}

	public void setBeneId(BigDecimal beneId) {
		this.beneId = beneId;
	}

	public String getBeneName() {
		return beneName;
	}

	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}

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

	public BigDecimal getTotalLocalTranxAmount() {
		return totalLocalTranxAmount;
	}

	public void setTotalLocalTranxAmount(BigDecimal totalLocalTranxAmount) {
		this.totalLocalTranxAmount = totalLocalTranxAmount;
	}

	
	public String getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public BigDecimal getLocalTrnxCount() {
		return localTrnxCount;
	}

	public void setLocalTrnxCount(BigDecimal localTrnxCount) {
		this.localTrnxCount = localTrnxCount;
	}

	public String getBeneBranchName() {
		return beneBranchName;
	}

	public void setBeneBranchName(String beneBranchName) {
		this.beneBranchName = beneBranchName;
	}
	
	

	/*public Date getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(Date trnxDate) {
		this.trnxDate = trnxDate;
	}*/
	
	

}
