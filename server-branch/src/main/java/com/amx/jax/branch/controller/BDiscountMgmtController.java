package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import org.slf4j.Logger;
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
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.branch.beans.BranchSession;
import com.amx.jax.client.DiscountMgmtClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.pricer.dto.DiscountDetailsReqRespDTO;
import com.amx.jax.pricer.dto.DiscountMgmtReqDTO;
import com.amx.jax.pricer.dto.GroupDetails;
import com.amx.jax.pricer.dto.OnlineMarginMarkupInfo;
import com.amx.jax.pricer.dto.OnlineMarginMarkupReq;
import com.amx.jax.pricer.dto.PricingAndCostResponseDTO;
import com.amx.jax.pricer.dto.PricingRequestDTO;
import com.amx.jax.pricer.dto.PricingResponseDTO;
import com.amx.jax.pricer.dto.RoutBanksAndServiceRespDTO;
import com.amx.jax.sso.SSOUser;
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

	@Autowired
	BranchSession branchSession;
	
	@Autowired
	private SSOUser ssoUser;
	private static final Logger LOGGER = LoggerService.getLogger(BDiscountMgmtController.class);


	@RequestMapping(value = "/api/discount/country/list", method = { RequestMethod.GET })
	public AmxApiResponse<CountryMasterDTO, Object> getAllCountry() {

		return metaClient.getAllCountry();
	}

	@RequestMapping(value = "/api/discount/currency/list", method = { RequestMethod.GET })
	public AmxApiResponse<CurrencyMasterDTO, Object> getCurrencyByCountryId(
			@RequestParam(value = "countryId", required = true) BigDecimal countryId) {

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

	@RequestMapping(value = "/api/discount/rates/list", method = { RequestMethod.POST })
	public AmxApiResponse<PricingAndCostResponseDTO, Object> fetchDiscountedRates(
			@RequestBody PricingRequestDTO pricingRequestDTO) {
		return discountMgmtClient.fetchDiscountedRates(pricingRequestDTO);
	}

	@RequestMapping(value = "/api/discount/getDiscountDetails", method = { RequestMethod.POST })
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> getDiscountManagemetDetails(
			@RequestBody DiscountMgmtReqDTO discountMgmtReqDTO) {
		return discountMgmtClient.getDiscountManagemetDetails(discountMgmtReqDTO);
	}

	@RequestMapping(value = "/api/discount/saveDiscountDetails", method = { RequestMethod.POST })
	public AmxApiResponse<DiscountDetailsReqRespDTO, Object> saveDiscountDetails(
			@RequestBody DiscountDetailsReqRespDTO discountMgmtReqDTO) {
		return discountMgmtClient.saveDiscountDetails(discountMgmtReqDTO);

	}
	
	@RequestMapping(value = "/api/discount/exchange_rate_currency/list", method = { RequestMethod.GET })
	public AmxApiResponse<CurrencyMasterDTO, Object> getAllExchangeRateCurrencyList() {

		return metaClient.getAllExchangeRateCurrencyList();
	}



	@RequestMapping(value = "/api/discount/customer/rates", method = { RequestMethod.POST })
	public AmxApiResponse<PricingResponseDTO, Object> fetchCustomerRates(
			@RequestBody PricingRequestDTO pricingRequestDTO, 
			@RequestParam(required = false) String identity,
			@RequestParam(required = false) BigDecimal identityType) {
		branchSession.getCustomerContext().refresh();
		pricingRequestDTO.setCustomerId(branchSession.getCustomerId());
		return discountMgmtClient.fetchCustomerRates(pricingRequestDTO);
	}

	
	@RequestMapping(value = "/api/discount/groups/list", method = { RequestMethod.GET })
	public AmxApiResponse<GroupDetails, Object> getCurrencyGroupingData() {
			return discountMgmtClient.getCurrencyGroupingData();
	}
	
	@RequestMapping(value = "/api/discount/currencyGroup/list", method = { RequestMethod.POST })
	public AmxApiResponse<com.amx.jax.pricer.dto.CurrencyMasterDTO, Object> getCurrencyByGroupId(@RequestParam(value = "groupId", required = true)BigDecimal groupId) {
			return discountMgmtClient.getCurrencyByGroupId(groupId);
	}
	
	@RequestMapping(value = "/api/online-markup/list", method = { RequestMethod.POST })
	public AmxApiResponse<OnlineMarginMarkupInfo, Object> getOnlineMarginMarkupData(@RequestBody OnlineMarginMarkupReq request) {
		return discountMgmtClient.getOnlineMarginMarkupData(request);
	}
	
	@RequestMapping(value = "/api/save-markup/details", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> saveOnlineMarginMarkupData(@RequestBody OnlineMarginMarkupInfo onlineMarginMarkupInfo) {
		 LOGGER.info("ssoUser.getUserDetails().getEmployeeName()",ssoUser.getUserDetails().getEmployeeName());
		 String username=ssoUser.getUserDetails().getEmployeeName()!= null ? ssoUser.getUserDetails().getEmployeeName(): "";
		onlineMarginMarkupInfo.setEmpName(username);
		return discountMgmtClient.saveOnlineMarginMarkupData(onlineMarginMarkupInfo);
	}
	
}
