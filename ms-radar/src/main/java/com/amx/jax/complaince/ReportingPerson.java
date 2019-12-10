package com.amx.jax.complaince;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="reporting_person")
@XmlType(propOrder={"first_name", "last_name", "nationality1","phone", "email", "occupation"})	
public class ReportingPerson {
	
	private String first_name;
	private String last_name;
	private String nationality1;
	private List<Phone> phone = new ArrayList<Phone>();
	private String email;
	private String occupation;
	
	@XmlElement
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	@XmlElement
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	@XmlElement
	public String getNationality1() {
		return nationality1;
	}
	public void setNationality1(String nationality1) {
		this.nationality1 = nationality1;
	}
	
	@XmlElement
	public String getEmail() {
		return email;
	}
	@XmlElementWrapper(name="phones")
    @XmlElement
	public List<Phone> getPhone() {
		return phone;
	}
	public void setPhones(List<Phone> phone) {
		this.phone = phone;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@XmlElement
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public ReportingPerson(String first_name, String last_name, String nationality1, List<Phone> phone, String email,
			String occupation) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.nationality1 = nationality1;
		this.phone = phone;
		this.email = email;
		this.occupation = occupation;
	}
	public ReportingPerson() {
		super();
	}
	
	

}
