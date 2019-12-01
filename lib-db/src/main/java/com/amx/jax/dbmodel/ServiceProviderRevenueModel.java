package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EX_TPC_REVENUE")
public class ServiceProviderRevenueModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3805386334088082706L;
	
	@Column(name="UPLOAD_DATE")
	private Date uploadDate;
	
	@Id
	@Column(name="ID")
	private BigDecimal id;
	
	@Column(name="APPLICATION_COUNTRY_ID")
	private BigDecimal applicationCountryId;
	
	@Column(name="TPC_CODE")
	private String tpcCode;
	
	public String getTpcCode() {
		return tpcCode;
	}

	public void setTpcCode(String tpcCode) {
		this.tpcCode = tpcCode;
	}

	public BigDecimal getApplicationCountryId() {
		return applicationCountryId;
	}

	public void setApplicationCountryId(BigDecimal applicationCountryId) {
		this.applicationCountryId = applicationCountryId;
	}

	public Date getUploadDate() {
		return uploadDate;
	}

	public void setUploadDate(Date uploadDate) {
		this.uploadDate = uploadDate;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

}
