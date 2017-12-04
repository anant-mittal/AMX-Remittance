/*package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IGeneralDAO;
import com.amx.jax.services.AbstractService;

@Service
public class MetaService<T> extends AbstractService{
	@Autowired
	IGeneralDAO<ViewDistrict> viewDistDao;
	
	@Autowired
	IGeneralDAO<ViewState> viewStateDao;
	
	public ApiResponse<ViewDistrict> getDistrict(BigDecimal stateId, BigDecimal districtId, BigDecimal languageId){
		List<ViewDistrict> viewDistrict =viewDistDao.getDistrict(stateId, districtId, languageId);
		ApiResponse<ViewDistrict> response = getBlackApiResponse();
		if(viewDistrict.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(viewDistrict);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
		}
	}

	
	
	
	
	
	//ViewDistrictDto

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
*/