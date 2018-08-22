package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CollectionPaymentDetailsViewModel;
import com.amx.jax.repository.ICollectionPaymentDetailsViewDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class CollectionPaymentDetailsViewService extends AbstractService {

	@Autowired
	ICollectionPaymentDetailsViewDao collectionPaymentDetailsViewDao ;
	
	public AmxApiResponse<CollectionPaymentDetailsViewModel, Object> getCollectionPaymentDetailsFromView(BigDecimal companyId, BigDecimal documentNo,
			BigDecimal documentFinancialYear, BigDecimal documentCode){
		List<CollectionPaymentDetailsViewModel> listPaymentDetails = collectionPaymentDetailsViewDao.getCollectedPaymentDetails(companyId, documentNo, documentFinancialYear, documentCode);
		
		if(listPaymentDetails.isEmpty()) {
				throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
			} 
			return AmxApiResponse.buildList(listPaymentDetails);	
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
