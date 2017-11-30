package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IDmsDocumentDao;
import com.amx.jax.services.AbstractService;

@Service
public class DmsDocumentService extends AbstractService{
	
	@Autowired
	IDmsDocumentDao iDmsDocumentDao;
	
	
	public ApiResponse getDmsDocument(BigDecimal blobId,BigDecimal docFyr){
		List<DmsDocumentModel> dmsDocumentList = iDmsDocumentDao.getDmsDocumentList(blobId, docFyr);
		ApiResponse response = getBlackApiResponse();
		if(dmsDocumentList.isEmpty()) {
			throw new GlobalException("Image is not found");
		}else {
		response.getData().getValues().addAll(dmsDocumentList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		
		response.getData().setType("dms_image");
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
