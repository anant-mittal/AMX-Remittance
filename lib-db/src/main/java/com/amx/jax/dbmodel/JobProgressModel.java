package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="EX_TPC_UPLOAD_HD")
public class JobProgressModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name="TPC_CODE")
	private String tpcCode;
	
	@Column(name="CONFIRM_STATUS")
	private String confirmStatus;
	
	@Column(name="CREATED_DATE")
	private Date cretedDate;
	
	@Column(name="LASTEST_DATA_DATE")
	private Date latestDataDate;
	
	@Column(name="MODIFIED_DATE")
	private Date modifiedDate;
	
	@Column(name="UPLOAD_STATUS")
	private String uploadStatus;

	public String getUploadStatus() {
		return uploadStatus;
	}

	public void setUploadStatus(String uploadStatus) {
		this.uploadStatus = uploadStatus;
	}

	public String getTpcCode() {
		return tpcCode;
	}

	public void setTpcCode(String tpcCode) {
		this.tpcCode = tpcCode;
	}

	public String getConfirmStatus() {
		return confirmStatus;
	}

	public void setConfirmStatus(String confirmStatus) {
		this.confirmStatus = confirmStatus;
	}

	public Date getCretedDate() {
		return cretedDate;
	}

	public void setCretedDate(Date cretedDate) {
		this.cretedDate = cretedDate;
	}

	public Date getLatestDataDate() {
		return latestDataDate;
	}

	public void setLatestDataDate(Date latestDataDate) {
		this.latestDataDate = latestDataDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	
}
