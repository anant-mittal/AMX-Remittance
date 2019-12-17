package com.amx.jax.meta;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.meta.model.PrefixDTO;
import com.amx.amxlib.meta.model.ServiceGroupMasterDescDto;
import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.MetaClient;
import com.amx.jax.client.OffsiteCustRegClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.response.ComponentDataDto;

import io.swagger.annotations.Api;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "Branch Meta APIs")
public class MetaController {

	private static final Logger LOGGER = LoggerService.getLogger(MetaController.class);

	@Autowired
	MetaClient metaClient;

	/**
	 *
	 * @return the list of countries
	 */
	@RequestMapping(value = { "/pub/meta/country/list" }, method = { RequestMethod.GET })
	public AmxApiResponse<CountryMasterDTO, Object> getListOfCountriesPub() {
		return metaClient.getAllCountry();
	}

	@RequestMapping(value = { "/pub/meta/state/list" }, method = { RequestMethod.GET })
	public AmxApiResponse<ViewStateDto, Object> getListOfStatesForCountry(@RequestParam BigDecimal countryId) {
		return metaClient.getStateList(countryId);
	}

	/**
	 * Gets the list of districts for state.
	 *
	 * @param stateId the state id
	 * @return the list of districts for state
	 */
	@RequestMapping(value = { "/pub/meta/district/list" }, method = { RequestMethod.GET })
	public AmxApiResponse<ViewDistrictDto, Object> getListOfDistrictsForState(@RequestParam BigDecimal stateId) {
		return metaClient.getDistrictList(stateId);
	}

	/**
	 * Gets the list of cities for district.
	 *
	 * @param districtId the district id
	 * @return the list of cities for district
	 */
	@RequestMapping(value = { "/pub/meta/city/list" }, method = { RequestMethod.GET })
	public AmxApiResponse<ViewCityDto, Object> getListOfCitiesForDistrict(@RequestParam BigDecimal districtId) {
		return metaClient.getCitytList(districtId);
	}

	/**
	 * Gets the list of prefix.
	 *
	 * @return the list of prefix
	 */
	@RequestMapping(value = "/pub/meta/name_prefix/list", method = RequestMethod.GET)
	public AmxApiResponse<PrefixDTO, Object> getPrefixList() {
		return metaClient.getAllPrefix();
	}

	@Autowired
	private OffsiteCustRegClient offsiteCustRegClient;

	@RequestMapping(value = "/pub/meta/id_type/list", method = RequestMethod.GET)
	public AmxApiResponse<ComponentDataDto, Object> getIdTypes() {
		return offsiteCustRegClient.getIdTypes();
	}
	
	@RequestMapping(value = "/pub/meta/service/list", method = RequestMethod.GET)
	public AmxApiResponse<ServiceGroupMasterDescDto, Object> getServiceGroupList() {
		return metaClient.getServiceGroupList();
	}


}
