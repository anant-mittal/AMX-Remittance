package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.TermsAndConditionDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.TermsAndCondition;
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
		response.getData().getValues().addAll(convert(termsConditionList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("terms");
		return response;
	}
	
	
	public ApiResponse getTermsAndConditionAsPerCountry(BigDecimal languageId,BigDecimal countryId) {
		List<TermsAndCondition> termsConditionList = termsAndCondition.getTermsAndConditionBasedOnCountry(languageId, countryId);
		ApiResponse response = getBlackApiResponse();
		if(termsConditionList.isEmpty()) {
			throw new GlobalException("Terms and Condition is not abaliable");
		}else {
		response.getData().getValues().addAll(convert(termsConditionList));
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("terms");
		return response;
	}
	
	
	
	
	private List<TermsAndConditionDTO> convert(List<TermsAndCondition> termsConditionList) {
		List<TermsAndConditionDTO> list = new ArrayList<TermsAndConditionDTO>();
		for (TermsAndCondition terms : termsConditionList) {
			TermsAndConditionDTO model = new TermsAndConditionDTO();
			model.setCompanyId(terms.getCompanyId());
			model.setCountryId(terms.getCountryId());
			model.setDescription(terms.getDescription());
			model.setLanguageId(terms.getLanguageId());
			model.setStatus(terms.getStatus());
			model.setTermsConditionId(terms.getTermsConditionId());
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
