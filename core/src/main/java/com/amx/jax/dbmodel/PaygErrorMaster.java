package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * @author Shivaraj
 *
 */
@Entity
@Table(name = "EX_ONLINE_PAYG_ERROR_MASTER")
public class PaygErrorMaster implements Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal onlinePaygErrorId;
	private BigDecimal countryId; 
	private String errorCategory;
	private String paygCode;
	private String errorCode;
	
	@Id
	@GeneratedValue(generator = "ex_online_payg_error_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "ex_online_payg_error_seq", sequenceName = "EX_ONLINE_PAYG_ERROR_SEQ", allocationSize = 1)
	@Column(name = "ONLINE_PAYG_ERROR_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getOnlinePaygErrorId() {
		return onlinePaygErrorId;
	}
	public void setOnlinePaygErrorId(BigDecimal onlinePaygErrorId) {
		this.onlinePaygErrorId = onlinePaygErrorId;
	}
	
	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}
	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}
	
	@Column(name = "ERROR_CATEGORY")
	public String getErrorCategory() {
		return errorCategory;
	}
	public void setErrorCategory(String errorCategory) {
		this.errorCategory = errorCategory;
	}
	
	@Column(name = "PAYG_CODE")
	public String getPaygCode() {
		return paygCode;
	}
	public void setPaygCode(String paygCode) {
		this.paygCode = paygCode;
	}
	
	@Column(name = "ERROR_CODE")
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
