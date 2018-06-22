package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.services.AbstractService;

@Service
public class ViewDistrictService extends AbstractService{
	
	@Autowired
	IViewDistrictDAO viewDistrictDao;
	
	public ApiResponse getDistrict(BigDecimal stateId, BigDecimal districtId, BigDecimal languageId){
		List<ViewDistrict> viewDistrict =viewDistrictDao.getDistrict(stateId, districtId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewDistrict.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(viewDistrict));
		response.setResponseStatus(ResponseStatus.OK);
				}
		
		
		response.getData().setType("district");
		return response;
	}
	
	
	
	public ApiResponse getAllDistrict(BigDecimal stateId,BigDecimal languageId){
		List<ViewDistrict> viewDistrict =viewDistrictDao.getAllDistrict(stateId,languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewDistrict.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(viewDistrict));
		response.setResponseStatus(ResponseStatus.OK);
				}
		
		
		response.getData().setType("district");
		return response;
	}


	
	
	public List<ViewDistrictDto> convert(List<ViewDistrict> viewDistrict){
		
		List<ViewDistrictDto> list = new ArrayList<>();
		
		for(ViewDistrict district: viewDistrict) {
			ViewDistrictDto model = new ViewDistrictDto();
			model.setStateId(district.getStateId());
			model.setLanguageId(district.getLanguageId());
			model.setDistrictId(district.getDistrictId());
			model.setDistrictDesc(district.getDistrictDesc());
			list.add(model);
		}
		
		return list;
		
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
