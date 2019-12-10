package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;
@XmlRootElement(name="t_from_my_client")
@XmlType(propOrder={"from_funds_code","from_foreign_currency","from_person","from_country"})	
public class TFromMyClient {
	
	private String from_funds_code;
	private FromForeignCurrency from_foreign_currency; 
	private FromPerson from_person; 
	private String from_country;
	@XmlElement
	public String getFrom_funds_code() {
		return from_funds_code;
	}
	public void setFrom_funds_code(String from_funds_code) {
		this.from_funds_code = from_funds_code;
	}
	
	
	@XmlElement
	public String getFrom_country() {
		return from_country;
	}
	public FromForeignCurrency getFrom_foreign_currency() {
		return from_foreign_currency;
	}
	@XmlElement
	public void setFrom_foreign_currency(FromForeignCurrency from_foreign_currency) {
		this.from_foreign_currency = from_foreign_currency;
	}
	public FromPerson getFrom_person() {
		return from_person;
	}
	@XmlElement
	public void setFrom_person(FromPerson from_person) {
		this.from_person = from_person;
	}
	public void setFrom_country(String from_country) {
		this.from_country = from_country;
	}
	
	
	public TFromMyClient(String from_funds_code, FromForeignCurrency from_foreign_currency, FromPerson from_person,
			String from_country) {
		super();
		this.from_funds_code = from_funds_code;
		this.from_foreign_currency = from_foreign_currency;
		this.from_person = from_person;
		this.from_country = from_country;
	}
	public TFromMyClient() {
		super();
	}
	
	

}
