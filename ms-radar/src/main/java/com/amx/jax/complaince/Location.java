package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="location")
@XmlType(propOrder={"address_type", "address", "city","country_code"})	
public class Location {
	
	private String address_type;
	private String address;
	private String city;
	private String country_code;
	@XmlElement
	public String getAddress_type() {
		return address_type;
	}
	public void setAddress_type(String address_type) {
		this.address_type = address_type;
	}
	@XmlElement
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@XmlElement
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	@XmlElement
	public String getCountry_code() {
		return country_code;
	}
	public void setCountry_code(String country_code) {
		this.country_code = country_code;
	}
	public Location(String address_type, String address, String city, String country_code) {
		super();
		this.address_type = address_type;
		this.address = address;
		this.city = city;
		this.country_code = country_code;
	}
	public Location() {
		super();
		
	}
	
	

}
