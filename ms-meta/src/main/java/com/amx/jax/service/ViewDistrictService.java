package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.services.AbstractService;

@Service
public class ViewDistrictService extends AbstractService{
	
	@Autowired
	IViewDistrictDAO viewDistrictDao;
	
/*	public List<ViewDistrict> getDistrict(BigDecimal stateId, BigDecimal districtId, BigDecimal languageId){
		return viewDistrictDao.getDistrict(stateId, districtId, languageId);
	}
	*/
	
	
	public ApiResponse getDistrict(BigDecimal stateId, BigDecimal districtId, BigDecimal languageId){
		List<ViewDistrict> viewDistrict =viewDistrictDao.getDistrict(stateId, districtId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewDistrict.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(viewDistrict);
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
