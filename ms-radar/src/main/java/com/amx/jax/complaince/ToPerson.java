package com.amx.jax.complaince;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="to_person")
@XmlType(propOrder={"gender","title","first_name","last_name","ssn","nationality1","phone","address","identification"})	
public class ToPerson {
	
	private String gender;
	private String title;
	private String first_name;
	private String last_name;
	private String ssn;
	private String nationality1;
	private List<Phones> phone = new  ArrayList<Phones>();
	private List<Address> address = new ArrayList<Address>();
	private Identification identification ;
	
	@XmlElement
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	@XmlElement
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
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
	public String getSsn() {
		return ssn;
	}
	public void setSsn(String ssn) {
		this.ssn = ssn;
	}
	@XmlElement
	public String getNationality1() {
		return nationality1;
	}
	public void setNationality1(String nationality1) {
		this.nationality1 = nationality1;
	}
	
	@XmlElement
	public Identification getIdentification() {
		return identification;
	}
	public void setIdentification(Identification identification) {
		this.identification = identification;
	}
	@XmlElementWrapper(name="phones")
    @XmlElement
	public List<Phones> getPhone() {
		return phone;
	}
	public void setPhone(List<Phones> phone) {
		this.phone = phone;
	}
	@XmlElementWrapper(name="addresses")
    @XmlElement
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	
	
	public ToPerson(String gender, String title, String first_name, String last_name, String ssn, String nationality1,
			List<Phones> phone, List<Address> address, Identification identification) {
		super();
		this.gender = gender;
		this.title = title;
		this.first_name = first_name;
		this.last_name = last_name;
		this.ssn = ssn;
		this.nationality1 = nationality1;
		this.phone = phone;
		this.address = address;
		this.identification = identification;
	}
	public ToPerson() {
		super();
		
	}
	
	
	

}
