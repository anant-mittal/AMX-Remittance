package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.TermsAndCondition;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ITermsAndConditionRepository;
import com.amx.jax.services.AbstractService;

/**
 * 
 * @author :Rabil
 *
 */

@Service
public class TermsAndConditionService extends AbstractService{
	
	@Autowired
	ITermsAndConditionRepository termsAndCondition;
	
	public ApiResponse getTermsAndCondition(BigDecimal languageId) {
		List<TermsAndCondition> termsConditionList = termsAndCondition.getTermsAndCondition(languageId);
		ApiResponse response = getBlackApiResponse();
		if(termsConditionList.isEmpty()) {
			throw new GlobalException("Terms and Condition is not abaliable");
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
