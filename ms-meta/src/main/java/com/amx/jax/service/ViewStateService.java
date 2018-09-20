package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class ViewStateService extends AbstractService{
	
	@Autowired
	IViewStateDao viewStateDao;
	public AmxApiResponse<ViewStateDto, Object> getState(BigDecimal countryId, BigDecimal stateId, BigDecimal languageId){
		List<ViewState> viewState =viewStateDao.getState(countryId, stateId, languageId);
		if(viewState.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}		
		return AmxApiResponse.buildList(convert(viewState));
	}
	
	
	
	
	public AmxApiResponse<ViewStateDto, Object> getStateAll(BigDecimal countryId,BigDecimal languageId){
		List<ViewState> viewState =viewStateDao.getStateForCountry(countryId, languageId);
		if(viewState.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.buildList(convert(viewState));
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


	public AmxApiResponse<List<ViewStateDto>, Object> getStateListOffsite(BigDecimal countryId,BigDecimal languageId){
		List<ViewState> viewState =viewStateDao.getStateForCountry(countryId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(viewState.isEmpty()) {
			throw new GlobalException(ResponseStatus.NOT_FOUND.toString());
		}
		return AmxApiResponse.build(convert(viewState));
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
