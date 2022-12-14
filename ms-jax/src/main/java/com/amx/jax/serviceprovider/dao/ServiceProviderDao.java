package com.amx.jax.serviceprovider.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
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
import com.amx.jax.dbmodel.ServiceProviderSummaryModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.multitenant.MultiTenantConnectionProviderImpl;
import com.amx.jax.repository.ServiceProviderDefaultDateRepository;
import com.amx.jax.repository.ServiceProviderPartnerRepository;
import com.amx.jax.repository.ServiceProviderSummaryRepository;
import com.amx.jax.repository.ServiceProviderTempUploadRepository;
import com.amx.jax.util.DBUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ServiceProviderDao {
	private Logger logger = Logger.getLogger(ServiceProviderDao.class);
	@Autowired
	ServiceProviderPartnerRepository serviceProviderPartnerRepository;
	@Autowired
	ServiceProviderTempUploadRepository serviceProviderTempUploadRepository;
	@Autowired
	ServiceProviderSummaryRepository serviceProviderSummaryRepository;
	@Autowired
	ServiceProviderDefaultDateRepository serviceProviderConfRepository;
	@Autowired
	MultiTenantConnectionProviderImpl connectionProvider;
	@Autowired
	MetaData metaData;
	
	
	public List<ServiceProviderPartner> getServiceProviderPartner() {
		List<ServiceProviderPartner> serviceProviderPartner =  serviceProviderPartnerRepository.getServiceProviderPartner();
		return serviceProviderPartner;
		
	}
	public void saveFileUploadTemp(FileUploadTempModel fileUploadTempModel) {
		
		serviceProviderTempUploadRepository.save(fileUploadTempModel);
	}
	public void saveDataByProcedure(Date fileUploadDate, String tpcCode) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String callProcedure = "{call EX_FIND_TPC_TRNX (?,?,?)}";
			cs = connection.prepareCall(callProcedure);
			cs.setBigDecimal(1, metaData.getCountryId());
			cs.setString(2, tpcCode);
			cs.setDate(3, fileUploadDate);
			cs.executeUpdate();
			
		}catch(DataAccessException | SQLException e) {
			logger.info("Exception in procedure to save temporary data" + e.getMessage());
		}finally {
			DBUtil.closeResources(cs, connection);
		}
	}
	
	public List<ServiceProviderSummaryModel> getSummary() {
		List<ServiceProviderSummaryModel> serviceProviderSummaryModelList = serviceProviderSummaryRepository.getServiceProviderSummary();
		return serviceProviderSummaryModelList;
	}
	
	public void serviceProviderConfirmation(Date fileUploadDate, String tpcCode) {
		Connection connection = null;
		CallableStatement cs = null;
		try {
			connection = connectionProvider.getDataSource().getConnection();
			String callProcedure = "{call EX_TPC_REVENUE_FINENTRY (?,?,?)}";
			cs = connection.prepareCall(callProcedure);
			cs.setBigDecimal(1, metaData.getCountryId());
			cs.setString(2, tpcCode);
			cs.setDate(3, fileUploadDate);
			cs.executeUpdate();
			
		}catch(DataAccessException | SQLException e) {
			logger.info("Exception in procedure to save permanent data" + e.getMessage());
		}finally {
			DBUtil.closeResources(cs, connection);
		}
	}
	
	public Date getServiceProviderDefaultDate(String tpcCode) {
		Date serviceProviderConfirmDate =  serviceProviderConfRepository.getServiceProviderRevenueModel(metaData.getCountryId(),tpcCode);
		if(serviceProviderConfirmDate==null) {
			Calendar today = Calendar.getInstance();
			today.add(Calendar.DATE, -1);
			serviceProviderConfirmDate=new java.sql.Date(today.getTimeInMillis());
		}
		return serviceProviderConfirmDate;
	}
	
	public void deleteTemporaryData() {
		serviceProviderTempUploadRepository.deleteAll();
	}
	
	
}
