package com.amx.jax.grid.views;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import com.amx.jax.dict.ContactType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ContactVerificationReport implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "EMP_NAME")
	private String employeeName;

	@Column(name = "AREA_NAME")
	private String areaName;

	@Column(name = "BRANCH_NAME")
	private String branchName;

	/*
	 * @Column(name = "EMPLOYEE_NAME") private String employeeName;
	 * 
	 * @Column(name = "BRANCH_NAME") private String branchName;
	 * 
	 * @Column(name = "AREA_NAME") private String areaName;
	 * 
	 * @Column(name = "ISACTIVE")
	 * 
	 * @Enumerated(value = EnumType.STRING) private Status isActive;
	 * 
	 */

	@Column(name = "CONTACT_TYPE")
	@Enumerated(value = EnumType.STRING)
	private ContactType contactType;

	@Column(name = "SENT_COUNT")
	private BigDecimal sent;

	@Column(name = "CUSTOMER_COUNT")
	private BigDecimal customers;

	@Column(name = "VERIFIED_COUNT")
	private BigDecimal verified;

	public ContactType getContactType() {
		return contactType;
	}

	public void setContactType(ContactType contactType) {
		this.contactType = contactType;
	}

	public BigDecimal getCustomers() {
		return customers;
	}

	public void setCustomers(BigDecimal customers) {
		this.customers = customers;
	}

	public BigDecimal getVerified() {
		return verified;
	}

	public void setVerified(BigDecimal verified) {
		this.verified = verified;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public BigDecimal getSent() {
		return sent;
	}

	public void setSent(BigDecimal sent) {
		this.sent = sent;
	}

}
