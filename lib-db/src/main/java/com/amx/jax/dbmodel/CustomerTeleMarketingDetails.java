package com.amx.jax.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EX_TELEMART_CUSTOMER")
public class CustomerTeleMarketingDetails implements Serializable {

	private static final long serialVersionUID = -6694134035069111195L;

	@Id
	@Column(name = "TELEMART_CUSTOMER_ID")
	BigDecimal leadId;

	@Column(name = "FOLW_UP_CODE")
	String followUpCode;

	@Column(name = "FOLW_UP_DATE")
	Date followUpDate;

	@Column(name = "CUSTOMER_ID")
	BigDecimal customerId;

	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;

	@Column(name = "REMARKS")
	String remark;

	public String getFollowUpCode() {
		return followUpCode;
	}

	public void setFollowUpCode(String followUpCode) {
		this.followUpCode = followUpCode;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public BigDecimal getLeadId() {
		return leadId;
	}

	public void setLeadId(BigDecimal leadId) {
		this.leadId = leadId;
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

	public Date getFollowUpDate() {
		return followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

}
