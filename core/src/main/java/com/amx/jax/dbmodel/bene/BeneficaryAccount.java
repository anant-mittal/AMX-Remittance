package com.amx.jax.dbmodel.bene;

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
@Table(name = "EX_BENEFICARY_ACCOUNT")
public class BeneficaryAccount implements Serializable {
	
	
	private static final long serialVersionUID = 2315791709068216697L;
	
	private BigDecimal beneficaryAccountSeqId;
	private BigDecimal beneApplicationCountryId;
	private BigDecimal beneficaryCountryId;
	private BigDecimal beneficaryMasterId;
	private BigDecimal bankId;
	private BigDecimal bankBranchId;
	private String bankAccountNumber;
	private BigDecimal currencyId;
	private BigDecimal serviceGroupId ;
	private String aliasFirstName;
	private String aliasSecondName;
	private String aliasThirdName;
	private String aliasFourthName;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String bankCode;
	private BigDecimal bankBranchCode;
	private BigDecimal serviceProviderId;
	private BigDecimal serviceProviderBranchId;
	private String swiftCode;
	private BigDecimal bankAccountTypeId; 
	private String recordStaus;
	private Date lastJavaRemittence;
	private Date lastEmosRemittance; 
	private String ibanNumber;
	
	
	@Id
	@GeneratedValue(generator="ex_beneficary_account_seq", strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_beneficary_account_seq",sequenceName="EX_BENEFICARY_ACCOUNT_SEQ",allocationSize=1)
	@Column(name = "BENEFICARY_ACCOUNT_SEQ_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getBeneficaryAccountSeqId() {
		return beneficaryAccountSeqId;
	}
	public void setBeneficaryAccountSeqId(BigDecimal beneficaryAccountSeqId) {
		this.beneficaryAccountSeqId = beneficaryAccountSeqId;
	}

	
	@Column(name = "APPLICATION_COUNTRY_ID")	
	public BigDecimal getBeneApplicationCountryId() {
		return beneApplicationCountryId;
	}
	public void setBeneApplicationCountryId(BigDecimal beneApplicationCountryId) {
		this.beneApplicationCountryId = beneApplicationCountryId;
	}
	
	
	@Column(name = "BENEFICARY_COUNTRY")
	public BigDecimal getBeneficaryCountryId() {
		return beneficaryCountryId;
	}
	public void setBeneficaryCountryId(BigDecimal beneficaryCountryId) {
		this.beneficaryCountryId = beneficaryCountryId;
	}
	
	@Column(name = "BENEFICARY_MASTER_SEQ_ID")
	public BigDecimal getBeneficaryMasterId() {
		return beneficaryMasterId;
	}
	public void setBeneficaryMasterId(BigDecimal beneficaryMasterId) {
		this.beneficaryMasterId = beneficaryMasterId;
	}
	
	
	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	
	@Column(name = "BANK_BRANCH_ID")
	public BigDecimal getBankBranchId() {
		return bankBranchId;
	}
	public void setBankBranchId(BigDecimal bankBranchId) {
		this.bankBranchId = bankBranchId;
	}
	
	
	

	@Column(name = "BANK_ACCOUNT_NUMBER")
	public String getBankAccountNumber() {
		return bankAccountNumber;
	}
	public void setBankAccountNumber(String bankAccountNumber) {
		this.bankAccountNumber = bankAccountNumber;
	}
	
	
	@Column(name = "CURRENCY_ID")
	public BigDecimal getCurrencyId() {
		return currencyId;
	}
	public void setCurrencyId(BigDecimal currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name="ALIAS_FIRST_NAME")
	public String getAliasFirstName() {
		return aliasFirstName;
	}
	public void setAliasFirstName(String aliasFirstName) {
		this.aliasFirstName = aliasFirstName;
	}
	
	@Column(name="ALIAS_SECOND_NAME")
	public String getAliasSecondName() {
		return aliasSecondName;
	}
	public void setAliasSecondName(String aliasSecondName) {
		this.aliasSecondName = aliasSecondName;
	}
	
	@Column(name="ALIAS_THIRD_NAME")
	public String getAliasThirdName() {
		return aliasThirdName;
	}
	public void setAliasThirdName(String aliasThirdName) {
		this.aliasThirdName = aliasThirdName;
	}
	
	@Column(name="ALIAS_FOURTH_NAME")
	public String getAliasFourthName() {
		return aliasFourthName;
	}
	public void setAliasFourthName(String aliasFourthName) {
		this.aliasFourthName = aliasFourthName;
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

	@Column(name="BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	@Column(name="BRANCH_CODE")
	public BigDecimal getBankBranchCode() {
		return bankBranchCode;
	}
	public void setBankBranchCode(BigDecimal bankBranchCode) {
		this.bankBranchCode = bankBranchCode;
	}

	
	@Column(name = "SERVICE_PROVIDER")
	public BigDecimal getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(BigDecimal serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}

	@Column(name="BANK_ACCOUNT_TYPE_ID")
	public BigDecimal getBankAccountTypeId() {
		return bankAccountTypeId;
	}
	public void setBankAccountTypeId(BigDecimal bankAccountTypeId) {
		this.bankAccountTypeId = bankAccountTypeId;
	}

	@Column(name="SWIFT_BIC")
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	
	@Column(name="SERVICE_PROVIDER_BRANCH_ID")
	public BigDecimal getServiceProviderBranchId() {
		return serviceProviderBranchId;
	}
	public void setServiceProviderBranchId(BigDecimal serviceProviderBranchId) {
		this.serviceProviderBranchId = serviceProviderBranchId;
	}

	@Column(name="CREATION_TYPE")
	public String getRecordStaus() {
		return recordStaus;
	}

	public void setRecordStaus(String recordStaus) {
		this.recordStaus = recordStaus;
	}

	@Column(name="LAST_JAVA_REMITTANCE")
	public Date getLastJavaRemittence() {
		return lastJavaRemittence;
	}

	public void setLastJavaRemittence(Date lastJavaRemittence) {
		this.lastJavaRemittence = lastJavaRemittence;
	}
	@Column(name="LAST_EMOS_REMITTANCE")
	public Date getLastEmosRemittance() {
		return lastEmosRemittance;
	}

	public void setLastEmosRemittance(Date lastEmosRemittance) {
		this.lastEmosRemittance = lastEmosRemittance;
	}
	
	@Column(name = "SERVICE_GROUP_ID")
	public BigDecimal getServiceGroupId() {
		return serviceGroupId;
	}
	public void setServiceGroupId(BigDecimal serviceGroupId) {
		this.serviceGroupId = serviceGroupId;
	}
	
	@Column(name = "IBAN_NUMBER")
	public String getIbanNumber() {
		return ibanNumber;
	}
	public void setIbanNumber(String ibanNumber) {
		this.ibanNumber = ibanNumber;
	}
	
}

