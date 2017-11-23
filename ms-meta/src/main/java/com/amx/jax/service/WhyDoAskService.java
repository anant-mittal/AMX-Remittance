package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.WhyDoAskInformation;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IWhyDoAskInformationRepository;
import com.amx.jax.services.AbstractService;

/**
 * 
 * @author :Rabil
 *
 */
@Service
public class WhyDoAskService extends AbstractService{
		
	@Autowired
	IWhyDoAskInformationRepository whyDoAskInformationRepository;
	
	public ApiResponse getWhyDoAskInformation(BigDecimal languageId,BigDecimal countryId){
		List<WhyDoAskInformation> termsConditionList = whyDoAskInformationRepository.getwhyDoAskInformation(languageId,countryId);
		ApiResponse response = getBlackApiResponse();
		if(termsConditionList.isEmpty()) {
			throw new GlobalException("Info not avaliable");
		}else {
		response.getData().getValues().addAll(termsConditionList);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
		}
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
