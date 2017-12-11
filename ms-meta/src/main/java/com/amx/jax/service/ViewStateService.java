package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class ViewStateService extends AbstractService{
	
	@Autowired
	IViewStateDao viewStateDao;
	public ApiResponse getState(BigDecimal countryId, BigDecimal stateId, BigDecimal languageId){
		List<ViewState> viewState =viewStateDao.getState(countryId, stateId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewState.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(viewState));
		response.setResponseStatus(ResponseStatus.OK);
		
		}
		response.getData().setType("state");
		return response;
	}
	
	
	
	
	public ApiResponse getStateAll(BigDecimal countryId,BigDecimal languageId){
		List<ViewState> viewState =viewStateDao.getStateForCountry(countryId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewState.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}else {
		response.getData().getValues().addAll(convert(viewState));
		response.setResponseStatus(ResponseStatus.OK);
		
		}
		response.getData().setType("state");
		return response;
	}
	
	
	

	public List<ViewStateDto> convert(List<ViewState> viewState){
		List<ViewStateDto> list = new ArrayList<>();
		for(ViewState state: viewState) {
			ViewStateDto model = new ViewStateDto();
			model.setStateId(state.getStateId());
			model.setLanguageId(state.getLanguageId());
			model.setCountryId(state.getCountryId());
			model.setStateName(state.getStateName());
			model.setStateCode(state.getStateCode());
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
