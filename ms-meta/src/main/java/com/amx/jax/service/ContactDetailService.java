package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IContactDetailsDAO;
import com.amx.jax.services.AbstractService;

@Service
public class ContactDetailService  extends AbstractService{

	
	@Autowired
	IContactDetailsDAO customerDao;
	

	
	public ApiResponse getContactDetail(BigDecimal customerId){
	List<ContactDetail> contactDetailList =customerDao.getContactDetails(customerId);
	ApiResponse response = getBlackApiResponse();
	if(contactDetailList.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(contactDetailList);
	response.setResponseStatus(ResponseStatus.OK);
	}
	
	response.getData().setType("contact");
	return response;
	
}
	
	
	
	public ApiResponse getContactDetailByCotactId(BigDecimal customerId ,BigDecimal contactTypeId ){
	List<ContactDetail> contactDetailList =customerDao.getContactDetailByCotactId(customerId, contactTypeId);
	ApiResponse response = getBlackApiResponse();
	if(contactDetailList.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(contactDetailList);
	response.setResponseStatus(ResponseStatus.OK);
	}
	
	response.getData().setType("contact");
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
