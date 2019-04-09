package com.amx.jax.model.response.customer;

public class CustomerModelSignupResponse {
	private String mobile;
	private String email;
	private String whatsapp;
	CustomerFlags customerFlags;
	public CustomerModelSignupResponse() {
		super();
	}

	public CustomerModelSignupResponse(String mobile, String email, String whatsapp, CustomerFlags customerFlags) {
		super();
		this.mobile = mobile;
		this.email = email;
		this.whatsapp = whatsapp;
		this.customerFlags = customerFlags;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWhatsapp() {
		return whatsapp;
	}
	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}
	public CustomerFlags getCustomerFlags() {
		return customerFlags;
	}
	public void setCustomerFlags(CustomerFlags customerFlags) {
		this.customerFlags = customerFlags;
	}
	
	
}
