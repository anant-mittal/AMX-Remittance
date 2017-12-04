package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICurrencyDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class CurrencyMasterService extends AbstractService{
	
	
	@Autowired
	ICurrencyDao currencyDao;
	
	
	public ApiResponse getCurrencyDetails(BigDecimal currencyId) {
		List<CurrencyMasterModel> currencyList = currencyDao.getCurrencyList(currencyId);
		ApiResponse response = getBlackApiResponse();
		if(currencyList.isEmpty()) {
			throw new GlobalException("Currency details not avaliable");
		} else {
			response.getData().getValues().addAll(currencyList);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("currency");
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
