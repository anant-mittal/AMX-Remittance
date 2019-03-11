package com.amx.jax.grid.views;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.es.ESDocFormat;
import com.amx.jax.grid.GridViewRecord;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "VW_CUSTOMER_KIBANA")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDetailViewRecord implements GridViewRecord {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CUSTOMER_ID")
	private BigDecimal id;

	@Column(name = "CUSTOMER_TYPE_ID")
	private BigDecimal typeId;

	@Column(name = "IDENTITY_INT")
	private String identity;

	@Column(name = "CUSTOMER_EN_NAME")
	private String name;

	@Column(name = "GENDER")
	private String gender;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "NATIONALITY")
	private BigDecimal nationality;

	@Column(name = "NATIONALITY_CODE")
	private String nationalityCode;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "PREFIX_CODE_MOBILE")
	private String mobilePrefix;

	@Column(name = "OTHER_CONTACTNO")
	private String mobileOther;

	@Column(name = "PREFIX_CODE_MOBILE_OTH")
	private String mobileOtherPrefix;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "WHATSAPP_NO")
	private String whatsapp;

	@Column(name = "WHATSAPP_PREFIX_CODE")
	private String whatsappPrefix;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "ONLINE_CREATION_DATE")
	private Date creationDateOnline;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "LAST_UPDATED")
	private Date updateDate;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "LATEST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "IS_MOBILE_WHATSAPP")
	private String isMobileWhatsApp;

	@Column(name = "IS_ONLINE_USER")
	private String isOnlineUser;

	@ESDocFormat(ESDocFormat.Type.DATE)
	@Column(name = "LAST_TRANSACTION_DATE")
	private Date lastTransactionDate;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public BigDecimal getNationality() {
		return nationality;
	}

	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
	}

	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String countryCode) {
		this.nationalityCode = countryCode;
	}

	// Contact
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Account Info
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getIsMobileWhatsApp() {
		return isMobileWhatsApp;
	}

	public void setIsMobileWhatsApp(String isMobileWhatsApp) {
		this.isMobileWhatsApp = isMobileWhatsApp;
	}

	public String getIsOnlineUser() {
		return isOnlineUser;
	}

	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}

	// Transaction
	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	private Integer totalRecords;

	private Integer rn;

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Integer getRn() {
		return rn;
	}

	public void setRn(Integer rn) {
		this.rn = rn;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getTypeId() {
		return typeId;
	}

	public void setTypeId(BigDecimal typeId) {
		this.typeId = typeId;
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public Date getCreationDateOnline() {
		return creationDateOnline;
	}

	public void setCreationDateOnline(Date creationDateOnline) {
		this.creationDateOnline = creationDateOnline;
	}

	public String getMobilePrefix() {
		return mobilePrefix;
	}

	public void setMobilePrefix(String mobilePrefix) {
		this.mobilePrefix = mobilePrefix;
	}

	public String getMobileOther() {
		return mobileOther;
	}

	public void setMobileOther(String mobileOther) {
		this.mobileOther = mobileOther;
	}

	public String getMobileOtherPrefix() {
		return mobileOtherPrefix;
	}

	public void setMobileOtherPrefix(String mobileOtherPrefix) {
		this.mobileOtherPrefix = mobileOtherPrefix;
	}

	public String getWhatsappPrefix() {
		return whatsappPrefix;
	}

	public void setWhatsappPrefix(String whatsappPrefix) {
		this.whatsappPrefix = whatsappPrefix;
	}
}
