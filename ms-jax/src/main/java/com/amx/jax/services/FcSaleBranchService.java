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
import com.amx.jax.dbmodel.fx.OrderManagementView;
import com.amx.jax.error.JaxError;
import com.amx.jax.manager.FcSaleBranchOrderManager;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.UserStockDto;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class FcSaleBranchService extends AbstractService{

	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleBranchOrderManager branchOrderManager;

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
		List<OrderManagementView> orderManagement = branchOrderManager.fetchFcSaleOrderManagement(applicationCountryId,employeeId);
		if(orderManagement != null && orderManagement.size() != 0) {
			saleOrderManage  = convertFcSaleOrderManagementDTO(orderManagement,applicationCountryId,employeeId);
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

		return AmxApiResponse.buildList(saleOrderManage);
	}

	public List<FcSaleOrderManagementDTO> convertFcSaleOrderManagementDTO(List<OrderManagementView> orderManagementView,BigDecimal applicationCountryId,BigDecimal employeeId){
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

						HashMap<BigDecimal, BigDecimal> foreignCurrencyAmt = new HashMap<>();
						String mutipleAmt = null;
						String mutipleInventoryId = null;
						for (OrderManagementView fcSaleOrderManagement : orderManagementView) {
							if(fcSaleOrderManagement.getCollectionDocumentNo().compareTo(orderManagement.getCollectionDocumentNo()) == 0) {
								if(mutipleAmt != null) {
									mutipleAmt = mutipleAmt.concat(",").concat(fcSaleOrderManagement.getForeignCurrencyQuote().concat(" ").concat(fcSaleOrderManagement.getForeignTrnxAmount().toString()));
									foreignCurrencyAmt.put(fcSaleOrderManagement.getForeignCurrencyId(), fcSaleOrderManagement.getForeignTrnxAmount());
								}else {
									mutipleAmt = fcSaleOrderManagement.getForeignCurrencyQuote().concat(" ").concat(fcSaleOrderManagement.getForeignTrnxAmount().toString());
									foreignCurrencyAmt.put(fcSaleOrderManagement.getForeignCurrencyId(), fcSaleOrderManagement.getForeignTrnxAmount());
								}
								if(mutipleInventoryId != null) {
									mutipleInventoryId = mutipleInventoryId.concat(",").concat(fcSaleOrderManagement.getInventoryId());
								}else {
									mutipleInventoryId = fcSaleOrderManagement.getInventoryId();
								}
							}
						}
						fcSaleOrder.setMutipleFcAmount(mutipleAmt);
						fcSaleOrder.setMutipleInventoryId(mutipleInventoryId);
						
						Boolean status = checkFcSaleStockAvailable(foreignCurrencyAmt,applicationCountryId,employeeId);
						if(status) {
							lstFcSaleOrder.add(fcSaleOrder);
						}
					}
				}
			}
		}catch (Exception e) {
			throw new GlobalException("Unable to fetch records",JaxError.UNKNOWN_JAX_ERROR);
		}
		
		return lstFcSaleOrder;
	}
	
	// checking stock available for fc sale
	public Boolean checkFcSaleStockAvailable(HashMap<BigDecimal, BigDecimal> foreignCurrencyAmt,BigDecimal applicationCountryId,BigDecimal employeeId) {
		HashMap<BigDecimal, BigDecimal> foreignCurrencySumAmt = new HashMap<>();
		List<Object[]> orderManagement = fetchFcSaleUserStockSum(applicationCountryId,employeeId);
		for (Object object : orderManagement) {
			Object[] currencyAmt = (Object[]) object;
			if (currencyAmt.length >= 2) {
				foreignCurrencySumAmt.put(new BigDecimal(currencyAmt[0].toString()),new BigDecimal(currencyAmt[1].toString()));
			}
		}
		Boolean status = Boolean.TRUE;
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
		
		return status;
	}
	
	public List<Object[]> fetchFcSaleUserStockSum(BigDecimal countryId,BigDecimal employeeId){
		List<Object[]> orderManagement =  branchOrderManager.fetchUserStockViewSum(countryId,employeeId);
		return orderManagement;
	}

	/* 
	 * @param   :fetch Pending Orders by order number
	 * @return FcSaleOrderManagementDTO
	 */
	public AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchFcSaleOrderDetails(BigDecimal applicationCountryId,BigDecimal orderNumber,BigDecimal orderYear){
		List<FcSaleOrderManagementDTO> saleOrderManage = new ArrayList<>();
		if(applicationCountryId == null || applicationCountryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		List<OrderManagementView> orderManagement =  branchOrderManager.fetchFcSaleOrderDetails(applicationCountryId,orderNumber,orderYear);
		if(orderManagement != null && orderManagement.size() != 0) {
			saleOrderManage = convertFcSaleOrderDetails(orderManagement);
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

		return AmxApiResponse.buildList(saleOrderManage);
	}

	public List<FcSaleOrderManagementDTO> convertFcSaleOrderDetails(List<OrderManagementView> orderManagementView){
		List<FcSaleOrderManagementDTO> lstDto = new ArrayList<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		if(orderManagementView != null && orderManagementView.size() != 0) {

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
		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}
		if(foreignCurrencyId == null || foreignCurrencyId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("foreign currency id should not be blank",JaxError.NULL_CURRENCY_ID);
		}
		List<UserStockDto> userStock =  branchOrderManager.fetchUserStockViewByCurrency(countryId,employeeId,foreignCurrencyId);
		if(userStock != null && userStock.size() != 0) {
			// continue
		}else {
			throw new GlobalException("User stock records not found",JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(userStock);
	}
	
	public AmxApiResponse<UserStockDto,Object> fetchFcSaleUserStock(BigDecimal countryId,BigDecimal employeeId){
		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(employeeId == null || employeeId.compareTo(BigDecimal.ZERO) == 0){
			throw new GlobalException("Employee Id should not be blank",JaxError.NULL_EMPLOYEE_ID);
		}
		List<UserStockDto> userStock =  branchOrderManager.fetchUserStockView(countryId,employeeId);
		if(userStock != null && userStock.size() != 0) {
			// continue
		}else {
			throw new GlobalException("User stock records not found",JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(userStock);
	}

	public AmxApiResponse<FcEmployeeDetailsDto,Object> fetchDriverDetails(){
		List<FcEmployeeDetailsDto> driverEmp =  branchOrderManager.fetchEmpDriverDetails();
		if(driverEmp != null && driverEmp.size() != 0) {
			// continue
		}else {
			throw new GlobalException("driver records not found",JaxError.NO_RECORD_FOUND);
		}
		return AmxApiResponse.buildList(driverEmp);
	}
	
	public BoolRespModel assignDriver(BigDecimal countryId,BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId) {
		if(countryId == null || countryId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Application country id should not be blank",JaxError.NULL_APPLICATION_COUNTRY_ID);
		}
		if(orderNumber == null || orderNumber.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Order Number should not be blank",JaxError.NULL_ORDER_NUBMER);
		}
		if(driverId == null || driverId.compareTo(BigDecimal.ZERO)==0){
			throw new GlobalException("Driver id should not be blank",JaxError.NULL_DRIVER_ID);
		}
		Boolean status = branchOrderManager.saveAssignDriver(countryId,orderNumber,orderYear,driverId);
		if(status) {
			// success
		}else {
			throw new GlobalException("Driver id didn't updated",JaxError.SAVE_FAILED);
		}
		return new BoolRespModel(status);
	}
	
	public BoolRespModel dispatchOrder(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest,BigDecimal employeeId,BigDecimal countryId,BigDecimal companyId) {
		Boolean status = branchOrderManager.saveDispatchOrder(fcSaleBranchDispatchRequest,employeeId,countryId,companyId);
		return new BoolRespModel(status);
	}

}
