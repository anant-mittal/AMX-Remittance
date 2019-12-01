package com.amx.jax.dbmodel;

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
@Table(name = "EX_CUSTOMER_CALLS")
public class CustomerCallDetails {

	@Id
	@GeneratedValue(generator = "EX_CUSTOMER_CALLS_SEQ", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "EX_CUSTOMER_CALLS_SEQ", sequenceName = "EX_CUSTOMER_CALLS_SEQ", allocationSize = 1)
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	BigDecimal callid;

	@Column(name = "MOBILE")
	String mobile;

	@Column(name = "STATUS")
	String status;

	@Column(name = "CREATED_DATE")
	Date createdDate;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;

	@Column(name = "REMARK")
	String remark;

	@Column(name = "SESSION_ID")
	String session;

	public BigDecimal getCallid() {
		return callid;
	}

	public void setCallid(BigDecimal callid) {
		this.callid = callid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

}
