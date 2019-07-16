package com.amx.jax.serviceprovider.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.FileUploadTempModel;
import com.amx.jax.dbmodel.ServiceProviderPartner;
import com.amx.jax.repository.ServiceProviderPartnerRepository;
import com.amx.jax.repository.ServiceProviderTempUploadRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceProviderDao {
	private Logger logger = Logger.getLogger(ServiceProviderDao.class);
	@Autowired
	ServiceProviderPartnerRepository serviceProviderPartnerRepository;
	@Autowired
	ServiceProviderTempUploadRepository serviceProviderTempUploadRepository;
	
	public List<ServiceProviderPartner> getServiceProviderPartner() {
		List<ServiceProviderPartner> serviceProviderPartner =  serviceProviderPartnerRepository.getServiceProviderPartner();
		return serviceProviderPartner;
		
	}
	public void saveFileUploadTemp(FileUploadTempModel fileUploadTempModel) {
		
		serviceProviderTempUploadRepository.save(fileUploadTempModel);
	}
	
}
