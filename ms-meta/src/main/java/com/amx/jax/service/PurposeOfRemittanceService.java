package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IPurposeOfRemittance;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class PurposeOfRemittanceService extends AbstractService{

	
	@Autowired
	IPurposeOfRemittance purposeOfRemittance;
	
	
	
	public ApiResponse getPurposeOfRemittance(BigDecimal documentNumber,BigDecimal documentFinancialYear) {
		
		List<PurposeOfRemittanceViewModel> purposeOfRemittanceList = purposeOfRemittance.getPurposeOfRemittance(documentNumber, documentFinancialYear);
		ApiResponse response = getBlackApiResponse();
		
		if(purposeOfRemittanceList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} else {
			response.getData().getValues().addAll(purposeOfRemittanceList);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("purposeOfRemittance");
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
