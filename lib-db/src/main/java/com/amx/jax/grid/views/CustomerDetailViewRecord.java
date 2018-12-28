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

	private Date lastTransactionDate;
	private String nationalityCode;
	private BigDecimal customerId;
	private BigDecimal customerTypeId;
	private String name;
	private String gender;
	private Date dateOfBirth;
	private BigDecimal nationality;
	private String mobile;
	private String email;
	private Date creationDate;
	private Date updateDate;
	private Date lastUpdateDate;
	private String isActive;
	private String isMobileWhatsApp;
	private String isOnlineUser;

	@Id
	@Column(name = "CUSTOMER_ID")
	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	@Column(name = "CUSTOMER_TYPE_ID")
	public BigDecimal getCustomerTypeId() {
		return customerTypeId;
	}

	public void setCustomerTypeId(BigDecimal customerTypeId) {
		this.customerTypeId = customerTypeId;
	}

	@Column(name = "CUSTOMER_EN_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "GENDER")
	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Column(name = "DATE_OF_BIRTH")
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Column(name = "NATIONALITY")
	public BigDecimal getNationality() {
		return nationality;
	}

	public void setNationality(BigDecimal nationality) {
		this.nationality = nationality;
	}

	@Column(name = "NATIONALITY_CODE")
	public String getNationalityCode() {
		return nationalityCode;
	}

	public void setNationalityCode(String countryCode) {
		this.nationalityCode = countryCode;
	}

	// Contact
	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// Account Info
	@Column(name = "CREATION_DATE")
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@Column(name = "LAST_UPDATED")
	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	@Column(name = "LATEST_UPDATE_DATE")
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "IS_MOBILE_WHATSAPP")
	public String getIsMobileWhatsApp() {
		return isMobileWhatsApp;
	}

	public void setIsMobileWhatsApp(String isMobileWhatsApp) {
		this.isMobileWhatsApp = isMobileWhatsApp;
	}

	@Column(name = "IS_ONLINE_USER")
	public String getIsOnlineUser() {
		return isOnlineUser;
	}

	public void setIsOnlineUser(String isOnlineUser) {
		this.isOnlineUser = isOnlineUser;
	}

	// Transaction
	@Column(name = "LAST_TRANSACTION_DATE")
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
