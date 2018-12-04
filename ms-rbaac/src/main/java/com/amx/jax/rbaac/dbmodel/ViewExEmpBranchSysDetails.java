package com.amx.jax.rbaac.dbmodel;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "VW_EX_EMP_BRANCH_SYS_DTLS")
public class ViewExEmpBranchSysDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7198851528461302648L;

	@Id
	@Column(name = "SEQ_ID")
	private BigDecimal seqId;

	@Column(name = "EMPLOYEE_ID")
	private BigDecimal employeeId;

	@Column(name = "EMPLOYEE_NAME")
	private String employeeName;

	@Column(name = "SYSTEM_NAME")
	private String SystemName;

	@Column(name = "BRANCH_ID")
	private BigDecimal branchId;

	@Column(name = "IP_ADDRESS")
	private BigDecimal ipAddress;

	
	
	public BigDecimal getSeqId() {
		return seqId;
	}

	public void setSeqId(BigDecimal seqId) {
		this.seqId = seqId;
	}

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getSystemName() {
		return SystemName;
	}

	public void setSystemName(String systemName) {
		SystemName = systemName;
	}

	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}

	public BigDecimal getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(BigDecimal ipAddress) {
		this.ipAddress = ipAddress;
	}
	

}
