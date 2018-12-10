package com.amx.jax.services;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleApplicationTransactionManager;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleCurrencyAmountModel;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.UserStockDto;
import com.amx.jax.util.RoundUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class FcSaleBranchService extends AbstractService{

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleBranchOrderManager branchOrderManager;

	@Autowired
	FcSaleApplicationTransactionManager fcSaleApplicationTransactionManager;

	/* 
	 * @param   :fetch List of Pending Orders
	 * @return FcSaleOrderManagementDTO
	 */
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchFcSaleOrderManagement(BigDecimal applicationCountryId,BigDecimal employeeId){
		List<FcSaleOrderManagementDTO> saleOrderManage = null;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}
		try {
			HashMap<String, Object> orderDetails = branchOrderManager.fetchFcSaleOrderManagement(applicationCountryId,employeeId);
			if(orderDetails != null) {
				if(orderDetails.get("ORDERS") != null && orderDetails.get("AREA") != null) {
					List<OrderManagementView> orderManagement = (List<OrderManagementView>) orderDetails.get("ORDERS");
					Boolean areaCodeCheck = (Boolean) orderDetails.get("AREA");

					if(orderManagement != null && orderManagement.size() != 0) {
						saleOrderManage  = convertFcSaleOrderManagementDTO(orderManagement,applicationCountryId,employeeId,areaCodeCheck);
						if(saleOrderManage != null && saleOrderManage.size() != 0) {
							// continue
						}else {
							// error
							throw new GlobalException("Order Management records not found",JaxError.NO_RECORD_FOUND);
						}
					}else {
						// error
						throw new GlobalException("Order Management records not found",JaxError.NO_RECORD_FOUND);
					}
				}
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.buildList(saleOrderManage);
	}

	public List<FcSaleOrderManagementDTO> convertFcSaleOrderManagementDTO(List<OrderManagementView> orderManagementView,BigDecimal applicationCountryId,BigDecimal employeeId,Boolean areaCodeCheck){
		List<FcSaleOrderManagementDTO> lstFcSaleOrder = new ArrayList<>();
		List<BigDecimal> duplicate = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		try {
			if(orderManagementView != null && orderManagementView.size() != 0) {
				for (OrderManagementView orderManagement : orderManagementView) {
					if(!duplicate.contains(orderManagement.getCollectionDocumentNo())) {
						duplicate.add(orderManagement.getCollectionDocumentNo());
						FcSaleOrderManagementDTO fcSaleOrder = new FcSaleOrderManagementDTO();
						fcSaleOrder.setApplicationCountryId(orderManagement.getApplicationCountryId());
						fcSaleOrder.setCollectionDocFinanceYear(orderManagement.getCollectionDocFinanceYear());
						fcSaleOrder.setCollectionDocumentNo(orderManagement.getCollectionDocumentNo());
						fcSaleOrder.setCustomerMobile(orderManagement.getCustomerMobile());
						fcSaleOrder.setDeliveryDate(dateFormat.format(orderManagement.getDeliveryDate()));
						fcSaleOrder.setDeliveryTime(orderManagement.getDeliveryTime());
						fcSaleOrder.setDenominationType(orderManagement.getDenominationType());
						fcSaleOrder.setDocumentNo(orderManagement.getDocumentNo());
						fcSaleOrder.setDriverEmployeId(orderManagement.getDriverEmployeId());
						fcSaleOrder.setDriverEmployeName(orderManagement.getDriverEmployeeName());
						fcSaleOrder.setOrderStatus(orderManagement.getOrderStatus());
						fcSaleOrder.setOrderStatusDesc(orderManagement.getOrderStatusDesc());

						HashMap<BigDecimal, BigDecimal> foreignCurrencyAmt = new HashMap<>();
						List<FcSaleCurrencyAmountModel> lstCurrencyAmt = new ArrayList<>();
						String mutipleInventoryId = null;
						for (OrderManagementView fcSaleOrderManagement : orderManagementView) {
							if(fcSaleOrderManagement.getCollectionDocumentNo().compareTo(orderManagement.getCollectionDocumentNo()) == 0) {

								FcSaleCurrencyAmountModel fcSaleCurrencyAmountModel = new FcSaleCurrencyAmountModel();
								fcSaleCurrencyAmountModel.setAmount(fcSaleOrderManagement.getForeignTrnxAmount());
								fcSaleCurrencyAmountModel.setCurrencyQuote(fcSaleOrderManagement.getForeignCurrencyQuote());
								lstCurrencyAmt.add(fcSaleCurrencyAmountModel);

								foreignCurrencyAmt.put(fcSaleOrderManagement.getForeignCurrencyId(), fcSaleOrderManagement.getForeignTrnxAmount());

								if(mutipleInventoryId != null) {
									mutipleInventoryId = mutipleInventoryId.concat(",").concat(fcSaleOrderManagement.getInventoryId());
								}else {
									mutipleInventoryId = fcSaleOrderManagement.getInventoryId();
								}
							}
						}

						fcSaleOrder.setMutipleFcAmount(lstCurrencyAmt);
						fcSaleOrder.setMutipleInventoryId(mutipleInventoryId);

						if(areaCodeCheck) {
							lstFcSaleOrder.add(fcSaleOrder);
						}else {
							Boolean status = checkFcSaleStockAvailable(foreignCurrencyAmt,applicationCountryId,employeeId);
							if(status) {
								lstFcSaleOrder.add(fcSaleOrder);
							}
						}
					}
				}
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException("Converting multiple records to single by collection document failed",JaxError.UNABLE_CONVERT_PENDING_RECORDS);
		}

		return lstFcSaleOrder;
	}

	// checking stock available for fc sale
	public Boolean checkFcSaleStockAvailable(HashMap<BigDecimal, BigDecimal> foreignCurrencyAmt,BigDecimal applicationCountryId,BigDecimal employeeId) {
		HashMap<BigDecimal, BigDecimal> foreignCurrencySumAmt = new HashMap<>();
		Boolean status = Boolean.TRUE;
		List<Object[]> orderManagement = fetchFcSaleUserStockSum(applicationCountryId,employeeId);
		if(orderManagement != null && orderManagement.size() != 0) {
			for (Object object : orderManagement) {
				Object[] currencyAmt = (Object[]) object;
				if (currencyAmt.length >= 2) {
					foreignCurrencySumAmt.put(new BigDecimal(currencyAmt[0].toString()),new BigDecimal(currencyAmt[1].toString()));
				}
			}
			
			for (HashMap.Entry<BigDecimal, BigDecimal> currencyAmt : foreignCurrencyAmt.entrySet()) {
				BigDecimal currency = currencyAmt.getKey();
				BigDecimal amount = currencyAmt.getValue();
				if(currency != null && foreignCurrencySumAmt.get(currency) != null) {
					if(amount.compareTo(foreignCurrencySumAmt.get(currency)) > 0) {
						status = Boolean.FALSE;
						break;
					}
				}else {
					status = Boolean.FALSE;
					break;
				}
			}
		}else {
			throw new GlobalException("Employee stock not available",JaxError.EMPTY_STOCK_EMPLOYEE);
		}
		
		return status;
	}

	public List<Object[]> fetchFcSaleUserStockSum(BigDecimal countryId,BigDecimal employeeId){
		List<Object[]> orderManagement = branchOrderManager.fetchUserStockViewSum(countryId,employeeId);
		return orderManagement;
	}

	/* 
	 * @param   :fetch Pending Orders by order number
	 * @return FcSaleOrderManagementDTO
	 */
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchFcSaleOrderDetails(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId,BigDecimal companyId){
		List<FcSaleOrderManagementDTO> saleOrderManage = new ArrayList<>();

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			List<OrderManagementView> orderManagement =  branchOrderManager.fetchFcSaleOrderDetails(applicationCountryId,orderNumber,orderYear);
			if(orderManagement != null && orderManagement.size() != 0) {
				saleOrderManage = convertFcSaleOrderDetails(orderManagement,employeeId,companyId);
				if(saleOrderManage != null && saleOrderManage.size() != 0) {
					// continue
				}else {
					// error
					throw new GlobalException("Order Management records not found",JaxError.NO_RECORD_FOUND);
				}
			}else {
				// error
				throw new GlobalException("Order Management records not found",JaxError.NO_RECORD_FOUND);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.buildList(saleOrderManage);
	}

	public List<FcSaleOrderManagementDTO> convertFcSaleOrderDetails(List<OrderManagementView> orderManagementView,BigDecimal employeeId,BigDecimal companyId){
		List<FcSaleOrderManagementDTO> lstDto = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		String deliveryAddress = null;
		if(orderManagementView != null && orderManagementView.size() != 0) {

			OrderManagementView ordDelieryAddress = orderManagementView.get(0);
			if(ordDelieryAddress.getDeliveryDetailsId() != null) {
				String address = fcSaleApplicationTransactionManager.getDeliveryAddress(ordDelieryAddress.getCustomerId(),ordDelieryAddress.getDeliveryDetailsId());
				deliveryAddress = address;
			}

			for (OrderManagementView orderManagement : orderManagementView) {

				FcSaleOrderManagementDTO fcSaleOrder = new FcSaleOrderManagementDTO();

				fcSaleOrder.setApplicationCountryId(orderManagement.getApplicationCountryId());
				fcSaleOrder.setCollectionDocFinanceYear(orderManagement.getCollectionDocFinanceYear());
				fcSaleOrder.setCollectionDocumentNo(orderManagement.getCollectionDocumentNo());
				fcSaleOrder.setCustomerMobile(orderManagement.getCustomerMobile());
				fcSaleOrder.setDeliveryDate(dateFormat.format(orderManagement.getDeliveryDate()));
				fcSaleOrder.setDeliveryTime(orderManagement.getDeliveryTime());
				fcSaleOrder.setDenominationType(orderManagement.getDenominationType());
				fcSaleOrder.setDocumentNo(orderManagement.getDocumentNo());
				fcSaleOrder.setDriverEmployeId(orderManagement.getDriverEmployeId());
				fcSaleOrder.setForeignCurrencyQuote(orderManagement.getForeignCurrencyQuote());
				fcSaleOrder.setForeignCurrencyId(orderManagement.getForeignCurrencyId());
				fcSaleOrder.setForeignTrnxAmount(orderManagement.getForeignTrnxAmount());
				fcSaleOrder.setPurposeDesc(orderManagement.getPurposeDesc());
				fcSaleOrder.setPurposeId(orderManagement.getPurposeId());
				fcSaleOrder.setSourceDesc(orderManagement.getSourceDesc());
				fcSaleOrder.setSourceId(orderManagement.getSourceId());
				fcSaleOrder.setInventoryId(orderManagement.getInventoryId());
				fcSaleOrder.setAreaCode(orderManagement.getAreaCode());
				fcSaleOrder.setDeliveryDetailsId(orderManagement.getDeliveryDetailsId());
				fcSaleOrder.setReceiptPaymentId(orderManagement.getReceiptPaymentId());
				fcSaleOrder.setLocalNetAmount(orderManagement.getLocalNetAmount());
				fcSaleOrder.setTransactionActualRate(orderManagement.getTransactionActualRate());
				fcSaleOrder.setCustomerName(orderManagement.getCustomerName());
				fcSaleOrder.setCustomerId(orderManagement.getCustomerId());
				fcSaleOrder.setDriverEmployeName(orderManagement.getDriverEmployeeName());
				fcSaleOrder.setOrderStatus(orderManagement.getOrderStatus());
				fcSaleOrder.setOrderStatusDesc(orderManagement.getOrderStatusDesc());
				if(orderManagement.getTransactionActualRate() != null && orderManagement.getForeignCurrencyId() != null) {
					BigDecimal localRate = new BigDecimal((BigDecimal.ONE).doubleValue()/orderManagement.getTransactionActualRate().doubleValue());
					BigDecimal currencyDecimal = branchOrderManager.fetchCurrencyMasterDetails(orderManagement.getForeignCurrencyId());
					if(currencyDecimal != null && currencyDecimal.compareTo(BigDecimal.ZERO) != 0) {
						fcSaleOrder.setLocalActualRate(RoundUtil.roundBigDecimal(localRate, currencyDecimal.intValue()));
					}
				}

				if(orderManagement.getOrderLock() != null) {
					fcSaleOrder.setOrderLock(dateTimeFormat.format(orderManagement.getOrderLock()));
				}
				fcSaleOrder.setEmployeeId(orderManagement.getEmployeeId());
				if(orderManagement.getEmployeeId() != null){
					if(orderManagement.getEmployeeId().compareTo(employeeId) == 0){
						fcSaleOrder.setOrderEmployee(Boolean.TRUE);
					}else {
						fcSaleOrder.setOrderEmployee(Boolean.FALSE);
					}
				}else {
					fcSaleOrder.setOrderEmployee(Boolean.TRUE);
				}

				List<UserStockDto> userStockDto = branchOrderManager.fetchCurrencyAdjustDetails(orderManagement.getDocumentNo(), orderManagement.getCollectionDocFinanceYear(), companyId, ConstantDocument.DOCUMENT_CODE_FOR_FCSALE);
				if(userStockDto != null) {
					fcSaleOrder.setFcDenomination(userStockDto);
				}

				if(deliveryAddress != null) {
					fcSaleOrder.setDeliveryAddress(deliveryAddress);
				}

				lstDto.add(fcSaleOrder);
			}
		}

		return lstDto;
	}

	/* 
	 * @param   :fetch stock
	 * @return UserStockView
	 */
	public AmxApiResponse<UserStockDto,Object> fetchFcSaleUserStockByCurrency(BigDecimal countryId,BigDecimal employeeId,BigDecimal foreignCurrencyId){
		List<UserStockDto> userStock = new ArrayList<>();

		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}
		if(foreignCurrencyId == null || foreignCurrencyId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("foreign currency id should not be blank",JaxError.NULL_CURRENCY_ID);
		}

		try {
			userStock = branchOrderManager.fetchUserStockViewByCurrency(countryId,employeeId,foreignCurrencyId);
			if(userStock != null && userStock.size() != 0) {
				// continue
			}else {
				throw new GlobalException("User stock records not found",JaxError.NO_RECORD_FOUND);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.buildList(userStock);
	}

	public AmxApiResponse<UserStockDto,Object> fetchFcSaleUserStock(BigDecimal countryId,BigDecimal employeeId){
		List<UserStockDto> userStock = new ArrayList<>();

		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			userStock =  branchOrderManager.fetchUserStockView(countryId,employeeId);
			if(userStock != null && userStock.size() != 0) {
				// continue
			}else {
				throw new GlobalException("User stock records not found",JaxError.NO_RECORD_FOUND);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.buildList(userStock);
	}

	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchDriverDetails(){
		List<FcEmployeeDetailsDto> driverEmp = new ArrayList<>();

		try {
			driverEmp =  branchOrderManager.fetchEmpDriverDetails();
			if(driverEmp != null && driverEmp.size() != 0) {
				// continue
			}else {
				throw new GlobalException("driver records not found",JaxError.NO_RECORD_FOUND);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.buildList(driverEmp);
	}

	public BoolRespModel assignDriver(BigDecimal countryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(driverId == null || driverId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Driver id should not be blank",JaxError.NULL_DRIVER_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.saveAssignDriver(countryId,orderNumber,orderYear,driverId,employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("Driver id didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public AmxApiResponse<FxOrderReportResponseDto,Object> printOrderSave(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,BigDecimal employeeId,BigDecimal countryId,BigDecimal companyId) {
		FxOrderReportResponseDto fxOrderReportResponseDto = null;

		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}
		if(companyId == null || companyId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Company Id should not be blank",JaxError.NULL_COMPANY_ID);
		}
		if(fcSaleBranchDispatchRequest == null){
			throw new GlobalException("Currency Denomination should not be empty",JaxError.EMPTY_CURRENCY_DENOMINATION_DETAILS);
		}

		try {
			fxOrderReportResponseDto = branchOrderManager.printOrderSave(fcSaleBranchDispatchRequest,employeeId,countryId,companyId);
			if(fxOrderReportResponseDto != null) {
				// success
			}else {
				throw new GlobalException("Print order save didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.build(fxOrderReportResponseDto);
	}

	public BoolRespModel acceptOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId){
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.acceptOrderLock(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("accept Order lock didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public BoolRespModel releaseOrderLock(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId){
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.releaseOrderLock(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("Release Order lock didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public BoolRespModel dispatchOrder(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.dispatchOrder(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("dispatch order status didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public BoolRespModel acknowledgeDriver(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.acknowledgeDriver(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("acknowledge driver status didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public BoolRespModel returnAcknowledge(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.returnAcknowledge(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("return acknowledge status didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public BoolRespModel acceptCancellation(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		Boolean status = Boolean.FALSE;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			status = branchOrderManager.acceptCancellation(applicationCountryId, orderNumber, orderYear, employeeId);
			if(status) {
				// success
			}else {
				throw new GlobalException("accept cancellation status didn't updated",JaxError.SAVE_FAILED);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return new BoolRespModel(status);
	}

	public AmxApiResponse<FxOrderReportResponseDto,Object> reprintOrder(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal employeeId) {
		FxOrderReportResponseDto fxOrderReportResponseDto = null;

		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(orderYear == null || orderYear.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Year should not be blank",JaxError.NULL_ORDER_YEAR);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}

		try {
			fxOrderReportResponseDto = branchOrderManager.reprintOrder(applicationCountryId, orderNumber, orderYear, employeeId);
			if(fxOrderReportResponseDto != null) {
				// success
			}else {
				throw new GlobalException("Re-print order unable to print",JaxError.UNABLE_TO_PRINT_ORDER);
			}
		}catch (GlobalException e) {
			throw new GlobalException(e.getErrorMessage(),e.getErrorKey());
		}catch (Exception e) {
			throw new GlobalException(e.getMessage());
		}

		return AmxApiResponse.build(fxOrderReportResponseDto);
	}
}
