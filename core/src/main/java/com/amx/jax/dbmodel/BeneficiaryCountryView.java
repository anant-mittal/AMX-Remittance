package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * 
 * @author :Rabil
 * Date    : 28/11/2017
 * purpose : Get beneficiary country List branch as well as Online and KIOSK
 *
 */

@Entity
@Table(name="JAX_VW_BENEFICIARY_COUNTRY")
public class BeneficiaryCountryView {
	
	@Id
	@Column(name="IDNO")
	private BigDecimal idNo;
	@Column(name="CUSTOMER_ID")
	private BigDecimal customerId;
	@Column(name="BENEFICARY_COUNTRY")
	private BigDecimal beneCountry;
	@Column(name="APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountry;
	@Column(name="COUNTRY_CODE")
	private String countryCode;
	@Column(name="BENEFICARY_BANK_COUNTRY_NAME")
	private String countryName;
	
	@Column(name="ORS_STATUS")
	private BigDecimal orsStatus;
	
	public BigDecimal getIdNo() {
		return idNo;
	}
	public void setIdNo(BigDecimal idNo) {
		this.idNo = idNo;
	}
	public BigDecimal getCustomerId() {
		return customerId;
	}
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}
	public BigDecimal getBeneCountry() {
		return beneCountry;
	}
	public void setBeneCountry(BigDecimal beneCountry) {
		this.beneCountry = beneCountry;
	}
	public BigDecimal getApplicationCountry() {
		return applicationCountry;
	}
	public void setApplicationCountry(BigDecimal applicationCountry) {
		this.applicationCountry = applicationCountry;
	}
	public String getCountryCode() {
		return countryCode;
	}
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}
	public String getCountryName() {
		return countryName;
	}
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
	public BigDecimal getOrsStatus() {
		return orsStatus;
	}
	public void setOrsStatus(BigDecimal orsStatus) {
		this.orsStatus = orsStatus;
	}
	
}
