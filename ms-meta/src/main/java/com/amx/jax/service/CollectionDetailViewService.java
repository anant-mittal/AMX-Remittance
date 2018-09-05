package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CollectionDetailViewModel;
import com.amx.jax.repository.ICollectionDetailViewDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class CollectionDetailViewService extends AbstractService {

	@Autowired
	ICollectionDetailViewDao collectionDetailViewDao;

	public AmxApiResponse<CollectionDetailViewModel, Object> getCollectionDetailFromView(BigDecimal companyId, BigDecimal documentNo,
			BigDecimal documentFinancialYear, BigDecimal documentCode) {

		List<CollectionDetailViewModel> collectionDetailView = collectionDetailViewDao
				.getCollectionDetailView(companyId, documentNo, documentFinancialYear, documentCode);

		if (collectionDetailView.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		} 
		return AmxApiResponse.buildList(collectionDetailView);	
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
