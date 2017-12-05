package com.amx.jax.dbmodel;
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
@Table(name = "EX_BANK_MASTER")
public class BankMasterModel implements java.io.Serializable {

	/**
	 * Generated Serial UID 
	 */
	private static final long serialVersionUID = 3948079494013331401L;
	private BigDecimal bankId;
	private String bankCode;
	private String bankFullName;
	private String bankShortName;
	private String address1;
	private String address2;
	private String zipCode;
	private String teleponeNo;
	private String faxNo;
	private String email;
	private String recordStatus;
	private String bankFullNameAr;
	private String bankShortNameAr;
	private String address1Ar;
	private String address2Ar;
	private String languageInd;
	private String fileBranch;
	private String fileRemitMode;
	private String fileAlls;
	private BigDecimal ifscLen;
	private String reutersBankName;
	private String creator;
	private Date createDate;
	private Date updateDate;
	private String modifier;
	private String micrCode;	
	private String allowNoBank;
	private String approvedBy;
	private Date approvedDate;
	private String remarks;
	private String splitIndicator;
	public BankMasterModel() {
	}

	public BankMasterModel(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	
	
	@Id
	@GeneratedValue(generator="ex_bank_master_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_bank_master_seq",sequenceName="EX_BANK_MASTER_SEQ",allocationSize=1)
	@Column(name = "BANK_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getBankId() {
		return this.bankId;
	}

	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}

	@Column(name = "BANK_CODE", length = 10)
	public String getBankCode() {
		return this.bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name = "BANK_FULL_NAME", unique=true, length = 150)
	public String getBankFullName() {
		return this.bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	@Column(name = "BANK_SHORT_NAME", length = 80)
	public String getBankShortName() {
		return this.bankShortName;
	}

	public void setBankShortName(String bankShortName) {
		this.bankShortName = bankShortName;
	}

	@Column(name = "ADDRESS1", length = 100)
	public String getAddress1() {
		return this.address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	@Column(name = "ADDRESS2", length = 100)
	public String getAddress2() {
		return this.address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	@Column(name = "ZIP_CODE", length = 15)
	public String getZipCode() {
		return this.zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	@Column(name = "TELEPONE_NO", length = 30)
	public String getTeleponeNo() {
		return this.teleponeNo;
	}

	public void setTeleponeNo(String teleponeNo) {
		this.teleponeNo = teleponeNo;
	}

	@Column(name = "FAX_NO", length = 30)
	public String getFaxNo() {
		return this.faxNo;
	}

	public void setFaxNo(String faxNo) {
		this.faxNo = faxNo;
	}

	@Column(name = "EMAIL", length = 30)
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "ISACTIVE", length = 1)
	public String getRecordStatus() {
		return this.recordStatus;
	}
	public void setRecordStatus(String recordStatus) {
		this.recordStatus = recordStatus;
	}
	

	@Column(name = "ALLOW_NO_BANK", length = 1)
	public String getAllowNoBank() {
		return allowNoBank;
	}

	public void setAllowNoBank(String allowNoBank) {
		this.allowNoBank = allowNoBank;
	}
	

	@Column(name = "BANK_FULL_NAME_AR", length = 150)
	public String getBankFullNameAr() {
		return this.bankFullNameAr;
	}

	public void setBankFullNameAr(String bankFullNameAr) {
		this.bankFullNameAr = bankFullNameAr;
	}

	@Column(name = "BANK_SHORT_NAME_AR", length = 100)
	public String getBankShortNameAr() {
		return this.bankShortNameAr;
	}

	public void setBankShortNameAr(String bankShortNameAr) {
		this.bankShortNameAr = bankShortNameAr;
	}

	@Column(name = "ADDRESS1_AR", length = 120)
	public String getAddress1Ar() {
		return this.address1Ar;
	}

	public void setAddress1Ar(String address1Ar) {
		this.address1Ar = address1Ar;
	}

	@Column(name = "ADDRESS2_AR", length = 120)
	public String getAddress2Ar() {
		return this.address2Ar;
	}

	public void setAddress2Ar(String address2Ar) {
		this.address2Ar = address2Ar;
	}

	@Column(name = "LANGUAGE_IND", length = 1)
	public String getLanguageInd() {
		return this.languageInd;
	}

	public void setLanguageInd(String languageInd) {
		this.languageInd = languageInd;
	}

	@Column(name = "FILE_BRANCH", length = 1)
	public String getFileBranch() {
		return this.fileBranch;
	}

	public void setFileBranch(String fileBranch) {
		this.fileBranch = fileBranch;
	}

	@Column(name = "FILE_REMIT_MODE", length = 1)
	public String getFileRemitMode() {
		return this.fileRemitMode;
	}

	public void setFileRemitMode(String fileRemitMode) {
		this.fileRemitMode = fileRemitMode;
	}

	@Column(name = "FILE_ALLS", length = 1)
	public String getFileAlls() {
		return this.fileAlls;
	}

	public void setFileAlls(String fileAlls) {
		this.fileAlls = fileAlls;
	}

	@Column(name = "IFSC_LEN", precision = 22, scale = 0)
	public BigDecimal getIfscLen() {
		return this.ifscLen;
	}

	public void setIfscLen(BigDecimal ifscLen) {
		this.ifscLen = ifscLen;
	}

	@Column(name = "REUTERS_BANK_NAME", length = 100)
	public String getReutersBankName() {
		return this.reutersBankName;
	}

	public void setReutersBankName(String reutersBankName) {
		this.reutersBankName = reutersBankName;
	}

	@Column(name = "CREATOR", length = 15)
	public String getCreator() {
		return this.creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	@Column(name = "CREATE_DATE")
	public Date getCreateDate() {
		return this.createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "UPDATE_DATE")
	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "MODIFIER", length = 15)
	public String getModifier() {
		return this.modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	
	@Column(name = "MICR_CODE")
	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}
	
	@Column(name = "APPROVED_BY")
	public String getApprovedBy() {
		return approvedBy;
	}

	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	
	@Column(name = "APPROVED_DATE")
	public Date getApprovedDate() {
		return approvedDate;
	}

	public void setApprovedDate(Date approvedDate) {
		this.approvedDate = approvedDate;
	}
	
	@Column(name = "REMARKS")
	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	@Column(name = "SPLIT_INDICATOR")
	public String getSplitIndicator() {
		return splitIndicator;
	}

	public void setSplitIndicator(String splitIndicator) {
		this.splitIndicator = splitIndicator;
	}
	 
	
	
}
