package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.AuthenticationLimitCheckView;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.AuthenticationLimitCheckDAO;
import com.amx.jax.services.AbstractService;

@Service
public class ParameterService extends AbstractService {
	
	
	@Autowired
	AuthenticationLimitCheckDAO authentication;
	
	public ApiResponse  getContactUsTime(){
		List<AuthenticationLimitCheckView> contactUsList =authentication.getContactUsTime();
		ApiResponse response = getBlackApiResponse();
		if(contactUsList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(contactUsList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("parameter");
		return response;
	}
	
	
	
	public ApiResponse  getContactPhoneNo(){
		List<AuthenticationLimitCheckView> phoneNoList =authentication.getContactUsPhoneNo();
		ApiResponse response = getBlackApiResponse();
		if(phoneNoList.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(phoneNoList);
		response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("parameter");
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
