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
	private BigDecimal bankCountryId;
	private String ibanFlag;
	
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

	@Column(name = "BANK_COUNTRY_ID", precision = 22, scale = 0)
	public BigDecimal getBankCountryId() {
		return bankCountryId;
	}

	public void setBankCountryId(BigDecimal bankCountryId) {
		this.bankCountryId = bankCountryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address1 == null) ? 0 : address1.hashCode());
		result = prime * result + ((address1Ar == null) ? 0 : address1Ar.hashCode());
		result = prime * result + ((address2 == null) ? 0 : address2.hashCode());
		result = prime * result + ((address2Ar == null) ? 0 : address2Ar.hashCode());
		result = prime * result + ((allowNoBank == null) ? 0 : allowNoBank.hashCode());
		result = prime * result + ((approvedBy == null) ? 0 : approvedBy.hashCode());
		result = prime * result + ((approvedDate == null) ? 0 : approvedDate.hashCode());
		result = prime * result + ((bankCode == null) ? 0 : bankCode.hashCode());
		result = prime * result + ((bankCountryId == null) ? 0 : bankCountryId.hashCode());
		result = prime * result + ((bankFullName == null) ? 0 : bankFullName.hashCode());
		result = prime * result + ((bankFullNameAr == null) ? 0 : bankFullNameAr.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((bankShortName == null) ? 0 : bankShortName.hashCode());
		result = prime * result + ((bankShortNameAr == null) ? 0 : bankShortNameAr.hashCode());
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((creator == null) ? 0 : creator.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((faxNo == null) ? 0 : faxNo.hashCode());
		result = prime * result + ((fileAlls == null) ? 0 : fileAlls.hashCode());
		result = prime * result + ((fileBranch == null) ? 0 : fileBranch.hashCode());
		result = prime * result + ((fileRemitMode == null) ? 0 : fileRemitMode.hashCode());
		result = prime * result + ((ifscLen == null) ? 0 : ifscLen.hashCode());
		result = prime * result + ((languageInd == null) ? 0 : languageInd.hashCode());
		result = prime * result + ((micrCode == null) ? 0 : micrCode.hashCode());
		result = prime * result + ((modifier == null) ? 0 : modifier.hashCode());
		result = prime * result + ((recordStatus == null) ? 0 : recordStatus.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((reutersBankName == null) ? 0 : reutersBankName.hashCode());
		result = prime * result + ((splitIndicator == null) ? 0 : splitIndicator.hashCode());
		result = prime * result + ((teleponeNo == null) ? 0 : teleponeNo.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BankMasterModel other = (BankMasterModel) obj;
		if (address1 == null) {
			if (other.address1 != null)
				return false;
		} else if (!address1.equals(other.address1))
			return false;
		if (address1Ar == null) {
			if (other.address1Ar != null)
				return false;
		} else if (!address1Ar.equals(other.address1Ar))
			return false;
		if (address2 == null) {
			if (other.address2 != null)
				return false;
		} else if (!address2.equals(other.address2))
			return false;
		if (address2Ar == null) {
			if (other.address2Ar != null)
				return false;
		} else if (!address2Ar.equals(other.address2Ar))
			return false;
		if (allowNoBank == null) {
			if (other.allowNoBank != null)
				return false;
		} else if (!allowNoBank.equals(other.allowNoBank))
			return false;
		if (approvedBy == null) {
			if (other.approvedBy != null)
				return false;
		} else if (!approvedBy.equals(other.approvedBy))
			return false;
		if (approvedDate == null) {
			if (other.approvedDate != null)
				return false;
		} else if (!approvedDate.equals(other.approvedDate))
			return false;
		if (bankCode == null) {
			if (other.bankCode != null)
				return false;
		} else if (!bankCode.equals(other.bankCode))
			return false;
		if (bankCountryId == null) {
			if (other.bankCountryId != null)
				return false;
		} else if (!bankCountryId.equals(other.bankCountryId))
			return false;
		if (bankFullName == null) {
			if (other.bankFullName != null)
				return false;
		} else if (!bankFullName.equals(other.bankFullName))
			return false;
		if (bankFullNameAr == null) {
			if (other.bankFullNameAr != null)
				return false;
		} else if (!bankFullNameAr.equals(other.bankFullNameAr))
			return false;
		if (bankId == null) {
			if (other.bankId != null)
				return false;
		} else if (!bankId.equals(other.bankId))
			return false;
		if (bankShortName == null) {
			if (other.bankShortName != null)
				return false;
		} else if (!bankShortName.equals(other.bankShortName))
			return false;
		if (bankShortNameAr == null) {
			if (other.bankShortNameAr != null)
				return false;
		} else if (!bankShortNameAr.equals(other.bankShortNameAr))
			return false;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (creator == null) {
			if (other.creator != null)
				return false;
		} else if (!creator.equals(other.creator))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (faxNo == null) {
			if (other.faxNo != null)
				return false;
		} else if (!faxNo.equals(other.faxNo))
			return false;
		if (fileAlls == null) {
			if (other.fileAlls != null)
				return false;
		} else if (!fileAlls.equals(other.fileAlls))
			return false;
		if (fileBranch == null) {
			if (other.fileBranch != null)
				return false;
		} else if (!fileBranch.equals(other.fileBranch))
			return false;
		if (fileRemitMode == null) {
			if (other.fileRemitMode != null)
				return false;
		} else if (!fileRemitMode.equals(other.fileRemitMode))
			return false;
		if (ifscLen == null) {
			if (other.ifscLen != null)
				return false;
		} else if (!ifscLen.equals(other.ifscLen))
			return false;
		if (languageInd == null) {
			if (other.languageInd != null)
				return false;
		} else if (!languageInd.equals(other.languageInd))
			return false;
		if (micrCode == null) {
			if (other.micrCode != null)
				return false;
		} else if (!micrCode.equals(other.micrCode))
			return false;
		if (modifier == null) {
			if (other.modifier != null)
				return false;
		} else if (!modifier.equals(other.modifier))
			return false;
		if (recordStatus == null) {
			if (other.recordStatus != null)
				return false;
		} else if (!recordStatus.equals(other.recordStatus))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (reutersBankName == null) {
			if (other.reutersBankName != null)
				return false;
		} else if (!reutersBankName.equals(other.reutersBankName))
			return false;
		if (splitIndicator == null) {
			if (other.splitIndicator != null)
				return false;
		} else if (!splitIndicator.equals(other.splitIndicator))
			return false;
		if (teleponeNo == null) {
			if (other.teleponeNo != null)
				return false;
		} else if (!teleponeNo.equals(other.teleponeNo))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		return true;
	}

	@Column(name = "IBAN_FLAG")
	public String getIbanFlag() {
		return ibanFlag;
	}

	public void setIbanFlag(String ibanFlag) {
		this.ibanFlag = ibanFlag;
	}
	 
	 
	
}
