package com.amx.jax.pricer.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.amx.jax.pricer.var.PricerServiceConstants.GL_JOB_STATUS;

@Entity
@Table(name = "V_EX_GL_JOB_STATUS")
public class VwExGlJobStatus implements Serializable {

	private static final long serialVersionUID = 8076702182738926973L;

	@Id
	@Column(name = "UUID")
	private String uuid;

	@Column(name = "EX_JOB_TRACKER_ID")
	private BigDecimal jobTrackerId;

	@Column(name = "JOB_ID")
	private BigDecimal jobId;

	@Column(name = "JOB_START_DATE")
	private Date jobStartDate;

	@Column(name = "JOB_END_DATE")
	private Date jobEndDate;

	@Column(name = "JOB_STATUS")
	@Enumerated(value = EnumType.STRING)
	private GL_JOB_STATUS jobStatus;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public BigDecimal getJobTrackerId() {
		return jobTrackerId;
	}

	public void setJobTrackerId(BigDecimal jobTrackerId) {
		this.jobTrackerId = jobTrackerId;
	}

	public BigDecimal getJobId() {
		return jobId;
	}

	public void setJobId(BigDecimal jobId) {
		this.jobId = jobId;
	}

	public Date getJobStartDate() {
		return jobStartDate;
	}

	public void setJobStartDate(Date jobStartDate) {
		this.jobStartDate = jobStartDate;
	}

	public Date getJobEndDate() {
		return jobEndDate;
	}

	public void setJobEndDate(Date jobEndDate) {
		this.jobEndDate = jobEndDate;
	}

	public GL_JOB_STATUS getJobStatus() {
		return jobStatus;
	}

	public void setJobStatus(GL_JOB_STATUS jobStatus) {
		this.jobStatus = jobStatus;
	}

}
