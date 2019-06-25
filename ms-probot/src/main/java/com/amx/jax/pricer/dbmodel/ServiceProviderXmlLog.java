package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="EX_WU_LOG")
public class ServiceProviderXmlLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal wulogId;
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
	
	
	public ServiceProviderXmlLog() {
		super();
	}

	public ServiceProviderXmlLog(BigDecimal wulogId, BigDecimal applicationCountryId,
			BigDecimal companyId, BigDecimal countryBranchId,
			BigDecimal emosBranchCode, BigDecimal customerId,
			BigDecimal customerReference, String transactionType,
			String xmlType, BigDecimal sequence, String fileName,
			Clob xmlData, String identifier, BigDecimal refernceNo,
			String foreignTerminalId, String mtcNo, String createdBy,
			Date createdDate) {
		super();
		this.wulogId = wulogId;
		this.applicationCountryId = applicationCountryId;
		this.companyId = companyId;
		this.countryBranchId = countryBranchId;
		this.emosBranchCode = emosBranchCode;
		this.customerId = customerId;
		this.customerReference = customerReference;
		this.transactionType = transactionType;
		this.xmlType = xmlType;
		this.sequence = sequence;
		this.fileName = fileName;
		this.xmlData = xmlData;
		this.identifier = identifier;
		this.refernceNo = refernceNo;
		this.foreignTerminalId = foreignTerminalId;
		this.mtcNo = mtcNo;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	@Id
	@GeneratedValue(generator = "ex_wu_log_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_wu_log_seq", sequenceName = "EX_WU_LOG_SEQ", allocationSize = 1)
	@Column(name = "WU_LOG_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getWulogId() {
		return wulogId;
	}
	public void setWulogId(BigDecimal wulogId) {
		this.wulogId = wulogId;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	@Column(name = "COMPANY_ID")
	public BigDecimal getCompanyId() {
		return companyId;
	}
	public void setCompanyId(BigDecimal companyId) {
		this.companyId = companyId;
	}

	@Column(name = "COUNTRY_BRANCH_ID")
	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}
	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	@Column(name = "EMOS_BRANCH_CODE")
	public BigDecimal getEmosBranchCode() {
		return emosBranchCode;
	}
	public void setEmosBranchCode(BigDecimal emosBranchCode) {
		this.emosBranchCode = emosBranchCode;
	}

	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CUSTOMER_REFERENCE")
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}

	@Column(name = "TRANSACTION_TYPE")
	public String getTransactionType() {
		return transactionType;
	}
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Column(name = "XML_TYPE")
	public String getXmlType() {
		return xmlType;
	}
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}

	@Column(name = "SEQ")
	public BigDecimal getSequence() {
		return sequence;
	}
	public void setSequence(BigDecimal sequence) {
		this.sequence = sequence;
	}

	@Column(name = "FILENAME")
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "XML_DATA")
	public Clob getXmlData() {
		return xmlData;
	}
	public void setXmlData(Clob xmlData) {
		this.xmlData = xmlData;
	}

	@Column(name = "IDENTIFIER")
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Column(name = "REFERENCE_NO")
	public BigDecimal getRefernceNo() {
		return refernceNo;
	}
	public void setRefernceNo(BigDecimal refernceNo) {
		this.refernceNo = refernceNo;
	}

	@Column(name = "FOREIGN_TERMINAL_ID")
	public String getForeignTerminalId() {
		return foreignTerminalId;
	}
	public void setForeignTerminalId(String foreignTerminalId) {
		this.foreignTerminalId = foreignTerminalId;
	}

	@Column(name = "MTCNO")
	public String getMtcNo() {
		return mtcNo;
	}
	public void setMtcNo(String mtcNo) {
		this.mtcNo = mtcNo;
	}

	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

}
