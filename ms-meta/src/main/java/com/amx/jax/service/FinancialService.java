package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.UserFinancialYear;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IFinanceYearRespository;
import com.amx.jax.services.AbstractService;

/*
 * Author :Rabil
 */
@Service
public class FinancialService  extends AbstractService{
	
	@Autowired
	IFinanceYearRespository financialYearRepository;

	
	
	
	public ApiResponse getFinancialYear() {
		List<UserFinancialYear> financialList = financialYearRepository.getFinancialYear();
		ApiResponse response = getBlackApiResponse();
		if(financialList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(financialList);
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
