package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CusmasModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICusmasDao;
import com.amx.jax.services.AbstractService;

@Service
public class CusmasService extends AbstractService{
	
	
	@Autowired
	ICusmasDao cusmasDao;
	
	
	
	
	public ApiResponse  getOldCusMasDetails(BigDecimal customerRefernce){
		List<CusmasModel> cusmasDetails =cusmasDao.getEmosCustomerDetails(customerRefernce);
		ApiResponse response = getBlackApiResponse();
		if(cusmasDetails.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(cusmasDetails);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("cusmas");
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
