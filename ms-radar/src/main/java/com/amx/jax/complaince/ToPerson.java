package com.amx.jax.complaince;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

@XmlRootElement(name="to_person")
@XmlType(propOrder={"title","first_name","last_name","ssn","nationality1","phone","address"})	
public class ToPerson {
	
	private String title;
	private String first_name;
	private String last_name;
	private String ssn;
	private String nationality1;
	private List<Phones> phone = new  ArrayList<Phones>();
	private List<Address> address = new ArrayList<Address>();
	
	@JacksonXmlProperty
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	
	@JacksonXmlProperty
	public String getNationality1() {
		return nationality1;
	}
	public void setNationality1(String nationality1) {
		this.nationality1 = nationality1;
	}
	
	@JacksonXmlElementWrapper(localName = "phones")
	@JacksonXmlProperty
	public List<Phones> getPhone() {
		return phone;
	}
	public void setPhone(List<Phones> phone) {
		this.phone = phone;
	}
	@JacksonXmlElementWrapper(localName = "addresses")
	@JacksonXmlProperty
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	
	
	public ToPerson(String title, String first_name, String last_name, String ssn, String nationality1,
			List<Phones> phone, List<Address> address) {
		super();
		this.title = title;
		this.first_name = first_name;
		this.last_name = last_name;
		this.ssn = ssn;
		this.nationality1 = nationality1;
		this.phone = phone;
		this.address = address;
	}
	public ToPerson() {
		super();
		
	}
	
	
	

}
