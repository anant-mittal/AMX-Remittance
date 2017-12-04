package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICollectionPaymentDetailsViewDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class CollectionPaymentDetailsViewService extends AbstractService {

	@Autowired
	ICollectionPaymentDetailsViewDao collectionPaymentDetailsViewDao ;
	
	public ApiResponse getCollectionPaymentDetailsFromView(BigDecimal companyId, BigDecimal documentNo,
			BigDecimal documentFinancialYear, BigDecimal documentCode){
		List<CollectionPaymentDetailsViewModel> listPaymentDetails = collectionPaymentDetailsViewDao.getCollectedPaymentDetails(companyId, documentNo, documentFinancialYear, documentCode);

		ApiResponse response = getBlackApiResponse();
		if(listPaymentDetails.isEmpty()) {
				throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
			} else {
				response.getData().getValues().addAll(listPaymentDetails);
				response.setResponseStatus(ResponseStatus.OK);
			}
			response.getData().setType("collectionPaymentDetailView");
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
