package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.ViewOnlineEmailMobileCheck;
import com.amx.jax.repository.ICustomerEmailMobileCheck;
import com.amx.jax.services.AbstractService;

/**
 * 
 * @author :Rabil
 *
 */
@Service
public class EmailMobileCheckService extends AbstractService{

	@Autowired
	ICustomerEmailMobileCheck customerEmailMobileCheck;
	
	
	public AmxApiResponse<ViewOnlineEmailMobileCheck, Object> checkEmail(BigDecimal languageId,BigDecimal countryId,String email){
		List<ViewOnlineEmailMobileCheck> emailCheck = customerEmailMobileCheck.getEmailCheck(languageId, countryId, email);
	
		/*ApiResponse response = getBlackApiResponse();
		if(emailCheck.isEmpty()) {
			response.setResponseStatus(ResponseStatus.OK);
		}else if(emailCheck.size()==1){
		response.getData().getValues().addAll(emailCheck);
		response.setResponseStatus(ResponseStatus.OK);
		}else {
			response.getData().getValues().addAll(emailCheck);
			response.setResponseStatus(ResponseStatus.DUPLICATE);
		}
		response.getData().setType("email");*/		
		return AmxApiResponse.buildList(emailCheck);
	}
	
	
	public AmxApiResponse<ViewOnlineEmailMobileCheck, Object> checkMobile(BigDecimal languageId,BigDecimal countryId,String mobile){
		List<ViewOnlineEmailMobileCheck> mobileCheck = customerEmailMobileCheck.getMobileCheck(languageId, countryId, mobile);
	
		/*ApiResponse response = getBlackApiResponse();
		if(mobileCheck.isEmpty()) {
			response.setResponseStatus(ResponseStatus.OK);
		}else if(mobileCheck.size()==1){
		response.getData().getValues().addAll(mobileCheck);
		response.setResponseStatus(ResponseStatus.OK);
		}else {
			response.getData().getValues().addAll(mobileCheck);
			response.setResponseStatus(ResponseStatus.DUPLICATE);
		}
		response.getData().setType("mobile");*/
		return AmxApiResponse.buildList(mobileCheck);
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
