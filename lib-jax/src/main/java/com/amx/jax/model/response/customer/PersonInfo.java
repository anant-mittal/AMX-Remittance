package com.amx.jax.model.response.customer;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.amx.jax.model.AbstractModel;


public class PersonInfo extends AbstractModel implements Cloneable {

	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String titleLocal;
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String gender;
	private Date dateOfBirth;
	private String alterEmailId;
	private String mobile;
	private String email;
	private BigDecimal nationalityId;
	private Timestamp lastLoginTime;
	private String identityInt;

	
	
	/**
	 * 
	 */
	public PersonInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param firstName
	 * @param middleName
	 * @param lastName
	 * @param email
	 */
	public PersonInfo(String firstName, String middleName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getTitleLocal() {
		return titleLocal;
	}

	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}

	public String getFirstNameLocal() {
		return firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	public String getMiddleNameLocal() {
		return middleNameLocal;
	}

	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}

	public String getLastNameLocal() {
		return lastNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getAlterEmailId() {
		return alterEmailId;
	}

	public void setAlterEmailId(String alterEmailId) {
		this.alterEmailId = alterEmailId;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getNationalityId() {
		return nationalityId;
	}

	public void setNationalityId(BigDecimal nationalityId) {
		this.nationalityId = nationalityId;
	}

	public Timestamp getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Timestamp lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

}
