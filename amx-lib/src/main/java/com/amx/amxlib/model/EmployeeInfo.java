package com.amx.amxlib.model;

import java.math.BigDecimal;

import com.amx.jax.AbstractModel;

public class EmployeeInfo extends AbstractModel implements Cloneable {

	private static final long serialVersionUID = 1L;

	private BigDecimal employeeId;
	private String employeeNumber;
	private String employeeName;
	private String userName;
	private String location;
	private String telephoneNumber;
	private BigDecimal countryId;
	private BigDecimal fsRoleMaster;
	private BigDecimal fsCountryBranch;
	private BigDecimal fsCompanyMaster;
	private String email;
	private String designation;
	private String isActive;
	private String civilId;

	public BigDecimal getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public BigDecimal getFsRoleMaster() {
		return fsRoleMaster;
	}

	public void setFsRoleMaster(BigDecimal fsRoleMaster) {
		this.fsRoleMaster = fsRoleMaster;
	}

	public BigDecimal getFsCountryBranch() {
		return fsCountryBranch;
	}

	public void setFsCountryBranch(BigDecimal fsCountryBranch) {
		this.fsCountryBranch = fsCountryBranch;
	}

	public BigDecimal getFsCompanyMaster() {
		return fsCompanyMaster;
	}

	public void setFsCompanyMaster(BigDecimal fsCompanyMaster) {
		this.fsCompanyMaster = fsCompanyMaster;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

}
