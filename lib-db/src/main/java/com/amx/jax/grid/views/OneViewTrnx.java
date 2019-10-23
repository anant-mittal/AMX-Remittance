package com.amx.jax.grid.views;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.grid.GridViewRecord;

@Entity
@Table(name = "JAX_VW_ONEVIEW_TRNX")
public class OneViewTrnx implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "REMITTANCE_TRANSACTION_ID")
	private BigDecimal id;

	@Column(name = "CUSTOMER_ID")
	private BigDecimal custmerId;

	// Bene
	@Column(name = "BENE_COUNTRY_ID")
	private BigDecimal beneCountryId;

	@Column(name = "BENE_COUNTRY_NAME")
	private String beneCountryName;

	@Column(name = "BENEFICIARY_ID")
	private BigDecimal beneId;

	@Column(name = "BENEFICIARY_NAME")
	private String beneName;

	@Column(name = "BENE_BANK_ID")
	private BigDecimal beneBankId;

	@Column(name = "BENE_BANK_NAME")
	private String beneBankName;

	// Trnx
	@Column(name = "LOCAL_TRANX_AMOUNT")
	private BigDecimal domTrnxAmount;

	@Column(name = "PAYMENT_MODES")
	private String paymentModes;

	@Column(name = "PAYOUT")
	private String serviceType;

	@Column(name = "CHANNEL")
	private String channel;

	@Column(name = "TRANSACTION_STATUS_NAME")
	private String trnxStatus;

	@Column(name = "DOCUMENT_DATE")
	private Date trnxDate;

	@Column(name = "TRNX_REF")
	private String trnxRef;

	// Branch
	@Column(name = "BRANCH_NAME")
	private String branchName;

	@Column(name = "FOREIGN_CURRENCY")
	private String foreignCurrency;

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

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

	public BigDecimal getDomTrnxAmount() {
		return domTrnxAmount;
	}

	public void setDomTrnxAmount(BigDecimal domTrnxAmount) {
		this.domTrnxAmount = domTrnxAmount;
	}

	public String getPaymentModes() {
		return paymentModes;
	}

	public void setPaymentModes(String paymentModes) {
		this.paymentModes = paymentModes;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getTrnxStatus() {
		return trnxStatus;
	}

	public void setTrnxStatus(String trnxStatus) {
		this.trnxStatus = trnxStatus;
	}

	public Date getTrnxDate() {
		return trnxDate;
	}

	public void setTrnxDate(Date trnxDate) {
		this.trnxDate = trnxDate;
	}

	public String getTrnxRef() {
		return trnxRef;
	}

	public void setTrnxRef(String trnxRef) {
		this.trnxRef = trnxRef;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(String foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
