package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IApplicationCountryRepository;
import com.amx.jax.services.AbstractService;

@Service
public class ApplicationCountryService  extends AbstractService{
	
	
	@Autowired
	IApplicationCountryRepository applicationCountryRepository;
	

	
	public ApiResponse getApplicationCountryListResponse(){
		List<ApplicationSetup> appCountryList = applicationCountryRepository.findAll();
		ApiResponse response = getBlackApiResponse();
		if(appCountryList.isEmpty()) {
			throw new GlobalException("Application country is not set");
		}else {
		response.getData().getValues().addAll(appCountryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("appl_country");
		return response;
	}
	
	
	
	
	
	public ApiResponse getApplicationCountryResponse(BigDecimal companyId,BigDecimal countryId){
		List<ApplicationSetup> appCountryList = applicationCountryRepository.findByCountryIdAndCompanyId(companyId,countryId);
		ApiResponse response = getBlackApiResponse();
		if(appCountryList.isEmpty()) {
			throw new GlobalException("Application list is not abaliable");
		}else {
		response.getData().getValues().addAll(appCountryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("appl_country");
		return response;
	}
	
	
	
	

	@Override
	public String getModelType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

}
