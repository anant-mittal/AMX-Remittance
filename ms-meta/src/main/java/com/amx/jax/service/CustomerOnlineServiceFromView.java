package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IViewOnlineCustomerCheck;
import com.amx.jax.services.AbstractService;

@Service
public class CustomerOnlineServiceFromView extends AbstractService{
	
	@Autowired
	IViewOnlineCustomerCheck viewOnlineCustomerCheck;
	
	

	
	public ApiResponse  civilIdCheckForOnlineUser(BigDecimal companyId,BigDecimal countryId,String civilId){
	List<ViewOnlineCustomerCheck> viewOnlinecustomerList =viewOnlineCustomerCheck.civilIdCheckForOnlineUser(companyId, countryId, civilId);
	ApiResponse response = getBlackApiResponse();
	if(viewOnlinecustomerList.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(viewOnlinecustomerList);
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
