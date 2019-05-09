package com.amx.jax.dbmodel.remittance;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 
 * @author rabil
 *
 */

@Entity
@Table(name="V_EX_VAT_DETAILS")
public class ViewVatDetails implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7426933595030820956L;



	@Column(name="VAT_MASTER_ID")
	private BigDecimal vatmasterId;
	
	@Id
	@Column(name="VAT_MASTER_DETAIL_ID")
	private BigDecimal vatmasterDetailId;
	
	@Column(name="VAT_PERCENTAG")
	private BigDecimal vatPercentage;
	
	@Column(name="ROUND_OFF")
	private BigDecimal roundOff;
	
	@Column(name="VAT_CATEGORY")
	private String vatCategory;
	
	@Column(name="ACCOUNT_TYPE")
	private String accountType;
	
	@Column(name="VAT_TYPE")
	private String vatType;
	
	@Column(name="CALCULATION_TYPE")
	private String calculationType;
	
	@Column(name="FROM_DT")
	private Date fromDate;
	@Column(name="TO_DT")
	private Date toDate;
	public BigDecimal getVatmasterId() {
		return vatmasterId;
	}
	public void setVatmasterId(BigDecimal vatmasterId) {
		this.vatmasterId = vatmasterId;
	}
	public BigDecimal getVatmasterDetailId() {
		return vatmasterDetailId;
	}
	public void setVatmasterDetailId(BigDecimal vatmasterDetailId) {
		this.vatmasterDetailId = vatmasterDetailId;
	}
	public BigDecimal getVatPercentage() {
		return vatPercentage;
	}
	public void setVatPercentage(BigDecimal vatPercentage) {
		this.vatPercentage = vatPercentage;
	}
	public BigDecimal getRoundOff() {
		return roundOff;
	}
	public void setRoundOff(BigDecimal roundOff) {
		this.roundOff = roundOff;
	}
	public String getVatCategory() {
		return vatCategory;
	}
	public void setVatCategory(String vatCategory) {
		this.vatCategory = vatCategory;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getVatType() {
		return vatType;
	}
	public void setVatType(String vatType) {
		this.vatType = vatType;
	}
	public String getCalculationType() {
		return calculationType;
	}
	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
}






