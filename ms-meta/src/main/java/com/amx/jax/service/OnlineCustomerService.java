package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.repository.IOnlineCustomerRepository;
import com.amx.jax.services.AbstractService;

@Service
public class OnlineCustomerService extends AbstractService{
	
	@Autowired
	IOnlineCustomerRepository onlineCustomerRepository;
	
	
	public ApiResponse  getOnlineCustomerList(BigDecimal countryId,String userName){
	List<CustomerOnlineRegistration> onlineCustomerList =onlineCustomerRepository.getOnlineCustomerList(countryId, userName);
	ApiResponse response = getBlackApiResponse();
	if(onlineCustomerList.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(onlineCustomerList);
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
