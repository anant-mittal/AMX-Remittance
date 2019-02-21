package com.amx.jax.pricer.api;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.pricer.ProbotDataService;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.DiscountMgmtRespDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.pricer.service.DiscountService;

@RestController
public class ProbotDataServiceApiController implements ProbotDataService{

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(ProbotDataServiceApiController.class);
	
	@Resource
	DiscountService discountService;

	@Override
	@RequestMapping(value = ApiEndPoints.GET_DISCOUNT_MGMT, method = RequestMethod.POST)
	public AmxApiResponse<DiscountMgmtRespDTO, Object> getDiscountManagemet(
			@RequestBody @Valid DiscountMgmtReqDTO discountMgmtReqDTO) {
		LOGGER.info("In Get API of Discount Management");
		
		DiscountMgmtRespDTO discountMgmtRespDTO = discountService.getDiscountManagementData(discountMgmtReqDTO);
		
		return AmxApiResponse.build(discountMgmtRespDTO);
	}

	@Override
	@RequestMapping(value = ApiEndPoints.GET_ROUTBANK_AND_SEVICE, method = RequestMethod.POST)
	public AmxApiResponse<RoutBanksAndServiceRespDTO, Object> getRbankAndService(
			@RequestParam(required = true) BigDecimal countryId, @RequestParam(required = true) BigDecimal currencyId) {
		LOGGER.info("In Get API of Routing Bank and Services");
		
		List<RoutBanksAndServiceRespDTO> routBanksAndServiceRespDTO = discountService.getRoutBankAndService(countryId, currencyId);
		
		return AmxApiResponse.buildList(routBanksAndServiceRespDTO);
	}
	
	
}
