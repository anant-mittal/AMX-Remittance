/*******************************************************************************************************************

File	: CustomerBank.java

Project	: AlmullaExchange

Package	: com.amg.exchange.treasury.model

Created	:	
				Date	: 27-Jan-2015 
				By		: Nagarjuna.Atla
				Revision:

Last Change:
				Date	: 
				By		: 
				Revision:

Description:
 
**********************************************************************************************************************/
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
@Table(name = "EX_CUSTOMER_BANK")
public class CustomerBank implements Serializable{
	private static final long serialVersionUID = 2315791709068216697L;
	
	private BigDecimal customerBankId;
	private BigDecimal customerId;
	private BigDecimal bankId;
	private String bankCode;
	private String collectionMode;
	private String debitCard;
	private String debitCardName;
	private String authorizedBy;
	private Date authorizedDate;
	private String isActive;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private BigDecimal customerReference;
	private String password;
	private String stsRemarks;
	private String bankPrefix;
	private String bankSuffix;
	private BigDecimal relationsId;
	
	public CustomerBank() {
		super();
	}
	
	@Id
	@GeneratedValue(generator = "ex_customer_bank_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_customer_bank_seq", sequenceName = "EX_CUSTOMER_BANK_SEQ", allocationSize = 1)
	@Column(name = "CUSTOMER_BANK_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCustomerBankId() {
		return customerBankId;
	}
	public void setCustomerBankId(BigDecimal customerBankId) {
		this.customerBankId = customerBankId;
	}
	
	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "BANK_ID")
	public BigDecimal getBankId() {
		return bankId;
	}
	public void setBankId(BigDecimal bankId) {
		this.bankId = bankId;
	}
	
	@Column(name = "CUSTOMER_REFERENCE")
	public BigDecimal getCustomerReference() {
		return customerReference;
	}
	public void setCustomerReference(BigDecimal customerReference) {
		this.customerReference = customerReference;
	}
	 
	@Column(name = "BANK_CODE")
	public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	
	@Column(name = "COLLECTION_MODE")
	public String getCollectionMode() {
		return collectionMode;
	}
	public void setCollectionMode(String collectionMode) {
		this.collectionMode = collectionMode;
	}
	
	@Column(name = "DEBIT_CARD")
	public String getDebitCard() {
		return debitCard;
	}
	public void setDebitCard(String debitCard) {
		this.debitCard = debitCard;
	}
	
	@Column(name = "DEBIT_CARD_NAME")
	public String getDebitCardName() {
		return debitCardName;
	}
	public void setDebitCardName(String debitCardName) {
		this.debitCardName = debitCardName;
	}
	
	@Column(name = "AUTHORIZED_BY")
	public String getAuthorizedBy() {
		return authorizedBy;
	}
	public void setAuthorizedBy(String authorizedBy) {
		this.authorizedBy = authorizedBy;
	}
	
	@Column(name = "AUTHORIZED_DATE")
	public Date getAuthorizedDate() {
		return authorizedDate;
	}
	public void setAuthorizedDate(Date authorizedDate) {
		this.authorizedDate = authorizedDate;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
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
	
	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column(name = "PASSWORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "REMARKS")
	public String getStsRemarks() {
		return stsRemarks;
	}
	public void setStsRemarks(String stsRemarks) {
		this.stsRemarks = stsRemarks;
	}

	@Column(name = "BANK_PREFIX")
	public String getBankPrefix() {
		return bankPrefix;
	}
	public void setBankPrefix(String bankPrefix) {
		this.bankPrefix = bankPrefix;
	}

	@Column(name = "BANK_SUFFIX")
	public String getBankSuffix() {
		return bankSuffix;
	}
	public void setBankSuffix(String bankSuffix) {
		this.bankSuffix = bankSuffix;
	}

	@Column(name="RELATIONS_ID")
	public BigDecimal getRelationsId() {
		return relationsId;
	}

	public void setRelationsId(BigDecimal relationsId) {
		this.relationsId = relationsId;
	}

}
