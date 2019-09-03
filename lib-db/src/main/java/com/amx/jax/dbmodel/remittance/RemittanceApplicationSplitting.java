package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "EX_APPL_SPLIT")
public class RemittanceApplicationSplitting implements Serializable{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8338174505572506346L;



	private BigDecimal applTrnxSplitId;
	
	@Column(name = "REMITTANCE_APPLICATION_ID")
	private BigDecimal remittanceApplicationId;
	
	
	@Column(name = "DOCUMENT_FINANCE_YEAR")
	private BigDecimal documentFinanceYear;
	
	@Column(name = "DOCUMENT_ID")
	private BigDecimal documentId;
	
	@Column(name = "DOCUMENT_NO")
	private BigDecimal documentNo;
	
	
	@Column(name = "DOCUMENT_DATE")
	private Date documentDate;
	
	@Column(name = "ACCOUNT_MMYYYY")
	private Date accountMmyyyy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "ISACTIVE")
	private String isactive;
	

	
	@Column(name = "SPLIT_DOCUMENT_NO") 
	private BigDecimal splitDocumentNo;
	

	@Column(name = "FOREIGN_TRANX_AMOUNT")
	private BigDecimal foreignTranxAmount;
	
	@Column(name = "LOCAL_TRANX_AMOUNT")
	private BigDecimal localTranxAmount;
	
	@Column(name="REMARKS")
	private String remarks;
	
	

	@Id
	@GeneratedValue(generator="ex_appl_split_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_appl_split_seq",sequenceName="EX_APPL_SPLIT_SEQ",allocationSize=1)
	@Column(name = "APPLICATION_SPLIT_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getApplTrnxSplitId() {
		return applTrnxSplitId;
	}
	public void setApplTrnxSplitId(BigDecimal applTrnxSplitId) {
		this.applTrnxSplitId = applTrnxSplitId;
	}
	
	public void setRemittanceApplicationId(BigDecimal remittanceApplicationId) {
		this.remittanceApplicationId = remittanceApplicationId;
	}
	public BigDecimal getDocumentFinanceYear() {
		return documentFinanceYear;
	}
	public void setDocumentFinanceYear(BigDecimal documentFinanceYear) {
		this.documentFinanceYear = documentFinanceYear;
	}
	public BigDecimal getDocumentId() {
		return documentId;
	}
	public void setDocumentId(BigDecimal documentId) {
		this.documentId = documentId;
	}
	public BigDecimal getDocumentNo() {
		return documentNo;
	}
	public void setDocumentNo(BigDecimal documentNo) {
		this.documentNo = documentNo;
	}
	public Date getDocumentDate() {
		return documentDate;
	}
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	public Date getAccountMmyyyy() {
		return accountMmyyyy;
	}
	public void setAccountMmyyyy(Date accountMmyyyy) {
		this.accountMmyyyy = accountMmyyyy;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public String getIsactive() {
		return isactive;
	}
	public void setIsactive(String isactive) {
		this.isactive = isactive;
	}
	public BigDecimal getSplitDocumentNo() {
		return splitDocumentNo;
	}
	public void setSplitDocumentNo(BigDecimal splitDocumentNo) {
		this.splitDocumentNo = splitDocumentNo;
	}
	public BigDecimal getForeignTranxAmount() {
		return foreignTranxAmount;
	}
	public void setForeignTranxAmount(BigDecimal foreignTranxAmount) {
		this.foreignTranxAmount = foreignTranxAmount;
	}
	public BigDecimal getLocalTranxAmount() {
		return localTranxAmount;
	}
	public void setLocalTranxAmount(BigDecimal localTranxAmount) {
		this.localTranxAmount = localTranxAmount;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
