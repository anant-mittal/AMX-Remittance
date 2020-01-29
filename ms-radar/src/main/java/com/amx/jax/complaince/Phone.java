package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlType;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName="phone")
@XmlType(propOrder={"tph_contact_type","tph_communication_type","tph_number"})	
public class Phone {
	
	private String tph_contact_type;
	private String tph_communication_type;
	private String tph_number;
	@JacksonXmlProperty
	public String getTph_contact_type() {
		return tph_contact_type;
	}
	public void setTph_contact_type(String tph_contact_type) {
		this.tph_contact_type = tph_contact_type;
	}
	@JacksonXmlProperty
	public String getTph_communication_type() {
		return tph_communication_type;
	}
	public void setTph_communication_type(String tph_communication_type) {
		this.tph_communication_type = tph_communication_type;
	}
	@JacksonXmlProperty
	public String getTph_number() {
		return tph_number;
	}
	public void setTph_number(String tph_number) {
		this.tph_number = tph_number;
	}
	public Phone(String tph_contact_type, String tph_communication_type, String tph_number) {
		super();
		this.tph_contact_type = tph_contact_type;
		this.tph_communication_type = tph_communication_type;
		this.tph_number = tph_number;
	}
	public Phone() {
		super();
		
	}
	
	
	
}
