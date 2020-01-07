package com.amx.jax.model.response.remittance;

import java.math.BigDecimal;

import com.amx.jax.model.ResourceDTO;

public class ServiceMasterDTO extends ResourceDTO{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String serviceCode;

	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	
	
}
