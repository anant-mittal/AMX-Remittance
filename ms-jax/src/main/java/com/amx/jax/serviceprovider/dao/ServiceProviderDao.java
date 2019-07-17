package com.amx.jax.serviceprovider.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.FileUploadTempModel;
import com.amx.jax.dbmodel.ServiceProviderPartner;
import com.amx.jax.meta.MetaData;
import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;
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
	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;
	@Autowired
	MetaData metaData;
	
	public List<ServiceProviderPartner> getServiceProviderPartner() {
		List<ServiceProviderPartner> serviceProviderPartner =  serviceProviderPartnerRepository.getServiceProviderPartner();
		return serviceProviderPartner;
		
	}
	public void saveFileUploadTemp(FileUploadTempModel fileUploadTempModel, Date fileUploadDate, String tpcCode) {
		
		serviceProviderTempUploadRepository.save(fileUploadTempModel);
		
		
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String callProcedure = "{call EX_FIND_TPC_TRNX (?,?,?)}";
			cs = connection.prepareCall(callProcedure);
			cs.setBigDecimal(1, metaData.getCountryId());
			cs.setString(2, tpcCode);
			cs.setDate(3, fileUploadDate);
			cs.execute();
			
		}catch(DataAccessException | SQLException e) {
			logger.info("Exception in procedure to save temporary data" + e.getMessage());
		}
		
		
	}
	
}
