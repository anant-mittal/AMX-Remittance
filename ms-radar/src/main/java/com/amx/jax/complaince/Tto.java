package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="t_to")
@XmlType(propOrder={"to_funds_code","to_foreign_currency","to_person","to_country"})	
public class Tto {
	
	private String to_funds_code;
	private ToForeignCurrency to_foreign_currency; 
	private ToPerson to_person;
	private String to_country;
	
	@XmlElement
	public String getTo_funds_code() {
		return to_funds_code;
	}
	public void setTo_funds_code(String to_funds_code) {
		this.to_funds_code = to_funds_code;
	}
	@XmlElement
	public ToForeignCurrency getTo_foreign_currency() {
		return to_foreign_currency;
	}
	public void setTo_foreign_currency(ToForeignCurrency to_foreign_currency) {
		this.to_foreign_currency = to_foreign_currency;
	}
	@XmlElement
	public ToPerson getTo_person() {
		return to_person;
	}
	public void setTo_person(ToPerson to_person) {
		this.to_person = to_person;
	}
	@XmlElement
	public String getTo_country() {
		return to_country;
	}
	public void setTo_country(String to_country) {
		this.to_country = to_country;
	}
	
	public Tto(String to_funds_code, ToForeignCurrency to_foreign_currency, ToPerson to_person, String to_country) {
		super();
		this.to_funds_code = to_funds_code;
		this.to_foreign_currency = to_foreign_currency;
		this.to_person = to_person;
		this.to_country = to_country;
	}
	public Tto() {
		super();
		
	}

	

}
