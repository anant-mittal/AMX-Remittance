package com.amx.jax.model.request.serviceprovider;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

public class Customer
{

	// Customer Object Details
	String customer_reference; // Local id for the customer

	String identity_type_desc;
	String identity_no;
	Date identity_expiry_date;
	Date date_of_birth;
	String nationality_3_digit_ISO;

	String first_name;
	String middle_name;
	String last_name;
	String gender;

	String building_no;
	String street;
	String city;
	String state;
	String district;
	String full_addrerss;
	String address_zip;
	String contact_no;
	String email;

	String customer_type; // I - Individual or C - Cooperate
	String profession;
	String employer_name;
	
	BigDecimal customerId;

	public String getCustomer_reference()
	{
		return customer_reference;
	}

	public void setCustomer_reference(String customer_reference)
	{
		this.customer_reference = customer_reference;
	}

	public String getIdentity_type_desc()
	{
		return identity_type_desc;
	}

	public void setIdentity_type_desc(String identity_type_desc)
	{
		this.identity_type_desc = identity_type_desc;
	}

	public String getIdentity_no()
	{
		return identity_no;
	}

	public void setIdentity_no(String identity_no)
	{
		this.identity_no = identity_no;
	}

	public Date getIdentity_expiry_date()
	{
		return identity_expiry_date;
	}

	public void setIdentity_expiry_date(Date identity_expiry_date)
	{
		this.identity_expiry_date = identity_expiry_date;
	}

	public Date getDate_of_birth()
	{
		return date_of_birth;
	}

	public void setDate_of_birth(Date date_of_birth)
	{
		this.date_of_birth = date_of_birth;
	}

	public String getNationality_3_digit_ISO()
	{
		return nationality_3_digit_ISO;
	}

	public void setNationality_3_digit_ISO(String nationality_3_digit_ISO)
	{
		this.nationality_3_digit_ISO = nationality_3_digit_ISO;
	}

	public String getFirst_name()
	{
		return first_name;
	}

	public void setFirst_name(String first_name)
	{
		this.first_name = first_name;
	}

	public String getMiddle_name()
	{
		return middle_name;
	}

	public void setMiddle_name(String middle_name)
	{
		this.middle_name = middle_name;
	}

	public String getLast_name()
	{
		return last_name;
	}

	public void setLast_name(String last_name)
	{
		this.last_name = last_name;
	}

	public String getGender()
	{
		return gender;
	}

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	public String getBuilding_no()
	{
		return building_no;
	}

	public void setBuilding_no(String building_no)
	{
		this.building_no = building_no;
	}

	public String getStreet()
	{
		return street;
	}

	public void setStreet(String street)
	{
		this.street = street;
	}

	public String getCity()
	{
		return city;
	}

	public void setCity(String city)
	{
		this.city = city;
	}

	public String getState()
	{
		return state;
	}

	public void setState(String state)
	{
		this.state = state;
	}

	public String getDistrict()
	{
		return district;
	}

	public void setDistrict(String district)
	{
		this.district = district;
	}

	public String getFull_addrerss()
	{
		return full_addrerss;
	}

	public void setFull_addrerss(String full_addrerss)
	{
		this.full_addrerss = full_addrerss;
	}

	public String getAddress_zip()
	{
		return address_zip;
	}

	public void setAddress_zip(String address_zip)
	{
		this.address_zip = address_zip;
	}

	public String getContact_no()
	{
		return contact_no;
	}

	public void setContact_no(String contact_no)
	{
		this.contact_no = contact_no;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getCustomer_type()
	{
		return customer_type;
	}

	public void setCustomer_type(String customer_type)
	{
		this.customer_type = customer_type;
	}

	public String getProfession()
	{
		return profession;
	}

	public void setProfession(String profession)
	{
		this.profession = profession;
	}

	public String getEmployer_name()
	{
		return employer_name;
	}

	public void setEmployer_name(String employer_name)
	{
		this.employer_name = employer_name;
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
	}

	public String getFull_name()
	{
		return StringUtils.normalizeSpace(first_name + " " + (middle_name == null ? "" : middle_name) + " "
				+ (last_name == null ? "" : last_name));
	}
	

    public String validate_customer_input()
	 {
	    // Validation is done on the filed that we think it is mandatory for any product and any country. Any conditional
	    // Fields that its existing in the API call depend on specific country or specific product is not cover here because
	    // it should be done on the country wise rules.

	    ArrayList<String> validation_error_list = new ArrayList<String>();
	    String validation_errors = "";

	    if (customer_reference == null)
	       validation_error_list.add("Customer reference is empty");

	    if (customer_type == null)
	       validation_error_list.add("Customer type is empty");

	    if (first_name == null)
	       validation_error_list.add("First name is empty");

	    if (customer_type != null && customer_type.equalsIgnoreCase("I"))
	       {
		  if (last_name == null)
		     validation_error_list.add("Last name is empty");
	       }

	    if (full_addrerss == null)
	       validation_error_list.add("Full Addrerss is empty");

	    for (int i = 0; i < validation_error_list.size(); i++)
	       {
		  if (i != 0)
		     validation_errors += "\n";
		  else
		     validation_errors = "------------- Cusotmer Validation Errors - Internal JAX Check -------------\n";

		  validation_errors = validation_errors + (i + 1)
				      + "- "
				      + validation_error_list.get(i);
	       }

	    return validation_errors;
	 }

	@Override
	public String toString()
	{
		return "Customer [customer_reference=" + customer_reference + ", identity_type_desc=" + identity_type_desc
				+ ", identity_no=" + identity_no + ", identity_expiry_date=" + identity_expiry_date + ", date_of_birth="
				+ date_of_birth + ", nationality_3_digit_ISO=" + nationality_3_digit_ISO + ", first_name=" + first_name
				+ ", middle_name=" + middle_name + ", last_name=" + last_name + ", gender=" + gender + ", building_no="
				+ building_no + ", street=" + street + ", city=" + city + ", state=" + state + ", district=" + district
				+ ", full_addrerss=" + full_addrerss + ", address_zip=" + address_zip + ", contact_no=" + contact_no
				+ ", email=" + email + ", customer_type=" + customer_type + ", profession=" + profession
				+ ", employer_name=" + employer_name + "]";
	}
}
