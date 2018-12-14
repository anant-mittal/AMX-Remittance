package com.amx.jax.controller;


import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.fx.IFxBranchOrderService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
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
	@Override
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
	@Override
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal companyId = metaData.getCompanyId();
		return fcSaleBranch.fetchFcSaleOrderDetails(countryId,orderNumber,orderYear,employeeId,companyId);
	}

	/**
	 * To get the fx fetch user stock by currency list
	 * 
	 */
	@RequestMapping(value = Path.FC_FETCH_STOCK_CURRENCY , method = RequestMethod.GET)
	@Override
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
	@Override
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
	@Override
	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchBranchEmployee() {
		return fcSaleBranch.fetchDriverDetails();
	}
	
	/**
	 * To get the save assign driver
	 * 
	 */
	@RequestMapping(value = Path.FC_ASSIGN_DRIVER , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> assignDriver(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear,@RequestParam(value = "driverId", required = true) BigDecimal driverId) {
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.assignDriver(countryId,orderNumber,orderYear,driverId,employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the save print order
	 * 
	 */
	@RequestMapping(value = Path.FC_PRINT_ORDER_SAVE , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<FxOrderReportResponseDto,Object> printOrderSave(@RequestBody FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest){
		BigDecimal employeeId = metaData.getEmployeeId();
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal companyId = metaData.getCompanyId();
		return fcSaleBranch.printOrderSave(fcSaleBranchDispatchRequest,employeeId,countryId,companyId);
	}
	
	/**
	 * To get the accept the order lock
	 * 
	 */
	@RequestMapping(value = Path.FC_ACCEPT_ORDER_LOCK , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> acceptOrderLock(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.acceptOrderLock(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the release the order lock
	 * 
	 */
	@RequestMapping(value = Path.FC_RELEASE_ORDER_LOCK , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> releaseOrderLock(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.releaseOrderLock(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the order to dispatch
	 * 
	 */
	@RequestMapping(value = Path.FC_DISPATCH_ORDER , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> dispatchOrder(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.dispatchOrder(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the order to Acknowledge driver
	 * 
	 */
	@RequestMapping(value = Path.FC_ACKNOWLEDGE_DRIVE , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> acknowledgeDrive(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.acknowledgeDriver(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the order to return Acknowledge
	 * 
	 */
	@RequestMapping(value = Path.FC_RETURN_ACKNOWLEDGE , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> returnAcknowledge(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.returnAcknowledge(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the order to accept cancellation
	 * 
	 */
	@RequestMapping(value = Path.FC_ACCEPT_CANCELLATION , method = RequestMethod.POST)
	@Override
	public AmxApiResponse<BoolRespModel,Object> acceptCancellation(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		BoolRespModel result = fcSaleBranch.acceptCancellation(countryId, orderNumber, orderYear, employeeId);
		return AmxApiResponse.build(result);
	}
	
	/**
	 * To get the re-print document order 
	 * 
	 */
	@RequestMapping(value = Path.FC_REPRINT_ORDER , method = RequestMethod.GET)
	@Override
	public AmxApiResponse<FxOrderReportResponseDto,Object> reprintOrder(@RequestParam(value = "orderNumber", required = true) BigDecimal orderNumber,@RequestParam(value = "orderYear", required = true) BigDecimal orderYear){
		BigDecimal countryId = metaData.getCountryId();
		BigDecimal employeeId = metaData.getEmployeeId();
		return fcSaleBranch.reprintOrder(countryId, orderNumber, orderYear, employeeId);
	}

}