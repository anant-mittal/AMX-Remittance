/**
 * Meta Servcie
 * Author : MRU
 * Purpose : To get the customer Info
 * Date    : 03/01/2018
 */

package com.amx.jax.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.OnlineConfiguration;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.meta.ServiceGroupMasterDesc;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.OnlineConfigurationRepository;
import com.amx.jax.repository.ServiceGroupMasterDescRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.JaxUtil;

@Service
@SuppressWarnings("rawtypes")
public class MetaService extends AbstractService {

	private Logger logger = Logger.getLogger(MetaService.class);

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	OnlineConfigurationRepository onlineConfigurationRepository;
	
	@Autowired
	ServiceGroupMasterDescRepository serviceGroupMasterDescRepository;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	JaxUtil jaxUtil;

	public ApiResponse getDistrictCity(BigDecimal districtId, BigDecimal languageId) {
		List<ViewCity> cityList = cityDao.getCityByDistrictId(districtId, languageId);
		ApiResponse response = getBlackApiResponse();
		if (cityList.isEmpty()) {
			throw new GlobalException("city not avaliable");
		} else {
			response.getData().getValues().addAll(convertCityDto(cityList));
			response.setResponseStatus(ResponseStatus.OK);
		}
		response.getData().setType("city");
		return response;
	}

	public ApiResponse getCityDescription(BigDecimal districtId, BigDecimal languageId, BigDecimal cityId) {
		List<ViewCity> cityList = cityDao.getCityDescription(districtId, cityId, languageId);
		ApiResponse response = getBlackApiResponse();
		if (cityList.isEmpty()) {
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

	public ApiResponse getOnlineConfig(String applInd) {
		ApiResponse response = getBlackApiResponse();
		List<OnlineConfiguration> output = onlineConfigurationRepository.findByappInd(applInd);
		OnlineConfigurationDto dto = new OnlineConfigurationDto();
		try {
			BeanUtils.copyProperties(dto, output.get(0));
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("unable to convert OnlineConfigurationDto", e);
		}
		response.getData().setType("online-config");
		response.getData().getValues().add(dto);
		return response;
	}
	
	public ApiResponse getServiceGroups() {
		ApiResponse response = getBlackApiResponse();
		List<ServiceGroupMasterDesc> output = serviceGroupMasterDescRepository
				.findActiveByLanguageId(metaData.getLanguageId());
		final List<ServiceGroupMasterDescDto> outputDto = new ArrayList<>();
		output.forEach(i -> {
			ServiceGroupMasterDescDto dto = new ServiceGroupMasterDescDto();
			dto.setServiceGroupMasterId(i.getServiceGroupMasterId().getServiceGroupId());
			dto.setServiceGroupDesc(i.getServiceGroupDesc());
			dto.setServiceGroupShortDesc(i.getServiceGroupShortDesc());
			outputDto.add(dto);
		});
		response.getData().setType("service-group-model");
		response.getData().getValues().addAll(outputDto);
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
