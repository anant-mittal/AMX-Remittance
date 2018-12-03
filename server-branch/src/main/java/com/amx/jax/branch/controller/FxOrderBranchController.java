package com.amx.jax.branch.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.fx.FxOrderBranchClient;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.UserStockDto;

import io.swagger.annotations.Api;

@PreAuthorize("hasPermission('CUSTOMER_MGMT.FXORDER', 'VIEW')")
@RestController
@Api(value = "Order Management APIs")
public class FxOrderBranchController {
	@Autowired
	private FxOrderBranchClient fxOrderBranchClient;
	
	@RequestMapping(value = "/api/fxo/order/list", method = { RequestMethod.GET })
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> getOrderList(){
		return fxOrderBranchClient.fetchBranchOrderManagement();
	}
	
	@RequestMapping(value = "/api/fxo/order/details",  method = { RequestMethod.POST })
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> getOrderDetails(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		return fxOrderBranchClient.fetchBranchOrderDetails(orderNumber, orderYear);
	}
	
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.POST })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(@RequestParam(value = "currencyId", required = true) BigDecimal foreignCurrencyId){
		return fxOrderBranchClient.fetchBranchStockDetailsByCurrency(foreignCurrencyId);
	}
	
	@RequestMapping(value = "/api/fxo/currency/stock",  method = { RequestMethod.GET })
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails(){
		return fxOrderBranchClient.fetchBranchStockDetails();
	}
	
	@RequestMapping(value = "/api/fxo/drivers",  method = { RequestMethod.GET })
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchDriverList(){
		return fxOrderBranchClient.fetchBranchEmployee();
	}
	
	@RequestMapping(value = "/api/fxo/driver/assign",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> assignDriver(
			@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,
			@RequestParam(value = "driverId", required = true) BigDecimal driverId,
			@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		return fxOrderBranchClient.assignDriver(orderNumber, orderYear, driverId);
	}
	
	@RequestMapping(value = "/api/fxo/order/dispatch",  method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel,Object> dispatchOrder(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest){
		return fxOrderBranchClient.dispatchOrder(fcSaleBranchDispatchRequest);
	}
	
}
