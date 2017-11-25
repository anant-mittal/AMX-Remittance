package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.services.AbstractService;

@Service
public class TransactionHistroyService extends AbstractService {
	
	
	
	@Autowired
	ITransactionHistroyDAO transactionHistroyDao;
	
	
	public ApiResponse getTransactionHistroy(BigDecimal cutomerReference,BigDecimal docfyr, BigDecimal docNumber, String fromDate,String  toDate){
		List<CustomerRemittanceTransactionView> trnxHisList = transactionHistroyDao.getTransactionHistroy(cutomerReference, docfyr, docNumber, fromDate, toDate);
		ApiResponse response = getBlackApiResponse();
		if(trnxHisList.isEmpty()) {
			throw new GlobalException("Transaction histroy not found");
		}else {
		response.getData().getValues().addAll(trnxHisList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("trnxhistroy");
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
