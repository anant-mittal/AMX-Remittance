package com.amx.jax.dbmodel.fx;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Clob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "JAX_EMPLOYEE_DETAILS")
public class EmployeeDetailsView implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "EMPLOYEE_ID")
	BigDecimal employeeId;
	
	@Column(name = "EMPLOYEE_NAME")
	String employeeName;
	
	@Column(name = "USER_NAME")
	String userName;
	
	@Column(name = "LOCATION")
	String location;
	
	@Column(name = "TELEPHONE")
	String telephone;
	
	@Column(name = "COUNTRY_BRANCH_ID")
	BigDecimal countryBranchId;
	
	@Column(name = "ROLE_ID")
	BigDecimal roleId;
	
	@Column(name = "COUNTRY_ID")
	BigDecimal countryId;
	
	@Column(name = "EMPLOYEE_NUMBER")
	String employeeNumber;
	
	@Column(name = "EMAIL")
	String email;
	
	@Column(name = "SIGNATURE_SPECIMEN_CLOB")
	Clob employeeSignature;
	
	@Column(name = "ALLOW_FC_TRNX")
	String allowFcTrnx;
	
	@Column(name = "IP_ADDRESS")
	String ipaddress;
	
	@Column(name = "CASHIER_OPTN")
	String cashierOption;
	
	@Column(name = "USER_TYPE")
	String userType;
	
	@Column(name = "ISACTIVE")
	String isActive;
	
	@Column(name = "DESIGNATION")
	String designation;
	
	@Column(name = "DELETED_USER")
	String deleteUser;
	
	@Column(name = "ALLOCATE_IND")
	String allocateInd;
	
	@Column(name = "CIVIL_ID")
	String civilId;
	
	@Column(name = "EMPLOYEE_COUNTRY_ID")
	BigDecimal employeeCountryId;
	
	@Column(name = "WU_ACCOUNT_ID")
	String wuAccountId;
	
	@Column(name = "WU_FOREIGN_TERMINAL_ID")
	String wuForeignTerminalId;
	
	@Column(name = "WU_NA_ID")
	String wuNaId;
	
	@Column(name = "WU_TERMINAL_ID")
	String wuTerminalId;
	
	@Column(name = "AREA_CODE")
	BigDecimal areaCode;
	
	@Column(name = "BRANCH_IP_ADDRESS")
	String branchIpaddress;
	
	@Column(name = "SCAN_IND")
	String scanId;
	
	@Column(name = "DIGITAL_SIGNATURE_IND")
	String digitalSignatureInd;
	
	@Column(name = "WU_ACCOUNT_CODE")
	String wuAccountCode;

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

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public BigDecimal getCountryBranchId() {
		return countryBranchId;
	}

	public void setCountryBranchId(BigDecimal countryBranchId) {
		this.countryBranchId = countryBranchId;
	}

	public BigDecimal getRoleId() {
		return roleId;
	}

	public void setRoleId(BigDecimal roleId) {
		this.roleId = roleId;
	}

	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Clob getEmployeeSignature() {
		return employeeSignature;
	}

	public void setEmployeeSignature(Clob employeeSignature) {
		this.employeeSignature = employeeSignature;
	}

	public String getAllowFcTrnx() {
		return allowFcTrnx;
	}

	public void setAllowFcTrnx(String allowFcTrnx) {
		this.allowFcTrnx = allowFcTrnx;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getCashierOption() {
		return cashierOption;
	}

	public void setCashierOption(String cashierOption) {
		this.cashierOption = cashierOption;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getDeleteUser() {
		return deleteUser;
	}

	public void setDeleteUser(String deleteUser) {
		this.deleteUser = deleteUser;
	}

	public String getAllocateInd() {
		return allocateInd;
	}

	public void setAllocateInd(String allocateInd) {
		this.allocateInd = allocateInd;
	}

	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	public BigDecimal getEmployeeCountryId() {
		return employeeCountryId;
	}

	public void setEmployeeCountryId(BigDecimal employeeCountryId) {
		this.employeeCountryId = employeeCountryId;
	}

	public String getWuAccountId() {
		return wuAccountId;
	}

	public void setWuAccountId(String wuAccountId) {
		this.wuAccountId = wuAccountId;
	}

	public String getWuForeignTerminalId() {
		return wuForeignTerminalId;
	}

	public void setWuForeignTerminalId(String wuForeignTerminalId) {
		this.wuForeignTerminalId = wuForeignTerminalId;
	}

	public String getWuNaId() {
		return wuNaId;
	}

	public void setWuNaId(String wuNaId) {
		this.wuNaId = wuNaId;
	}

	public String getWuTerminalId() {
		return wuTerminalId;
	}

	public void setWuTerminalId(String wuTerminalId) {
		this.wuTerminalId = wuTerminalId;
	}

	public BigDecimal getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(BigDecimal areaCode) {
		this.areaCode = areaCode;
	}

	public String getBranchIpaddress() {
		return branchIpaddress;
	}

	public void setBranchIpaddress(String branchIpaddress) {
		this.branchIpaddress = branchIpaddress;
	}

	public String getScanId() {
		return scanId;
	}

	public void setScanId(String scanId) {
		this.scanId = scanId;
	}

	public String getDigitalSignatureInd() {
		return digitalSignatureInd;
	}

	public void setDigitalSignatureInd(String digitalSignatureInd) {
		this.digitalSignatureInd = digitalSignatureInd;
	}

	public String getWuAccountCode() {
		return wuAccountCode;
	}

	public void setWuAccountCode(String wuAccountCode) {
		this.wuAccountCode = wuAccountCode;
	}

}
