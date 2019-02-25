package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.meta.model.CountryMasterDTO;
import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.DiscountMgmtClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Discount Management APIs")
@ApiStatusService(IDiscManagementService.class)
public class BDiscountMgmtController {

	@Autowired
	DiscountMgmtClient discountMgmtClient;
	
	@Autowired
	MetaClient metaClient;
	
	
	@RequestMapping(value = "/api/discount/country/list", method = { RequestMethod.GET })
	public AmxApiResponse<CountryMasterDTO, Object> getAllCountry() {
	
		return metaClient.getAllCountry();
	}
	
	@RequestMapping(value = "/api/discount/currency/list", method = { RequestMethod.GET })
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(@RequestParam(value = "countryId", required = true)BigDecimal countryId) {
		
		return metaClient.getCurrencyByCountryId(countryId);
	}

	@RequestMapping(value = "/api/discount/countrybranch/list", method = { RequestMethod.GET })
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranchList() {

		return discountMgmtClient.getCountryBranchList();
	}
	
	@RequestMapping(value = "/api/discount/RbanksAndServices/list", method = { RequestMethod.POST })
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbanksAndServices(
			@RequestParam(value = "countryId", required = true) BigDecimal countryId,
			@RequestParam(value = "currencyId", required = true) BigDecimal currencyId) {
		return discountMgmtClient.getRbanksAndServices(countryId, currencyId);
	}

	@RequestMapping(value = "/api/discount/rates/list", method = { RequestMethod.GET })
	public AmxApiResponse<PricingResponseDTO, Object> fetchDiscountedRates(
			@RequestBody PricingRequestDTO pricingRequestDTO) {

		return discountMgmtClient.fetchDiscountedRates(pricingRequestDTO);
	}

	@RequestMapping(value = "/api/discount/getDiscountDetails", method = { RequestMethod.POST })
	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemetDetails(
			@RequestBody DiscountMgmtReqDTO discountMgmtReqDTO) {

		return discountMgmtClient.getDiscountManagemetDetails(discountMgmtReqDTO);
	}

	

}
