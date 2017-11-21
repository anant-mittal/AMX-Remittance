package com.amx.jax.dbmodel;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


/*******************************************************************************************************************

		 File		: Customer.java
 
		 Project	: AlmullaExchange

		 Package	: com.amg.exchange.model
 
		 Created	:	
 						Date	: 29-May-2014 5:03:37 pm
		 				By		: Justin Vincent
 						Revision:
 
 		 Last Change:
 						Date	: 29-JAN-2015 
 						By		: Nazish Ehsan Hashmi
		 				Revision:

		 Description: TODO 

********************************************************************************************************************/

@Entity
@Table(name = "FS_CUSTOMER" )
public class OracleCustomer implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private BigDecimal customerId;
	
	private String title;
	private String firstName;
	private String middleName;
	private String lastName;
	private String titleLocal;
	private String firstNameLocal;
	private String middleNameLocal;
	private String lastNameLocal;
	private String gender;
	
	@Id
	@GeneratedValue(generator="fs_customer_seq",strategy=GenerationType.SEQUENCE)
	@SequenceGenerator(name="fs_customer_seq" ,sequenceName="FS_CUSTOMER_SEQ",allocationSize=1)
	@Column(name = "CUSTOMER_ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getCustomerId() {
		return this.customerId;
	}
	
	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	
	@Column(name = "TITLE", length = 10)
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Column(name = "FIRST_NAME", length = 200)
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "MIDDLE_NAME", length = 200)
	public String getMiddleName() {
		return this.middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	@Column(name = "LAST_NAME", length = 200)
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "TITLE_LOCAL", length = 200)
	public String getTitleLocal() {
		return this.titleLocal;
	}

	public void setTitleLocal(String titleLocal) {
		this.titleLocal = titleLocal;
	}

	@Column(name = "FIRST_NAME_LOCAL", length = 200)
	public String getFirstNameLocal() {
		return this.firstNameLocal;
	}

	public void setFirstNameLocal(String firstNameLocal) {
		this.firstNameLocal = firstNameLocal;
	}

	@Column(name = "MIDDLE_NAME_LOCAL", length = 200)
	public String getMiddleNameLocal() {
		return this.middleNameLocal;
	}

	public void setMiddleNameLocal(String middleNameLocal) {
		this.middleNameLocal = middleNameLocal;
	}

	@Column(name = "LAST_NAME_LOCAL", length = 200)
	public String getLastNameLocal() {
		return this.lastNameLocal;
	}

	public void setLastNameLocal(String lastNameLocal) {
		this.lastNameLocal = lastNameLocal;
	}

	@Column(name = "GENDER", length = 10)
	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
