package com.amx.jax.complaince;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName="reporting_person")
@XmlType(propOrder={"first_name", "last_name", "nationality1","phone", "occupation"})	
public class ReportingPerson {
	
	private String first_name;
	private String last_name;
	private String nationality1;
	private List<Phone> phone = new ArrayList<Phone>();
	private String occupation;
	
	@JacksonXmlProperty
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	@JacksonXmlProperty
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	@JacksonXmlProperty
	public String getNationality1() {
		return nationality1;
	}
	public void setNationality1(String nationality1) {
		this.nationality1 = nationality1;
	}
	
	@JacksonXmlElementWrapper(localName = "phones")
	public List<Phone> getPhone() {
		return phone;
	}
	public void setPhones(List<Phone> phone) {
		this.phone = phone;
	}

	@JacksonXmlProperty
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	
	public ReportingPerson(String first_name, String last_name, String nationality1, List<Phone> phone,
			String occupation) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.nationality1 = nationality1;
		this.phone = phone;
		this.occupation = occupation;
	}
	public ReportingPerson() {
		super();
	}
	
	

}
