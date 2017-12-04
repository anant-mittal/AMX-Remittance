package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.RemittanceTransactionView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IRemittanceTransactionDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class RemittanceTransactionService extends AbstractService {

	@Autowired
	IRemittanceTransactionDao remittanceTransactionDao;

	public ApiResponse getRemittanceTransactionDetails(BigDecimal collectionDocumentNo, BigDecimal fYear,BigDecimal collectionDocumentCode) {

		List<RemittanceTransactionView> transctionDetail = remittanceTransactionDao.getRemittanceTransaction(collectionDocumentNo, fYear, collectionDocumentCode);
		ApiResponse response = getBlackApiResponse();
		if (transctionDetail.isEmpty()) {
			throw new GlobalException("Transaction details not avaliable");
		} else {
			response.getData().getValues().addAll(transctionDetail);
			response.setResponseStatus(ResponseStatus.OK);
		}

		response.getData().setType("remittance_transaction");
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