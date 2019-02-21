package com.amx.jax.pricer.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ServiceMasterDesc;
import com.amx.jax.pricer.repository.ServiceMasterDescRepository;

@Component
public class ServiceMasterDescService {

	@Autowired
	ServiceMasterDescRepository serviceMasterDescRepository;
	
	public ServiceMasterDesc getServiceById(BigDecimal serviceId) {
		return serviceMasterDescRepository.getServiceById(serviceId);
	}
}
