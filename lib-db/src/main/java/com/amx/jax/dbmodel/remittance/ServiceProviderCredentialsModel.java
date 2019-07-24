package com.amx.jax.dbmodel.remittance;


import java.io.Serializable;
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
@Table(name = "EX_SERVICE_PROVIDER_CRED")
public class ServiceProviderCredentialsModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private BigDecimal serviceProviderCreditId;
	private BigDecimal employeeId;
	private String employeeUserName;
	private BigDecimal serviceProviderId;
	private String username;
	private String password;
	private String loginCredential1;
	private String loginCredential2;
	private String loginCredential3;
	private String loginCredential4;
	private String loginCredential5;
	private String loginCredential6;
	private String createdBy;
	private Date createdDate;
	private String modifiedBy;
	private Date modifiedDate;
	private String isActive;
	
	@Id
	@GeneratedValue(generator="ex_service_provider_cred_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="ex_service_provider_cred_seq",sequenceName="EX_SERVICE_PROVIDER_CRED_SEQ",allocationSize=1)
	@Column(name = "EX_SERVICE_PROVIDER_CRED_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getServiceProviderCreditId() {
		return serviceProviderCreditId;
	}
	public void setServiceProviderCreditId(BigDecimal serviceProviderCreditId) {
		this.serviceProviderCreditId = serviceProviderCreditId;
	}
	
	@Column(name = "EMPLOYEE_ID")
	public BigDecimal getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(BigDecimal employeeId) {
		this.employeeId = employeeId;
	}
	
	@Column(name = "EMOS_USER_NAME")
	public String getEmployeeUserName() {
		return employeeUserName;
	}
	public void setEmployeeUserName(String employeeUserName) {
		this.employeeUserName = employeeUserName;
	}
	
	@Column(name = "SERVICE_PROVIDER_ID")
	public BigDecimal getServiceProviderId() {
		return serviceProviderId;
	}
	public void setServiceProviderId(BigDecimal serviceProviderId) {
		this.serviceProviderId = serviceProviderId;
	}
	
	@Column(name = "USER_NAME")
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
	@Column(name = "PASS_WORD")
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	@Column(name = "LOGIN_CRED1")
	public String getLoginCredential1() {
		return loginCredential1;
	}
	public void setLoginCredential1(String loginCredential1) {
		this.loginCredential1 = loginCredential1;
	}
	
	@Column(name = "LOGIN_CRED2")
	public String getLoginCredential2() {
		return loginCredential2;
	}
	public void setLoginCredential2(String loginCredential2) {
		this.loginCredential2 = loginCredential2;
	}
	
	@Column(name = "LOGIN_CRED3")
	public String getLoginCredential3() {
		return loginCredential3;
	}
	public void setLoginCredential3(String loginCredential3) {
		this.loginCredential3 = loginCredential3;
	}
	
	@Column(name = "LOGIN_CRED4")
	public String getLoginCredential4() {
		return loginCredential4;
	}
	public void setLoginCredential4(String loginCredential4) {
		this.loginCredential4 = loginCredential4;
	}
	
	@Column(name = "LOGIN_CRED5")
	public String getLoginCredential5() {
		return loginCredential5;
	}
	public void setLoginCredential5(String loginCredential5) {
		this.loginCredential5 = loginCredential5;
	}
	
	@Column(name = "LOGIN_CRED6")
	public String getLoginCredential6() {
		return loginCredential6;
	}
	public void setLoginCredential6(String loginCredential6) {
		this.loginCredential6 = loginCredential6;
	}
	
	@Column(name = "CREATED_BY")
	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Column(name = "CREATED_DATE")
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	
	@Column(name = "MODIFIED_BY")
	public String getModifiedBy() {
		return modifiedBy;
	}
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	@Column(name = "MODIFIED_DATE")
	public Date getModifiedDate() {
		return modifiedDate;
	}
	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}
	
	@Column(name = "ISACTIVE")
	public String getIsActive() {
		return isActive;
	}
	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}
