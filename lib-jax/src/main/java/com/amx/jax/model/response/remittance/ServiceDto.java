package com.amx.jax.model.response.remittance;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceDto implements Serializable{


	@JsonProperty("servicecode")
	String serviceCode;
	@JsonProperty("servicedescription")
	String serviceDescription;
	@JsonProperty("servicemasterid")
	String serviceMasterId;
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public String getServiceDescription() {
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription) {
		this.serviceDescription = serviceDescription;
	}
	public String getServiceMasterId() {
		return serviceMasterId;
	}
	public void setServiceMasterId(String serviceMasterId) {
		this.serviceMasterId = serviceMasterId;
	}
	
	
}

