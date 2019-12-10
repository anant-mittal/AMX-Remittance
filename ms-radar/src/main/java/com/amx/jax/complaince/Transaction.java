package com.amx.jax.complaince;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.sun.xml.txw2.annotation.XmlElement;

@XmlRootElement(name="transaction")
@XmlType(propOrder={"transactionnumber","transaction_location","date_transaction","teller","authorized","transmode_code","amount_local","t_from_my_client","t_to"})	
public class Transaction {
	
	private String transactionnumber;
	private String transaction_location;
	private String date_transaction;
	private String teller;
	private String authorized;
	private String transmode_code;
	private String amount_local;
	private TFromMyClient t_from_my_client;
	private Tto t_to;
	@XmlElement
	public String getTransactionnumber() {
		return transactionnumber;
	}
	public void setTransactionnumber(String transactionnumber) {
		this.transactionnumber = transactionnumber;
	}
	@XmlElement
	public String getTransaction_location() {
		return transaction_location;
	}
	public void setTransaction_location(String transaction_location) {
		this.transaction_location = transaction_location;
	}
	@XmlElement
	public String getDate_transaction() {
		return date_transaction;
	}
	public void setDate_transaction(String date_transaction) {
		this.date_transaction = date_transaction;
	}
	@XmlElement
	public String getTeller() {
		return teller;
	}
	public void setTeller(String teller) {
		this.teller = teller;
	}
	@XmlElement
	public String getAuthorized() {
		return authorized;
	}
	public void setAuthorized(String authorized) {
		this.authorized = authorized;
	}
	@XmlElement
	public String getTransmode_code() {
		return transmode_code;
	}
	public void setTransmode_code(String transmode_code) {
		this.transmode_code = transmode_code;
	}
	@XmlElement
	public String getAmount_local() {
		return amount_local;
	}
	public void setAmount_local(String amount_local) {
		this.amount_local = amount_local;
	}
	
	
	
	@XmlElement
	public TFromMyClient getT_from_my_client() {
		return t_from_my_client;
	}
	public void setT_from_my_client(TFromMyClient t_from_my_client) {
		this.t_from_my_client = t_from_my_client;
	}
	@XmlElement
	public Tto getT_to() {
		return t_to;
	}
	public void setT_to(Tto t_to) {
		this.t_to = t_to;
	}
	
	public Transaction(String transactionnumber, String transaction_location, String date_transaction, String teller,
			String authorized, String transmode_code, String amount_local, TFromMyClient t_from_my_client, Tto t_to) {
		super();
		this.transactionnumber = transactionnumber;
		this.transaction_location = transaction_location;
		this.date_transaction = date_transaction;
		this.teller = teller;
		this.authorized = authorized;
		this.transmode_code = transmode_code;
		this.amount_local = amount_local;
		this.t_from_my_client = t_from_my_client;
		this.t_to = t_to;
	}
	
	
	
	public Transaction() {
		super();
		
	}
	
	
	
	

}
