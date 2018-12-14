package com.amx.jax.model.response.fx;

import java.math.BigDecimal;
import java.sql.Clob;

import com.amx.jax.model.AbstractModel;

public class FxEmployeeDetailsDto extends AbstractModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	BigDecimal employeeId;
	String employeeName;
	String userName;
	String location;
	String telephone;
	BigDecimal countryBranchId;
	BigDecimal roleId;
	BigDecimal countryId;
	String employeeNumber;
	String email;
	Clob employeeSignature;
	String allowFcTrnx;
	String ipaddress;
	String cashierOption;
	String userType;
	String isActive;
	String designation;
	String deleteUser;
	String allocateInd;
	String civilId;
	BigDecimal employeeCountryId;
	String wuAccountId;
	String wuForeignTerminalId;
	String wuNaId;
	String wuTerminalId;
	BigDecimal areaCode;
	String branchIpaddress;
	String scanId;
	String digitalSignatureInd;
	String wuAccountCode;
	BigDecimal branchId;

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
	
	public BigDecimal getBranchId() {
		return branchId;
	}

	public void setBranchId(BigDecimal branchId) {
		this.branchId = branchId;
	}
	
}
