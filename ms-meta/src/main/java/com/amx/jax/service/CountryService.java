package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.services.AbstractService;
/**
 * 
 * @author Rabil
 *
 */

@Service
public class CountryService extends AbstractService {
	@Autowired
	CountryRepository countryRepository;
	
	
	
	public ApiResponse getCountryListResponse(){
		List<CountryMasterView> countryList = countryRepository.findAll();
		ApiResponse response = getBlackApiResponse();
		if(countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		}else {
		response.getData().getValues().addAll(countryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return response;
	}
	
	
	
	public ApiResponse getCountryListByLanguageIdResponse(BigDecimal languageId){
		List<CountryMasterView> countryList = countryRepository.findByLanguageId(languageId);
		ApiResponse response = getBlackApiResponse();
		if(countryList.isEmpty()) {
			throw new GlobalException("Country list is not abaliable");
		}else {
		response.getData().getValues().addAll(countryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return response;
	}
	
	
	public ApiResponse getCountryByLanguageIdAndCountryIdResponse(BigDecimal languageId,BigDecimal countryId){
		List<CountryMasterView> countryList = countryRepository.findByLanguageIdAndCountryId(languageId,countryId);
		ApiResponse response = getBlackApiResponse();
		if(countryList.isEmpty()) {
			throw new GlobalException("Country is not abaliable");
		}else {
		response.getData().getValues().addAll(countryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return response;
	}
	

	
	public ApiResponse getBusinessCountryResponse(BigDecimal languageId){
		List<CountryMasterView> countryList = countryRepository.getBusinessCountry(languageId);
		ApiResponse response = getBlackApiResponse();
		if(countryList.isEmpty()) {
			throw new GlobalException("Business country is not abaliable");
		}else {
		response.getData().getValues().addAll(countryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("country");
		return response;
	}
	


	@Override
	public String getModelType() {
		return "country";
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}
	
	
	
	
	
	
	

	
	
	
	
	
	

}
