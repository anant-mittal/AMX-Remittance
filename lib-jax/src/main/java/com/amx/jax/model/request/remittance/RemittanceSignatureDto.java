package com.amx.jax.model.request.remittance;

/**
 * @author rabil
 */
import java.math.BigDecimal;
import java.util.Date;

public class RemittanceSignatureDto {

	
	private BigDecimal documentFYear;
	private BigDecimal documentNumber;
	private BigDecimal customerId;
	private BigDecimal forignAmount;
	private BigDecimal foreignCurrencyId;
	
	private BigDecimal beneRelationId;
	private String accountNumber;
	private String dateStr;
	//private Date date;
	private String beneName;
	private String foreignCurrencyIso3Code;
	public BigDecimal getDocumentFYear() {
		return documentFYear;
	}
	public void setDocumentFYear(BigDecimal documentFYear) {
		this.documentFYear = documentFYear;
	}
	public BigDecimal getDocumentNumber() {
		return documentNumber;
	}
	public void setDocumentNumber(BigDecimal documentNumber) {
		this.documentNumber = documentNumber;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getForignAmount() {
		return forignAmount;
	}
	public void setForignAmount(BigDecimal forignAmount) {
		this.forignAmount = forignAmount;
	}
	public BigDecimal getForeignCurrencyId() {
		return foreignCurrencyId;
	}
	public void setForeignCurrencyId(BigDecimal foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
	}
	public BigDecimal getBeneRelationId() {
		return beneRelationId;
	}
	public void setBeneRelationId(BigDecimal beneRelationId) {
		this.beneRelationId = beneRelationId;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	/*public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}*/
	public String getBeneName() {
		return beneName;
	}
	public void setBeneName(String beneName) {
		this.beneName = beneName;
	}
	public String getForeignCurrencyIso3Code() {
		return foreignCurrencyIso3Code;
	}
	public void setForeignCurrencyIso3Code(String foreignCurrencyIso3Code) {
		this.foreignCurrencyIso3Code = foreignCurrencyIso3Code;
	}
	
	
	

}
