package com.amx.jax.grid.views;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.grid.GridViewRecord;

/*
 * Author Rahamathali Shaik
*/
@Entity
@Table(name = "VW_CUSTOMER_KIBANA")
public class CustomerDetailViewRecord implements GridViewRecord {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "CUSTOMER_ID")
	private BigDecimal customerId;

	@Column(name = "CUSTOMER_TYPE_ID")
	private BigDecimal customerTypeId;

	@Column(name = "IDENTITY_INT")
	private BigDecimal identity;

	@Column(name = "CUSTOMER_EN_NAME")
	private String name;

	@Column(name = "GENDER")
	private String gender;

	@Column(name = "DATE_OF_BIRTH")
	private Date dateOfBirth;

	@Column(name = "NATIONALITY")
	private BigDecimal nationality;

	@Column(name = "NATIONALITY_CODE")
	private String nationalityCode;

	@Column(name = "MOBILE")
	private String mobile;

	@Column(name = "EMAIL")
	private String email;

	@Column(name = "CREATION_DATE")
	private Date creationDate;

	@Column(name = "LAST_UPDATED")
	private Date updateDate;

	@Column(name = "LATEST_UPDATE_DATE")
	private Date lastUpdateDate;

	@Column(name = "ISACTIVE")
	private String isActive;

	@Column(name = "IS_MOBILE_WHATSAPP")
	private String isMobileWhatsApp;

	@Column(name = "IS_ONLINE_USER")
	private String isOnlineUser;

	@Column(name = "LAST_TRANSACTION_DATE")
	private Date lastTransactionDate;

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

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

}
