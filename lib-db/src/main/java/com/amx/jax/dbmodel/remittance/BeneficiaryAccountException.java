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
@Table(name = "EX_BENEFICIARY_ACC_EXCEPTION")
public class BeneficiaryAccountException implements Serializable {
	

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5082942227282351230L;

	@Id
	@GeneratedValue(generator = "ex_bene_acc_exception_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_bene_acc_exception_seq", sequenceName = "EX_BENE_ACC_EXCEPTION_SEQ", allocationSize = 1)
	@Column(name = "BENE_ACC_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	private BigDecimal beneAccSeqId;
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	
	@Column(name = "BANK_COUNTRY_ID")
	private BigDecimal bankCountryId;
	
	@Column(name = "BANK_ID")
	private BigDecimal bankId;
	
	@Column(name = "BANK_BRANCH_ID")
	private BigDecimal bankBranchId;
	
	@Column(name = "BENEFICIARY_ACCOUNT_NO")
	private String beneAccountNumber;
	
	@Column(name = "ISACTIVE")
	private String isActive;
	
	@Column(name = "CREATED_BY")
	private String createdBy;
	
	@Column(name = "CREATED_DATE")
	private Date createdDate;
	
	@Column(name = "MODIFIED_BY")
	private String modifiedBy;
	
	@Column(name = "MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name = "APPROVED_BY")
	private String approvedBy;
	
	@Column(name = "APPROVED_DATE")
	private Date approvedDate;
	
	
	
	public BeneficiaryAccountException(){
		
	}
	
	
	public BeneficiaryAccountException(BigDecimal beneAccSeqId,
			BigDecimal applicationCountryId, BigDecimal bankCountryId,
			BigDecimal bankId, BigDecimal bankBranchId,
			String beneAccountNumber, String isActive, String createdBy,
			Date createdDate, String modifiedBy, Date modifiedDate,
			String approvedBy, Date approvedDate) {
		super();
		this.beneAccSeqId = beneAccSeqId;
		this.applicationCountryId = applicationCountryId;
		this.bankCountryId = bankCountryId;
		this.bankId = bankId;
		this.bankBranchId = bankBranchId;
		this.beneAccountNumber = beneAccountNumber;
		this.isActive = isActive;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
		this.modifiedBy = modifiedBy;
		this.modifiedDate = modifiedDate;
		this.approvedBy = approvedBy;
		this.approvedDate = approvedDate;
	}
	
	
	
	
	//Getters and setters.

	public BigDecimal getBeneAccSeqId() {
		return beneAccSeqId;
	}
	public void setBeneAccSeqId(BigDecimal beneAccSeqId) {
		this.beneAccSeqId = beneAccSeqId;
	}
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}
	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	public String getBeneAccountNumber() {
		return beneAccountNumber;
	}
	public void setBeneAccountNumber(String beneAccountNumber) {
		this.beneAccountNumber = beneAccountNumber;
	}
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

}
