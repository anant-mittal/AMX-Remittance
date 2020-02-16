package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.remittance.RemittanceClient;
import com.amx.jax.model.request.remittance.PlaceOrderRequestModel;
import com.amx.jax.model.request.remittance.PlaceOrderResponseModel;
import com.amx.jax.model.request.remittance.PlaceOrderUpdateStatusDto;
import com.amx.jax.model.response.remittance.GsmPlaceOrderListDto;
import com.amx.jax.model.response.remittance.GsmSearchRequestParameter;
import com.amx.jax.model.response.remittance.RatePlaceOrderInquiryDto;
import com.amx.jax.model.response.remittance.RatePlaceOrderResponseModel;
import com.amx.jax.rbaac.IRbaacService;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Place Order  APIs")
@ApiStatusService(IRbaacService.class)
public class POBranchController {

	@Autowired
	private RemittanceClient branchRemittanceClient;

	@PreAuthorize("#placeOrderRequestModel.getBooGsm() == true ? hasPermission('CUSTOMER_MGMT.PLACE_ORDER.RATE_PROVIDER', 'VIEW') : true")
	@RequestMapping(value = "/api/placeorder/create", method = { RequestMethod.POST })
	public AmxApiResponse<RatePlaceOrderResponseModel, Object> createPlaceOrder(
			@RequestBody PlaceOrderRequestModel placeOrderRequestModel) {
		return branchRemittanceClient.savePlaceOrderApplication(placeOrderRequestModel);
	}

	@RequestMapping(value = "/api/placeorder/provider/list", method = { RequestMethod.POST })
	public AmxApiResponse<GsmPlaceOrderListDto, Object> getCountryWisePlaceOrderProviderList(
			@RequestBody GsmSearchRequestParameter requestParameter) {
		return branchRemittanceClient.getCountryWisePlaceOrderCount(requestParameter);
	}

	@RequestMapping(value = "/api/placeorder/provider/action", method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> updateRatePlaceOrder(
			@RequestBody PlaceOrderUpdateStatusDto placeOrderRequestUpdatDto) {
		return branchRemittanceClient.updateRatePlaceOrder(placeOrderRequestUpdatDto);
	}

	@RequestMapping(value = "/api/placeorder/consumer/list", method = { RequestMethod.POST })
	public AmxApiResponse<RatePlaceOrderInquiryDto, Object> getCountryWisePlaceOrderConsumerList(
			@RequestParam(required = false) BigDecimal countryBranchId) {
		return branchRemittanceClient.fetchPlaceOrderInquiry(countryBranchId);
	}

	@RequestMapping(value = "/api/placeorder/consumer/accept", method = { RequestMethod.POST })
	public AmxApiResponse<PlaceOrderResponseModel, Object> acceptPlaceOrder(@RequestParam BigDecimal ratePlaceOrderId) {
		return branchRemittanceClient.acceptPlaceOrderByCustomer(ratePlaceOrderId);
	}


}
