package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.PurposeOfRemittanceViewModel;
import com.amx.jax.repository.IPurposeOfRemittance;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class PurposeOfRemittanceService extends AbstractService{

	
	@Autowired
	IPurposeOfRemittance purposeOfRemittance;
	
	
	
	public AmxApiResponse<PurposeOfRemittanceViewModel, Object>  getPurposeOfRemittance(BigDecimal documentNumber,BigDecimal documentFinancialYear) {
		
		List<PurposeOfRemittanceViewModel> purposeOfRemittanceList = purposeOfRemittance.getPurposeOfRemittance(documentNumber, documentFinancialYear);
		if(purposeOfRemittanceList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		}
		return AmxApiResponse.buildList(purposeOfRemittanceList);	
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
