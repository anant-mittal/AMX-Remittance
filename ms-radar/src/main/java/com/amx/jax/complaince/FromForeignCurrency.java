package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="from_foreign_currency")
@XmlType(propOrder={"foreign_currency_code","foreign_amount","foreign_exchange_rate"})	
public class FromForeignCurrency {
	
	private String foreign_currency_code;
	private String foreign_amount;
	private String foreign_exchange_rate;
	@XmlElement
	public String getForeign_currency_code() {
		return foreign_currency_code;
	}
	public void setForeign_currency_code(String foreign_currency_code) {
		this.foreign_currency_code = foreign_currency_code;
	}
	@XmlElement
	public String getForeign_amount() {
		return foreign_amount;
	}
	public void setForeign_amount(String foreign_amount) {
		this.foreign_amount = foreign_amount;
	}

	@XmlElement
	public String getForeign_exchange_rate() {
		return foreign_exchange_rate;
	}
	public void setForeign_exchange_rate(String foreign_exchange_rate) {
		this.foreign_exchange_rate = foreign_exchange_rate;
	}
	public FromForeignCurrency(String foreign_currency_code, String foreign_amount, String foreign_exchange_rate) {
		super();
		this.foreign_currency_code = foreign_currency_code;
		this.foreign_amount = foreign_amount;
		this.foreign_exchange_rate = foreign_exchange_rate;
	}
	public FromForeignCurrency() {
		super();
		
	}
	
	
	
	
	

}
