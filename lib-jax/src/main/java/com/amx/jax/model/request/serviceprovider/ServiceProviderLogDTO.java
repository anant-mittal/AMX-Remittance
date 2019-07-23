package com.amx.jax.model.request.serviceprovider;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

public class ServiceProviderLogDTO {
	
	private BigDecimal applicationCountryId;
	private BigDecimal companyId;
	private BigDecimal countryBranchId;
	private BigDecimal emosBranchCode;
	private BigDecimal customerId;
	private BigDecimal customerReference;
	private String transactionType;
	private String xmlType;	
	private BigDecimal sequence;
	private String fileName;	
	private Clob xmlData;
	private String identifier;
	private BigDecimal refernceNo;
	private String foreignTerminalId;
	private String mtcNo;
	private String createdBy;
	private Date createdDate;
	
	
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}
	
	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}
	
	public BigDecimal getEmosBranchCode() {
		return emosBranchCode;
	}
	public void setEmosBranchCode(BigDecimal emosBranchCode) {
		this.emosBranchCode = emosBranchCode;
	}
	
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	
	public String getXmlType() {
		return xmlType;
	}
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
	
	public BigDecimal getSequence() {
		return sequence;
	}
	public void setSequence(BigDecimal sequence) {
		this.sequence = sequence;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public Clob getXmlData() {
		return xmlData;
	}
	public void setXmlData(Clob xmlData) {
		this.xmlData = xmlData;
	}
	
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public BigDecimal getRefernceNo() {
		return refernceNo;
	}
	public void setRefernceNo(BigDecimal refernceNo) {
		this.refernceNo = refernceNo;
	}
	
	public String getForeignTerminalId() {
		return foreignTerminalId;
	}
	public void setForeignTerminalId(String foreignTerminalId) {
		this.foreignTerminalId = foreignTerminalId;
	}
	
	public String getMtcNo() {
		return mtcNo;
	}
	public void setMtcNo(String mtcNo) {
		this.mtcNo = mtcNo;
	}
	
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
