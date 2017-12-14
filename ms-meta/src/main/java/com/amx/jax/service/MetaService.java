package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.services.AbstractService;

@Service
@SuppressWarnings("rawtypes")
public class MetaService  extends AbstractService{
	
	private Logger logger = Logger.getLogger(MetaService.class);
	
	@Autowired
	IViewCityDao cityDao;
	
	
	public ApiResponse getDistrictCity(BigDecimal districtId,BigDecimal languageId) {
		List<ViewCity> cityList = cityDao.getCityByDistrictId(districtId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(cityList.isEmpty()) {
			throw new GlobalException("city not avaliable");
		} else {
			response.getData().getValues().addAll(convertCityDto(cityList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("city");
		return response;
	}
	
	public ApiResponse getCityDescription(BigDecimal districtId,BigDecimal languageId,BigDecimal cityId) {
		List<ViewCity> cityList = cityDao.getCityDescription(districtId, cityId, languageId);
		ApiResponse response = getBlackApiResponse();
		if(cityList.isEmpty()) {
			throw new GlobalException("city not avaliable");
		} else {
			response.getData().getValues().addAll(convertCityDto(cityList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("city");
		return response;
	}
	
	
	
	

	private List<ViewCityDto> convertCityDto(List<ViewCity> cityList) {
		List<ViewCityDto> output = new ArrayList<>();
		cityList.forEach(cityModel -> output.add(convertCityModelToDto(cityModel)));
		return output;
	}
	
	private ViewCityDto convertCityModelToDto(ViewCity cityModel) {
		ViewCityDto dto = new ViewCityDto();
		try {
			BeanUtils.copyProperties(dto, cityModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("unable to convert city", e);
		}
		return dto;
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
