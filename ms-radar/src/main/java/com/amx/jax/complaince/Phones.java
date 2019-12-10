package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


@XmlRootElement(name="phone")
@XmlType(propOrder={"tph_contact_type","tph_communication_type","tph_country_prefix","tph_number"})	
public class Phones {

	private String tph_contact_type;
	private String tph_communication_type;
	private String tph_country_prefix;
	private String tph_number;
	@XmlElement
	public String getTph_contact_type() {
		return tph_contact_type;
	}
	public void setTph_contact_type(String tph_contact_type) {
		this.tph_contact_type = tph_contact_type;
	}
	@XmlElement
	public String getTph_communication_type() {
		return tph_communication_type;
	}
	public void setTph_communication_type(String tph_communication_type) {
		this.tph_communication_type = tph_communication_type;
	}
	@XmlElement
	public String getTph_country_prefix() {
		return tph_country_prefix;
	}
	public void setTph_country_prefix(String tph_country_prefix) {
		this.tph_country_prefix = tph_country_prefix;
	}
	@XmlElement
	public String getTph_number() {
		return tph_number;
	}
	public void setTph_number(String tph_number) {
		this.tph_number = tph_number;
	}
	public Phones(String tph_contact_type, String tph_communication_type, String tph_country_prefix,
			String tph_number) {
		super();
		this.tph_contact_type = tph_contact_type;
		this.tph_communication_type = tph_communication_type;
		this.tph_country_prefix = tph_country_prefix;
		this.tph_number = tph_number;
	}
	public Phones() {
		super();
		
	}
	
	
	
}
