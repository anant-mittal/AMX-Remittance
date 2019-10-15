package com.amx.jax.dbmodel;

import java.math.BigDecimal;
import java.sql.Clob;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "FS_EMPLOYEE")
public class Employee implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private BigDecimal employeeId;
	private String employeeNumber;
	private String employeeName;
	private String userName;
	private String password;
	private String location;
	private String telephoneNumber;
	private BigDecimal countryId;
	private BigDecimal fsRoleMaster;
	private BigDecimal fsCountryBranch;
	private BigDecimal fsCompanyMaster;
	private String sesionStatus;
	private String email;
	@JsonIgnore
	private Clob signatureSpecimenClob;
	private String allowFcTransaction;
	private String ipAddress;
	private String cashierOpt;
	private BigDecimal wuUsername;
	private String wuPassword;
	private String userType;
	private String designation;
	private String isActive;
	private String deletedUser;
	private String civilId;
	private BigDecimal lockCnt;
	private Date lockDt;
	//private Date tokenDate;
	//private BigDecimal tokenSentCount;
	//private String emailToken;
	//private String smsToken;

	@Id
	@GeneratedValue(generator = "fs_employee_seq", strategy = GenerationType.SEQUENCE)
	@SequenceGenerator(name = "fs_employee_seq", sequenceName = "FS_EMPLOYEE_SEQ", allocationSize = 1)
	@Column(name = "EMPLOYEE_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getEmployeeId() {
		return this.employeeId;
	}

	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}

	@Column(name = "EMPLOYEE_NAME", length = 50)
	public String getEmployeeName() {
		return this.employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	@Column(name = "USER_NAME", length = 20, unique = true)
	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Column(name = "PASSWORD", length = 20)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name = "LOCATION")
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Column(name = "TELEPHONE")
	public String getTelephoneNumber() {
		return telephoneNumber;
	}

	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	@Column(name = "COUNTRY_ID")
	public BigDecimal getCountryId() {
		return countryId;
	}

	public void setCountryId(BigDecimal countryId) {
		this.countryId = countryId;
	}

	@Column(name = "ROLE_ID")
	public BigDecimal getFsRoleMaster() {
		return fsRoleMaster;
	}

	public void setFsRoleMaster(BigDecimal fsRoleMaster) {
		this.fsRoleMaster = fsRoleMaster;
	}

	@Column(name = "COUNTRY_BRANCH_ID")
	public BigDecimal getFsCountryBranch() {
		return fsCountryBranch;
	}

	public void setFsCountryBranch(BigDecimal fsCountryBranch) {
		this.fsCountryBranch = fsCountryBranch;
	}

	@Column(name = "Employee_Number")
	public String getEmployeeNumber() {
		return employeeNumber;
	}

	public void setEmployeeNumber(String employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	@Column(name = "SESSION_STATUS")
	public String getSesionStatus() {
		return sesionStatus;
	}

	public void setSesionStatus(String sesionStatus) {
		this.sesionStatus = sesionStatus;
	}

	@Column(name = "COMPANY_ID")
	public BigDecimal getFsCompanyMaster() {
		return fsCompanyMaster;
	}

	public void setFsCompanyMaster(BigDecimal fsCompanyMaster) {
		this.fsCompanyMaster = fsCompanyMaster;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "SIGNATURE_SPECIMEN_CLOB")
	public Clob getSignatureSpecimenClob() {
		return signatureSpecimenClob;
	}

	public void setSignatureSpecimenClob(Clob signatureSpecimenClob) {
		this.signatureSpecimenClob = signatureSpecimenClob;
	}

	@Column(name = "ALLOW_FC_TRNX")
	public String getAllowFcTransaction() {
		return allowFcTransaction;
	}

	public void setAllowFcTransaction(String allowFcTransaction) {
		this.allowFcTransaction = allowFcTransaction;
	}

	@Column(name = "IP_ADDRESS")
	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Column(name = "CASHIER_OPTN")
	public String getCashierOpt() {
		return cashierOpt;
	}

	public void setCashierOpt(String cashierOpt) {
		this.cashierOpt = cashierOpt;
	}

	@Column(name = "WU_USERNAME")
	public BigDecimal getWuUsername() {
		return wuUsername;
	}

	public void setWuUsername(BigDecimal wuUsername) {
		this.wuUsername = wuUsername;
	}

	@Column(name = "WU_PASSWORD")
	public String getWuPassword() {
		return wuPassword;
	}

	public void setWuPassword(String wuPassword) {
		this.wuPassword = wuPassword;
	}

	@Column(name = "USER_TYPE")
	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	@Column(name = "DESIGNATION")
	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

	@Column(name = "DELETED_USER")
	public String getDeletedUser() {
		return deletedUser;
	}

	public void setDeletedUser(String deletedUser) {
		this.deletedUser = deletedUser;
	}

	@Column(name = "CIVIL_ID")
	public String getCivilId() {
		return civilId;
	}

	public void setCivilId(String civilId) {
		this.civilId = civilId;
	}

	@Column(name = "LOCK_CNT")
	public BigDecimal getLockCnt() {
		return lockCnt;
	}

	public void setLockCnt(BigDecimal lockCnt) {
		this.lockCnt = lockCnt;
	}

	@Column(name = "LOCK_DATE")
	public Date getLockDt() {
		return lockDt;
	}

	public void setLockDt(Date lockDt) {
		this.lockDt = lockDt;
	}
	/*
	 * @Column(name = "TOKEN_DATE") public Date getTokenDate() { return tokenDate; }
	 * public void setTokenDate(Date tokenDate) { this.tokenDate = tokenDate; }
	 * 
	 * @Column(name = "TOKEN_SENT_CNT") public BigDecimal getTokenSentCount() {
	 * return tokenSentCount; } public void setTokenSentCount(BigDecimal
	 * tokenSentCount) { this.tokenSentCount = tokenSentCount; }
	 * 
	 * @Column(name="EMAIL_TOKEN") public String getEmailToken() { return
	 * emailToken; }
	 * 
	 * public void setEmailToken(String emailToken) { this.emailToken = emailToken;
	 * }
	 * 
	 * @Column(name="SMS_TOKEN") public String getSmsToken() { return smsToken; }
	 * 
	 * public void setSmsToken(String smsToken) { this.smsToken = smsToken; }
	 */

}
