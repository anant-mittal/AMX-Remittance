package com.amx.jax.model.request.fx;

import java.math.BigDecimal;
import java.util.Date;

public class FcSaleOrderFailReportDTO {
	
	private BigDecimal customerReference;
	private String customerName;
	private String customerContact;
	private BigDecimal transactionLocalAmount;
	private BigDecimal transactionForeignAmount;
	private String localCurrencyQuote;
	private String foreignCurrencyQuote;
	private String fromDate;
	private String toDate;
	private String exceptionMessage;
	
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerContact() {
		return customerContact;
	}
	public void setCustomerContact(String customerContact) {
		this.customerContact = customerContact;
	}
	
	public BigDecimal getTransactionLocalAmount() {
		return transactionLocalAmount;
	}
	public void setTransactionLocalAmount(BigDecimal transactionLocalAmount) {
		this.transactionLocalAmount = transactionLocalAmount;
	}
	
	public BigDecimal getTransactionForeignAmount() {
		return transactionForeignAmount;
	}
	public void setTransactionForeignAmount(BigDecimal transactionForeignAmount) {
		this.transactionForeignAmount = transactionForeignAmount;
	}
	
	public String getLocalCurrencyQuote() {
		return localCurrencyQuote;
	}
	public void setLocalCurrencyQuote(String localCurrencyQuote) {
		this.localCurrencyQuote = localCurrencyQuote;
	}
	
	public String getForeignCurrencyQuote() {
		return foreignCurrencyQuote;
	}
	public void setForeignCurrencyQuote(String foreignCurrencyQuote) {
		this.foreignCurrencyQuote = foreignCurrencyQuote;
	}
	
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
	
	public String getExceptionMessage() {
		return exceptionMessage;
	}
	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
		
}
