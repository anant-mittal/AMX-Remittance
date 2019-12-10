package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="identification")
@XmlType(propOrder={"type","number","issue_country"})	
public class Identification {
	
	private String type;
	private String number;
	private String issue_country;
	
	@XmlElement
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	
	}
	@XmlElement
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	@XmlElement
	public String getIssue_country() {
		return issue_country;
	}
	public void setIssue_country(String issue_country) {
		this.issue_country = issue_country;
	}
	public Identification(String type, String number, String issue_country) {
		super();
		this.type = type;
		this.number = number;
		this.issue_country = issue_country;
	}
	public Identification() {
		super();
		
	}
	
	
	

}
