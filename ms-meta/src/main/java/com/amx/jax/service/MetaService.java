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
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.OnlineConfiguration;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.dbmodel.meta.ServiceGroupMasterDesc;
import com.amx.jax.dbmodel.meta.ServiceMaster;
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
import com.amx.jax.repository.ServiceGroupMasterRepository;
import com.amx.jax.repository.ServiceMasterRepository;
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
	ServiceGroupMasterRepository serviceGroupMasterRepository;
	@Autowired
	ServiceMasterRepository serviceMasterRepository;
	
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
		response.getData().setType("service-group-model");
		List<ServiceGroupMasterDescDto> outputDto = getServiceGroupDto();
		response.getData().getValues().addAll(outputDto);
		return response;
	}

	private List<ServiceGroupMasterDescDto> getServiceGroupDto() {
		List<ServiceGroupMasterDesc> output = serviceGroupMasterDescRepository
				.findActiveByLanguageId(metaData.getLanguageId());
		final List<ServiceGroupMasterDescDto> outputDto = new ArrayList<>();
		output.forEach(i -> {
			// disable cash
			//if (!i.getServiceGroupMasterId().getServiceGroupId().equals(BigDecimal.ONE)) {
				ServiceGroupMasterDescDto dto = new ServiceGroupMasterDescDto();
				dto.setServiceGroupMasterId(i.getServiceGroupMasterId().getServiceGroupId());
				dto.setServiceGroupDesc(i.getServiceGroupDesc());
				dto.setServiceGroupShortDesc(i.getServiceGroupShortDesc());
				outputDto.add(dto);
			//}
		});
		return outputDto;
	}

	public boolean isCashSeriveGroup(BigDecimal serviceGroupMaseterId) {
		List<ServiceGroupMaster> output = serviceGroupMasterRepository
				.findByServiceGroupIdAndIsActive(serviceGroupMaseterId, ConstantDocument.Yes);
		if (output != null && !output.isEmpty()) {
			if (ConstantDocument.SERVICE_GROUP_CODE_CASH.equals(output.get(0).getServiceGroupCode())) {
				return true;
			}
		}
		return false;
	}

	public ServiceGroupMaster getServiceGroupMasterByCode(String serviceGroupCode) {
		return serviceGroupMasterRepository.findByServiceGroupCodeAndIsActive(serviceGroupCode, ConstantDocument.Yes)
				.get(0);
	}
	
	public List<ServiceMaster> getServiceMaster(String serviceGroupCode) {
		ServiceGroupMaster serviceGroupMaster = getServiceGroupMasterByCode(serviceGroupCode);
		return serviceMasterRepository.findByServiceGroupIdAndIsActive(serviceGroupMaster, ConstantDocument.Yes);
	}

	public Map<BigDecimal, ServiceGroupMasterDescDto> getServiceGroupDtoMap() {
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

		Map<BigDecimal, ServiceGroupMasterDescDto> outputDtoMap = outputDto.stream()
				.collect(Collectors.toMap(ServiceGroupMasterDescDto::getServiceGroupMasterId, x -> x));
		return outputDtoMap;
	}

	public ViewDistrict getDistrictMasterById(BigDecimal id) {
		return districtDao.findOne(id);
	}
	
	public ViewState getStateMasterById(BigDecimal id) {
		return stateDao.findOne(id);
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
