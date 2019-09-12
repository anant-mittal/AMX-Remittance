/**
 * 
 */
package com.amx.jax.dbmodel.partner;

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
@Table(name="EX_BANK_EXTERNAL_REF_DETAIL")
public class BankExternalReferenceDetail implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private BigDecimal bankExtRefDetailSeqId;
	private BigDecimal applicationCountryId;
	private BigDecimal bankId;
	private String bankCode;
	private BigDecimal beneficaryBankId;
	private String beneficaryBankCode;
	private BigDecimal beneficaryBranchId;
	private BigDecimal beneficaryBranchCode;
	private String bankBranchExternalId;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String approvedBy;
	private Date approvedDate;
	private BigDecimal bankExternalReferenceHeadId;
	private BigDecimal countryId;
	private String remarks;
	private String indic1;
	private String indic2 ;
	private String flexField1;
	private String flexField2;
	private String flexField3;

	@Id
	@GeneratedValue(generator="ex_bank_external_ref_detail_seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_bank_external_ref_detail_seq",sequenceName="EX_BANK_EXT_REF_DETAIL_SEQ",allocationSize=1)
	@Column(name = "BANK_EXT_REF_DETAIL_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getBankExtRefDetailSeqId() {
		return bankExtRefDetailSeqId;
	}
	public void setBankExtRefDetailSeqId(BigDecimal bankExtRefDetailSeqId) {
		this.bankExtRefDetailSeqId = bankExtRefDetailSeqId;
	}
	
	@Column(name = "APPLICATION_COUNTRY_ID")
	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}
	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}
	
	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name = "BENEFICARY_BANK_ID")
	public BigDecimal getBeneficaryBankId() {
		return beneficaryBankId;
	}
	public void setBeneficaryBankId(BigDecimal beneficaryBankId) {
		this.beneficaryBankId = beneficaryBankId;
	}
	
	@Column(name = "BENEFICARY_BRANCH_ID")
	public BigDecimal getBeneficaryBranchId() {
		return beneficaryBranchId;
	}
	public void setBeneficaryBranchId(BigDecimal beneficaryBranchId) {
		this.beneficaryBranchId = beneficaryBranchId;
	}
	
	@Column(name = "BANK_EXT_REF_HEAD_SEQ_ID")
	public BigDecimal getBankExternalReferenceHeadId() {
		return bankExternalReferenceHeadId;
	}
	public void setBankExternalReferenceHeadId(BigDecimal bankExternalReferenceHeadId) {
		this.bankExternalReferenceHeadId = bankExternalReferenceHeadId;
	}
	
	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	@Column(name="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name="BENEFICARY_BANK_CODE")
	public String getBeneficaryBankCode() {
		return beneficaryBankCode;
	}	
	public void setBeneficaryBankCode(String beneficaryBankCode) {
		this.beneficaryBankCode = beneficaryBankCode;
	}
	
	@Column(name="BENEFICARY_BRANCH_CODE")
	public BigDecimal getBeneficaryBranchCode() {
		return beneficaryBranchCode;
	}
	public void setBeneficaryBranchCode(BigDecimal beneficaryBranchCode) {
		this.beneficaryBranchCode = beneficaryBranchCode;
	}
	
	@Column(name="BRANCH_EXTERNAL_ID")
	public String getBankBranchExternalId() {
		return bankBranchExternalId;
	}
	public void setBankBranchExternalId(String bankBranchExternalId) {
		this.bankBranchExternalId = bankBranchExternalId;
	}
	
	@Column(name="ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
	@Column(name="CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@Column(name="CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name="MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name="MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Column(name="APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}

	@Column(name="APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}
	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}

	@Column(name="REMARKS")
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	@Column(name="O_INDIC1")
	public String getIndic1() {
		return indic1;
	}
	public void setIndic1(String indic1) {
		this.indic1 = indic1;
	}

	@Column(name="O_INDIC2")
	public String getIndic2() {
		return indic2;
	}
	public void setIndic2(String indic2) {
		this.indic2 = indic2;
	}
	
	@Column(name="FLEX_FIELD1")
	public String getFlexField1() {
		return flexField1;
	}
	public void setFlexField1(String flexField1) {
		this.flexField1 = flexField1;
	}

	@Column(name="FLEX_FIELD2")
	public String getFlexField2() {
		return flexField2;
	}
	public void setFlexField2(String flexField2) {
		this.flexField2 = flexField2;
	}

	@Column(name="FLEX_FIELD3")
	public String getFlexField3() {
		return flexField3;
	}
	public void setFlexField3(String flexField3) {
		this.flexField3 = flexField3;
	}
	
}
