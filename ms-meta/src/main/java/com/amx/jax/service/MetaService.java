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

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.ViewAreaDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewGovernateAreaDto;
import com.amx.amxlib.meta.model.ViewGovernateDto;
import com.amx.amxlib.model.OnlineConfigurationDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.config.JaxProperties;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.OnlineConfiguration;
import com.amx.jax.dbmodel.ViewAreaModel;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.dbmodel.VwGovernateAreaModel;
import com.amx.jax.dbmodel.VwGovernateModel;
import com.amx.jax.dbmodel.meta.ServiceGroupMaster;
import com.amx.jax.dbmodel.meta.ServiceGroupMasterDesc;
import com.amx.jax.dbmodel.meta.ServiceMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.IGovernateAreaDao;
import com.amx.jax.repository.IViewArea;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewGovernateDao;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.repository.OnlineConfigurationRepository;
import com.amx.jax.repository.ServiceGroupMasterDescRepository;
import com.amx.jax.repository.ServiceGroupMasterRepository;
import com.amx.jax.repository.ServiceMasterRepository;
import com.amx.jax.services.AbstractService;
import com.amx.jax.util.JaxUtil;

@Service
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
	IViewArea areaDao;

	@Autowired
	IViewGovernateDao govermentDao;

	@Autowired
	IGovernateAreaDao govermentAreaDao;

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
	@Autowired
	JaxProperties jaxProperties;

	public AmxApiResponse<ViewCityDto, Object> getDistrictCity(BigDecimal districtId, BigDecimal languageId) {
		List<ViewCity> cityList = cityDao.getCityByDistrictId(districtId, languageId);
		if (cityList.isEmpty()) {
			throw new GlobalException(JaxError.CITY_NOT_AVAILABLE, "city not avaliable");
		}
		return AmxApiResponse.buildList(convertCityDto(cityList));
	}

	public AmxApiResponse<ViewCityDto, Object> getCityDescription(BigDecimal districtId, BigDecimal languageId,
			BigDecimal cityId) {
		List<ViewCity> cityList = cityDao.getCityDescription(districtId, cityId, languageId);
		if (cityList.isEmpty()) {
			throw new GlobalException("city not avaliable");
		}
		return AmxApiResponse.buildList(convertCityDto(cityList));
	}

	private List<ViewCityDto> convertCityDto(List<ViewCity> cityList) {
		List<ViewCityDto> output = new ArrayList<>();
		cityList.forEach(cityModel -> output.add(convertCityModelToDto(cityModel)));
		return output;
	}

	public AmxApiResponse<ViewAreaDto, Object> getAreaList() {
		List<ViewAreaModel> viewAreaList = areaDao.getAreaList();
		if (viewAreaList.isEmpty()) {
			throw new GlobalException("area not avaliable");
		}
		return AmxApiResponse.buildList(convertAreaDto(viewAreaList));
	}

	/** For Governate list **/
	public AmxApiResponse<ViewGovernateDto, Object> getGovernateList(BigDecimal applicationCountryId) {
		List<VwGovernateModel> goveList = govermentDao.getGovermentList(applicationCountryId);
		if (goveList.isEmpty()) {
			throw new GlobalException("governate are not avaliable");
		}
		return AmxApiResponse.buildList(convertGovtDto(goveList));

	}

	/** For Governate area list **/
	public AmxApiResponse<ViewGovernateAreaDto, Object> getGovernateAreaList(BigDecimal governateId) {
		List<VwGovernateAreaModel> goveAreaList = govermentAreaDao.getGovermenAreaList(governateId);
		if (goveAreaList.isEmpty()) {
			throw new GlobalException("governate area are not avaliable");
		}
		return AmxApiResponse.buildList(convertGovtAreaDto(goveAreaList));

	}

	public List<ViewGovernateAreaDto> convertGovtAreaDto(List<VwGovernateAreaModel> goveAreaList) {
		List<ViewGovernateAreaDto> output = new ArrayList<>();
		for (VwGovernateAreaModel model : goveAreaList) {
			ViewGovernateAreaDto dto = new ViewGovernateAreaDto();
			dto.setAreaId(model.getGoverAreaId());
			dto.setArFullName(model.getArFullName());
			dto.setFullName(model.getFullName());
			dto.setGovendateId(model.getGovernateId());
			output.add(dto);
		}
		return output;
	}

	public List<ViewGovernateDto> convertGovtDto(List<VwGovernateModel> goveList) {
		List<ViewGovernateDto> output = new ArrayList<>();
		for (VwGovernateModel model : goveList) {
			ViewGovernateDto dto = new ViewGovernateDto();
			dto.setApplicationCountryId(model.getApplicationCountryId());
			dto.setArFullName(model.getArFullName());
			dto.setFullName(model.getFullName());
			dto.setGovernateId(model.getGovernateId());
			output.add(dto);
		}
		return output;
	}

	private List<ViewAreaDto> convertAreaDto(List<ViewAreaModel> viewAreaList) {
		List<ViewAreaDto> output = new ArrayList<>();
		for (ViewAreaModel viewAreaModel : viewAreaList) {
			ViewAreaDto dto = new ViewAreaDto();
			dto.setAreaCode(viewAreaModel.getAreaCode());
			dto.setAreaDesc(viewAreaModel.getAreaDesc());
			dto.setShortDesc(viewAreaModel.getShortDesc());
			output.add(dto);
		}
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

	public AmxApiResponse<OnlineConfigurationDto, Object> getOnlineConfig(String applInd) {
		List<OnlineConfiguration> output = onlineConfigurationRepository.findByappInd(applInd);
		OnlineConfigurationDto dto = new OnlineConfigurationDto();
		try {
			BeanUtils.copyProperties(dto, output.get(0));
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("unable to convert OnlineConfigurationDto", e);
		}
		return AmxApiResponse.build(dto);
	}

	public AmxApiResponse<ServiceGroupMasterDescDto, Object> getServiceGroups() {
		List<ServiceGroupMasterDescDto> outputDto = getServiceGroupDto();
				
		return AmxApiResponse.buildList(outputDto);
	}

	private List<ServiceGroupMasterDescDto> getServiceGroupDto() {
		List<ServiceGroupMasterDesc> output = serviceGroupMasterDescRepository
				.findActiveByLanguageId(metaData.getLanguageId());
		
		
		final List<ServiceGroupMasterDescDto> outputDto = new ArrayList<>();
	
		output.forEach(i -> {
			boolean isCash = i.getServiceGroupMasterId().getServiceGroupId().equals(BigDecimal.ONE);
			if (isCash && jaxProperties.getCashDisable()) {
				return;
			}
			ServiceGroupMasterDescDto dto = new ServiceGroupMasterDescDto();	
			
			dto.setServiceGroupMasterId(i.getServiceGroupMasterId().getServiceGroupId());
			dto.setServiceGroupDesc(i.getServiceGroupDesc());
			dto.setServiceGroupShortDesc(i.getServiceGroupShortDesc());
			
			outputDto.add(dto);
		
		}
		        );
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
