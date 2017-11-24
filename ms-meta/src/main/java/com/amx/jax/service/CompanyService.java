package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewCompanyDetails;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.services.AbstractService;

@Service
public class CompanyService  extends AbstractService{
	
	@Autowired
	ICompanyDAO companyDao;
	
	public ApiResponse  getCompanyDetails(BigDecimal languageId){
	List<ViewCompanyDetails> companyDetails =companyDao.getCompanyDetails(languageId);
	ApiResponse response = getBlackApiResponse();
	if(companyDetails.isEmpty()) {
		throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
	}else {
	response.getData().getValues().addAll(companyDetails);
	response.setResponseStatus(ResponseStatus.OK);
	}
	response.getData().setType("company");
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
