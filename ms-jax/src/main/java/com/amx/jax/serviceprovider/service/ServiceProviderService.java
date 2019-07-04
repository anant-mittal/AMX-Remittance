package com.amx.jax.serviceprovider.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.dbmodel.ServiceProviderPartner;
import com.amx.jax.response.branchuser.ServiceProviderPartnerResponse;
import com.amx.jax.serviceprovider.dao.ServiceProviderDao;
import com.amx.jax.services.AbstractService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class ServiceProviderService extends AbstractService {

	Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	ServiceProviderDao serviceProviderDao;
	
	public List<ServiceProviderPartnerResponse> getServiceProviderPartner() {
		List<ServiceProviderPartner> serviceProviderPartner = serviceProviderDao.getServiceProviderPartner();
		if(serviceProviderPartner.isEmpty()) {
			throw new GlobalException("Service provider partner list cannot be empty");
		}
		return convertServiceProviderPartner(serviceProviderPartner);
		
	}
	private List<ServiceProviderPartnerResponse> convertServiceProviderPartner(
			List<ServiceProviderPartner> serviceProviderPartnerList) {
		List<ServiceProviderPartnerResponse> output = new ArrayList<>();
		for (ServiceProviderPartner serviceProviderPartner : serviceProviderPartnerList) {
			ServiceProviderPartnerResponse serviceProviderPartnerResponse = new ServiceProviderPartnerResponse();
			serviceProviderPartnerResponse.setRecordId(serviceProviderPartner.getRecordId());
			serviceProviderPartnerResponse.setResourceName(serviceProviderPartner.getTpcName());
			serviceProviderPartnerResponse.setResourceCode(serviceProviderPartner.getTpcCode());
			output.add(serviceProviderPartnerResponse);
		}
		return output;
		
	}
	
	public BoolRespModel uploadServiceProviderFile() {
		//List<ServiceProviderPartner> serviceProviderPartner = serviceProviderDao.uploadServiceProviderFile();
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		return boolRespModel;
		
	}
	
	
}
