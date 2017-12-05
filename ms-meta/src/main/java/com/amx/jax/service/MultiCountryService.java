package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.MultiCountryDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.JAXDbCredentailsModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IMultiCounryRepository;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class MultiCountryService extends AbstractService {
	
	
	@Autowired
	IMultiCounryRepository multiCounryRepository;
	
	public ApiResponse getMultiCountryList() {
		List<JAXDbCredentailsModel> multiContryList = multiCounryRepository.getMultiCountryList();		
		ApiResponse response = getBlackApiResponse();
		
		if(multiContryList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(multiContryList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("multicountry");
		return response;
		
	}
	
	public ApiResponse getMultiCountryListByCountryID(BigDecimal countryId) {
		List<JAXDbCredentailsModel> multiContryList = multiCounryRepository.getMultiCountryListByCountryId(countryId);		
		ApiResponse response = getBlackApiResponse();
		
		if(multiContryList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(multiContryList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("multicountry");
		return response;
		
	}
	


	private List<MultiCountryDTO> convert(List<JAXDbCredentailsModel> multiContryList){
		List<MultiCountryDTO> list = new ArrayList<>();
		for (JAXDbCredentailsModel multiCountry : multiContryList) {
			MultiCountryDTO model = new MultiCountryDTO();
			model.setApplicationCountryId(multiCountry.getApplicationCountryId());
			model.setCountryName(multiCountry.getCountryName());
			model.setCountryIsoCode(multiCountry.getCountryIsoCode());
			list.add(model);
		}
		return list;
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
