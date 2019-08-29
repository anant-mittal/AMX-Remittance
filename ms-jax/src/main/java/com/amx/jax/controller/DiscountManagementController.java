package com.amx.jax.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.CountryBranchDTO;
import com.amx.jax.IDiscManagementService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.services.DiscountManagementService;

@RestController
public class DiscountManagementController implements IDiscManagementService {
	
	@Autowired
	DiscountManagementService discountManagementService;
	
	@Autowired
	MetaData metaData;
	
	private static final Logger LOGGER = LoggerService.getLogger(DiscountManagementController.class);

	
	@RequestMapping(value = ApiEndPoints.GET_COUNTRY_BRANCH, method = RequestMethod.GET)
	public AmxApiResponse<CountryBranchDTO, Object> getCountryBranch() {
		BigDecimal applicationCountryId = metaData.getCountryId();
		return discountManagementService.getCountryBranch(applicationCountryId);
	}
	
	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUTN_RATE, method = RequestMethod.POST)
	public AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(@RequestBody @Valid PricingRequestDTO pricingRequestDTO) {
		return discountManagementService.fetchDiscountedRates(pricingRequestDTO);
	}
	
	@Override
	@RequestMapping(value = ApiEndPoints.GET_MARKUP_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(
			@RequestBody @Valid OnlineMarginMarkupReq onlineMarginMarkupReq ) {
		LOGGER.debug("Received Request for markup " +onlineMarginMarkupReq.toString());
		return discountManagementService.getOnlineMarginMarkupData(onlineMarginMarkupReq);
		}
	
	@Override
	@RequestMapping(value = ApiEndPoints.SAVE_MARKUP_DETAILS, method = RequestMethod.POST)
	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(
			@RequestBody @Valid OnlineMarginMarkupInfo onlineMarginMarkupInfo ) {
		return discountManagementService.saveOnlineMarginMarkupData(onlineMarginMarkupInfo);

		}
}
