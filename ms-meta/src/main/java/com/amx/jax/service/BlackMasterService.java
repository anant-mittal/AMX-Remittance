package com.amx.jax.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.repository.IBlackMasterRepository;
import com.amx.jax.services.AbstractService;

@Service
public class BlackMasterService extends AbstractService {
	
	@Autowired
	IBlackMasterRepository  blackMasterRepository;
	

	
	public ApiResponse  getBlackList(String name){
	List<BlackListModel> blackList =blackMasterRepository.getBlackByName(name);
	ApiResponse response = getBlackApiResponse();
	if(blackList.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(blackList);
	response.setResponseStatus(ResponseStatus.OK);
	}
	response.getData().setType("blist");
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
