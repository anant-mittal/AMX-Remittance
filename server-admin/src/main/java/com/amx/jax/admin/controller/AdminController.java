/**  AlMulla Exchange
  *  
  */
package com.amx.jax.admin.controller;

import static com.amx.jax.admin.constant.AdminConstant.ADMIN_API_ENDPOINT;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.admin.service.AdminService;
import com.amx.jax.admin.service.JaxService;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.amxlib.model.JaxMetaInfo;
import com.amx.jax.client.ExchangeRateClient;
import com.amx.jax.client.MetaClient;
import com.amx.jax.dict.Tenant;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@RestController
@RequestMapping(ADMIN_API_ENDPOINT)
@Api(value = "Admin APIs")
@SuppressWarnings("rawtypes")
public class AdminController {

	private Logger logger = Logger.getLogger(AdminController.class);

	@Autowired
	private JaxService jaxService;

	@Autowired
	private AdminService adminService;

	@Autowired
	private ExchangeRateClient exchangeRateClient;

	@Autowired
	private MetaClient metaClient;

	@Autowired
	JaxMetaInfo metaData;

	@RequestMapping(value = "/customer/unlock/{civilid}", method = RequestMethod.GET)
	public ApiResponse unlockCustomer(@PathVariable("civilid") String civilid) {
		Tenant tenant = metaData.getTenant();

		logger.info(String.format("Tenant is : %s", tenant.getCode()));

		logger.info("in unlockCustomer Request ");
		jaxService.setDefaults();
		ApiResponse response = adminService.unlockCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/customer/deactivate/{civilid}", method = RequestMethod.GET)
	public ApiResponse deActivateCustomer(@PathVariable("civilid") String civilid) {
		logger.info("in deActivateCustomer Request ");
		jaxService.setDefaults();
		ApiResponse response = adminService.deactivateCustomer(civilid);
		return response;
	}

	@RequestMapping(value = "/config", method = RequestMethod.GET)
	public ApiResponse createorUpdateOtpSettings(@RequestParam Integer maxValidateOtpAttempts,
			@RequestParam Integer maxSendOtpAttempts, @RequestParam Integer otpValidityTime) {
		logger.info("in createorUpdateOtpSettings Request ");

		ApiResponse response = adminService.createorUpdateOtpSettings(
				new OtpSettings(maxValidateOtpAttempts, maxSendOtpAttempts, otpValidityTime));
		return response;
	}

	@RequestMapping(value = "/exrate", method = RequestMethod.POST)
	public ApiResponse<BooleanResponse> setOnlineExchangeRates(
			@ApiParam(required = true, allowableValues = "INR, AED, AUD, BDT, BAM, GBP, EURO, PKR, USD, ZAR, LKR,BHD,CAD,EGP,MUR,NPR,OMR,SAR", value = "Select quote", name = "quoteName") @RequestParam(required = true) String quoteName,
			@RequestParam BigDecimal value) {
		ApiResponse<BooleanResponse> response = exchangeRateClient.setExchangeRate(quoteName, value);
		return response;
	}

	@RequestMapping(value = "/exrate/placeorder", method = RequestMethod.POST)
	public ApiResponse<BooleanResponse> setOnlineExchangeRatesPlaceorder(
			@ApiParam(required = true, allowableValues = "INR, AED, AUD, BDT, BAM, GBP, EURO, PKR, USD, ZAR, LKR,BHD,CAD,EGP,MUR,NPR,OMR,SAR", value = "Select quote", name = "quoteName") @RequestParam(required = true) String quoteName,
			@RequestParam BigDecimal bankId,@RequestParam BigDecimal value) {
		ApiResponse<BooleanResponse> response = exchangeRateClient.setExchangeRatePlaceorder(quoteName, bankId, value);
		return response;
	}
}
