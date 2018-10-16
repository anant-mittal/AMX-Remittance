/**  AlMulla Exchange
  *  
  */
package com.amx.jax.userservice.validation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Viki Sanganil
 * 25-Jan-2018
 * ValidationClients.java
 */
@Service
public class ValidationClients {
	
	private final Map<String, ValidationClient> servicesByCode = new HashMap<>();

	@Autowired
	public ValidationClients(List<ValidationClient> services) {
		for (ValidationClient service : services) {
			register(service.getClientCode(), service);
		}
	}

	public void register(ValidationServiceCode serviceCode, ValidationClient service) {
		this.servicesByCode.put(serviceCode.getCode(), service);
	}

	public ValidationClient getValidationClient(ValidationServiceCode serviceCode) {
		return this.servicesByCode.get(serviceCode.getCode());
	}

	public ValidationClient getValidationClient(String service) {
		return this.servicesByCode.get(service);
	}

}
