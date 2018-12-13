package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "EX_FC_ADJ")
@IdClass(ForeignCurrencyOldModel.class)
public class ForeignCurrencyOldModel implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "COMCOD")
	private BigDecimal companyCode;
	
	@Id
	@Column(name = "DOCFYR")
	private BigDecimal documentFYR;
	
	@Id
	@Column(name = "DOCCOD")
	private BigDecimal documentCode;
	
	@Id
	@Column(name = "DOCNO")
	private BigDecimal documentNumber;
	
	@Column(name = "DOCDAT")
	private Date documentDate;
	
	@Column(name = "ACYYMM")
	private Date accountmmyyyy;
	
	@Id
	@Column(name = "LOCCOD")
	private BigDecimal locationCode;
	
	@Column(name = "CURTRNS")
	private String currencyTransfer;
	
	@Column(name = "AMTTRNS")
	private BigDecimal amountTransfer;
	
	@Column(name = "CUREXCH")
	private String currencyExchange;
	
	@Column(name = "AMTEXCH")
	private BigDecimal amountExchange;
	
	@Column(name = "NOTESQTY")
	private BigDecimal noteQuantity;
	
	@Id
	@Column(name = "DENOID")
	private BigDecimal denominationId;
	
	@Column(name = "CRTDAT")
	private Date createdDate;
	
	@Column(name = "CREATOR")
	private String creator;
	
	@Column(name = "UPDDAT")
	private Date updateDate;
	
	@Column(name = "MODIFIER")
	private String modifier;
	
	@Column(name = "RATEAPL")
	private BigDecimal rateApplied;
	
	@Column(name = "DENOAMT")
	private BigDecimal denominationAmount;
	
	@Column(name = "PROGNO")
	private String programNo;
	
	@Column(name = "DOCSTS")
	private String documentStatus;
	
	@Column(name = "STKUPD")
	private String stockUpdate;
	
	@Column(name = "TCTYPE")
	private String transactionType;
	
	@Column(name = "ORAUSER")
	private String oracleUser;
	
	@Column(name = "APPDAT")
	private Date approvedDate;
	
	@Column(name = "APPBY")
	private String approvedBy;
	
	@Column(name = "DOCLNO")
	private BigDecimal documentLineNo;
	
	public BigDecimal getCompanyCode() {
		return companyCode;
	}
	public void setCompanyCode(BigDecimal companyCode) {
		this.companyCode = companyCode;
	}
	
	public BigDecimal getDocumentFYR() {
		return documentFYR;
	}
	public void setDocumentFYR(BigDecimal documentFYR) {
		this.documentFYR = documentFYR;
	}
	
	public BigDecimal getDocumentCode() {
		return documentCode;
	}
	public void setDocumentCode(BigDecimal documentCode) {
		this.documentCode = documentCode;
	}
	
	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}
	
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	
	public Date getAccountmmyyyy() {
		return accountmmyyyy;
	}
	public void setAccountmmyyyy(Date accountmmyyyy) {
		this.accountmmyyyy = accountmmyyyy;
	}
	
	public BigDecimal getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(BigDecimal locationCode) {
		this.locationCode = locationCode;
	}
	
	public String getCurrencyTransfer() {
		return currencyTransfer;
	}
	public void setCurrencyTransfer(String currencyTransfer) {
		this.currencyTransfer = currencyTransfer;
	}
	
	public BigDecimal getAmountTransfer() {
		return amountTransfer;
	}
	public void setAmountTransfer(BigDecimal amountTransfer) {
		this.amountTransfer = amountTransfer;
	}
	
	public String getCurrencyExchange() {
		return currencyExchange;
	}
	public void setCurrencyExchange(String currencyExchange) {
		this.currencyExchange = currencyExchange;
	}
	
	public BigDecimal getAmountExchange() {
		return amountExchange;
	}
	public void setAmountExchange(BigDecimal amountExchange) {
		this.amountExchange = amountExchange;
	}
	
	public BigDecimal getNoteQuantity() {
		return noteQuantity;
	}
	public void setNoteQuantity(BigDecimal noteQuantity) {
		this.noteQuantity = noteQuantity;
	}
	
	public BigDecimal getDenominationId() {
		return denominationId;
	}
	public void setDenominationId(BigDecimal denominationId) {
		this.denominationId = denominationId;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	
	public BigDecimal getRateApplied() {
		return rateApplied;
	}
	public void setRateApplied(BigDecimal rateApplied) {
		this.rateApplied = rateApplied;
	}
	
	public BigDecimal getDenominationAmount() {
		return denominationAmount;
	}
	public void setDenominationAmount(BigDecimal denominationAmount) {
		this.denominationAmount = denominationAmount;
	}
	
	public String getProgramNo() {
		return programNo;
	}
	public void setProgramNo(String programNo) {
		this.programNo = programNo;
	}
	
	public String getDocumentStatus() {
		return documentStatus;
	}
	public void setDocumentStatus(String documentStatus) {
		this.documentStatus = documentStatus;
	}
	
	public String getStockUpdate() {
		return stockUpdate;
	}
	public void setStockUpdate(String stockUpdate) {
		this.stockUpdate = stockUpdate;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getOracleUser() {
		return oracleUser;
	}
	public void setOracleUser(String oracleUser) {
		this.oracleUser = oracleUser;
	}
	
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	public BigDecimal getDocumentLineNo() {
		return documentLineNo;
	}
	public void setDocumentLineNo(BigDecimal documentLineNo) {
		this.documentLineNo = documentLineNo;
	}
	
}
