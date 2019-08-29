package com.amx.jax.model.response.benebranch;

public class BeneStatusDto {

	String description;
	String statusCode;
	
	
	
	public BeneStatusDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BeneStatusDto(String description, String statusCode) {
		super();
		this.description = description;
		this.statusCode = statusCode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	
}
