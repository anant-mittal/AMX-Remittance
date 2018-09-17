package com.amx.jax.offsite.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.ViewCityDto;
import com.amx.amxlib.meta.model.ViewDistrictDto;
import com.amx.amxlib.meta.model.ViewStateDto;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.PrefixModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.service.CountryService;
import com.amx.jax.service.MetaService;
import com.amx.jax.service.PrefixService;
import com.amx.jax.service.ViewDistrictService;
import com.amx.jax.service.ViewStateService;

import io.swagger.annotations.Api;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "Meta APIs")
public class MetaController {
	
	private static final Logger logger = LoggerService.getLogger(MetaController.class);
	
	@Autowired
	CountryService countryService;
	
	@Autowired
	ViewStateService stateService;
	
	@Autowired
	ViewDistrictService districtService;
	
	@Autowired
	MetaService metaService;
	
	@Autowired
	PrefixService prefixService;
	
	/**
	 * Gets the list of countries by language Id.
	 *
	 * @return the list of countries
	 */
	@RequestMapping(value = "/country/{languageId}", method = RequestMethod.GET)
	public AmxApiResponse<CountryMasterView, Object> getCountryByLanguageIdResponse(@PathVariable("languageId") BigDecimal languageId) {
		
		logger.info("Country list called by language : " + languageId);
		
		return countryService.getCountryListByLanguageIdResponse(languageId);
	}
	
	/**
	 * Gets the list of states by language Id and country Id.
	 *
	 * @return the list of states
	 */
	@RequestMapping(value = "/statelist/{languageId}/{countryId}/", method = RequestMethod.GET)
	public AmxApiResponse<ViewStateDto, Object> getStateNameListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("countryId") BigDecimal countryId){
		
		logger.info("State list called by language : " + languageId + " , country id : " +countryId);
		
		return stateService.getStateAll(countryId, languageId);
	}
	
	/**
	 * Gets the list of districts by language Id and state Id.
	 *
	 * @return the list of districts
	 */
	@RequestMapping(value = "/districtlist/{languageId}/{stateId}/", method = RequestMethod.GET)
	public AmxApiResponse<ViewDistrictDto, Object> getDistrictNameResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("stateId") BigDecimal stateId){
		
		logger.info("District list called by language : " + languageId + " , state id : " +stateId);
		
		return districtService.getAllDistrict(stateId, languageId);
	}
	
	/**
	 * Gets the list of cities by language Id and district Id.
	 *
	 * @return the list of cities
	 */
	@RequestMapping(value = "/citylist/{languageId}/{districtId}", method = RequestMethod.GET)
	public AmxApiResponse<ViewCityDto, Object> getCityListResponse(@PathVariable("languageId") BigDecimal languageId,@PathVariable("districtId") BigDecimal districtId){
		
		logger.info("city list called by language : " + languageId + " , district id : " +districtId);
		
		return metaService.getDistrictCity(districtId, languageId);
	}
	
	/**
	 * Gets the list of prefix.
	 *
	 * @return the list of prefix
	 */
	@RequestMapping(value = "/prefix/", method = RequestMethod.GET)
	public AmxApiResponse<PrefixModel, Object> getPrefixList() {
		
		return prefixService.getPrefixListResponse();
	}
	
}
