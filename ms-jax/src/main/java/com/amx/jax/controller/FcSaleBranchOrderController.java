package com.amx.jax.controller;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.IFxBranchOrderService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.services.FcSaleBranchService;

/**
*
* @author : chiranjeevi
* @date : 28/11/2018
*/

@RestController
public class FcSaleBranchOrderController implements IFxBranchOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleBranchService fcSaleBranch;

	@Autowired
	MetaData metaData;

	/**
	 * To get the fx pending order management list
	 * 
	 */
	@RequestMapping(value = Path.FC_PENDING_ORDER_MANAGEMENT , method = RequestMethod.GET)
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderManagement() {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		return fcSaleBranch.fetchFcSaleOrderManagement(countryId,employeeId);
	}

	/**
	 * To get the fx pending order details by number list
	 * 
	 */
	@RequestMapping(value = Path.FC_FETCH_ORDER_MANAGEMENT , method = RequestMethod.GET)
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber) {
		BigDecimal countryId = metaData.getCountryId();
		return fcSaleBranch.fetchFcSaleOrderDetails(countryId,orderNumber);
	}

	/**
	 * To get the fx fetch user stock by currency list
	 * 
	 */
	@RequestMapping(value = Path.FC_FETCH_STOCK_CURRENCY , method = RequestMethod.GET)
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(@RequestParam(value = "foreignCurrencyId", required = true) BigDecimal foreignCurrencyId) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		return fcSaleBranch.fetchFcSaleUserStockByCurrency(countryId,employeeId,foreignCurrencyId);
	}

	/**
	 * To get the fx fetch user stock list
	 * 
	 */
	@RequestMapping(value = Path.FC_FETCH_STOCK , method = RequestMethod.GET)
	public AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails() {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();

		return fcSaleBranch.fetchFcSaleUserStock(countryId,employeeId);
	}

	/**
	 * To get the fx fetch driver list
	 * 
	 */
	@RequestMapping(value = Path.FC_EMLOYEE_DRIVERS , method = RequestMethod.GET)
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchBranchEmployee() {
		return fcSaleBranch.fetchDriverDetails();
	}
	
	/**
	 * To get the save assign driver
	 * 
	 */
	@RequestMapping(value = Path.FC_ASSIGN_DRIVER , method = RequestMethod.POST)
	public AmxApiResponse<Boolean,Object> assignDriver(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "driverId", required = true) BigDecimal driverId) {
		BigDecimal countryId = metaData.getCountryId();
		return fcSaleBranch.assignDriver(countryId,orderNumber,driverId);
	}

}
