package com.amx.jax.client.fx;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.fx.FcDeliveryBranchOrderSearchRequest;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.UserStockDto;

public interface IFxBranchOrderService extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/branch";
		public static final String FC_PENDING_ORDER_MANAGEMENT = PREFIX + "/pending-order-management/";
		public static final String FC_FETCH_ORDER_MANAGEMENT = PREFIX + "/fetch-order-details/";
		public static final String FC_FETCH_STOCK_CURRENCY = PREFIX + "/fetch-stock-currency/";
		public static final String FC_FETCH_STOCK = PREFIX + "/fetch-stock/";
		public static final String FC_EMLOYEE_DRIVERS = PREFIX + "/employee-drivers/";
		public static final String FC_ASSIGN_DRIVER = PREFIX + "/assign-driver/";
		public static final String FC_DISPATCH_ORDER = PREFIX + "/dispatch-order/";
		public static final String FC_ACCEPT_ORDER_LOCK = PREFIX + "/accept-order-lock/";
		public static final String FC_RELEASE_ORDER_LOCK = PREFIX + "/release-order-lock/";
		public static final String FC_PRINT_ORDER_SAVE = PREFIX + "/print-order-save/";
		public static final String FC_ACKNOWLEDGE_DRIVE = PREFIX + "/acknowledge-drive/";
		public static final String FC_RETURN_ACKNOWLEDGE = PREFIX + "/return-acknowledge/";
		public static final String FC_ACCEPT_CANCELLATION = PREFIX + "/accept-cancellation/";
		public static final String FC_REPRINT_ORDER = PREFIX + "/reprint-order/";
		public static final String FC_SEARCH_ORDER = PREFIX + "/search-order/";
		
	}

	public static class Params {
		public static final String FX_ORDER_NUMBER = "orderNumber";
		public static final String FX_ORDER_YEAR = "orderYear";
		public static final String FX_CURRENCY_ID = "foreignCurrencyId";
		public static final String FX_DRIVER_ID = "driverId";
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.NULL_AREA_CODE,JaxError.INVALID_EMPLOYEE,JaxError.UNABLE_CONVERT_PENDING_RECORDS,JaxError.EMPTY_STOCK_EMPLOYEE })
	AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderManagement();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID })
	AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(BigDecimal orderNumber,BigDecimal orderYear);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_CURRENCY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.INVALID_EMPLOYEE })
	AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(BigDecimal foreignCurrencyId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.INVALID_EMPLOYEE })
	AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FcEmployeeDetailsDto, Object> fetchBranchEmployee();

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_DRIVER_ID,JaxError.NULL_ORDER_NUBMER,JaxError.SAVE_FAILED,JaxError.NULL_ORDER_YEAR,JaxError.ORDER_STATUS_MISMATCH,
		JaxError.DRIVER_ALREADY_ASSIGNED,JaxError.NO_DELIVERY_DETAILS,JaxError.NULL_EMPLOYEE_ID,JaxError.INVALID_EMPLOYEE,JaxError.NULL_COMPANY_ID,JaxError.NULL_ORDER_STATUS})
	AmxApiResponse<BoolRespModel,Object> assignDriver(BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId);

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.NULL_COMPANY_ID,JaxError.SAVE_FAILED,JaxError.EMPTY_CURRENCY_DENOMINATION_DETAILS,JaxError.NO_DELIVERY_DETAILS,
		JaxError.INVALID_EMPLOYEE,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.INCORRECT_CURRENCY_DENOMINATION,JaxError.BLANK_DOCUMENT_DETAILS,JaxError.MISMATCH_COLLECTION_AMOUNT,JaxError.INVENTORY_ID_EXISTS,
		JaxError.INVALID_CURRENCY_DENOMINATION,JaxError.CURRENCY_STOCK_NOT_AVAILABLE,JaxError.MISMATCH_CURRENT_STOCK,JaxError.MISMATCH_ADJ_AMT_AND_DENOMINATION_AMT_QUANTITY,JaxError.ORDER_STATUS_MISMATCH,
		JaxError.INVALID_CUSTOMER,JaxError.NO_RECORD_FOUND,JaxError.INVALID_COMPANY_ID,JaxError.PAYMENT_DETAILS_NOT_FOUND})
	AmxApiResponse<FxOrderReportResponseDto,Object> printOrderSave(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest);

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.NULL_ORDER_NUBMER,JaxError.SAVE_FAILED,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.ORDER_LOCKED_OTHER_EMPLOYEE
		,JaxError.NO_DELIVERY_DETAILS,JaxError.NULL_ORDER_YEAR,JaxError.INVALID_EMPLOYEE,JaxError.ORDER_STATUS_MISMATCH})
	AmxApiResponse<BoolRespModel,Object> acceptOrderLock(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID,JaxError.NULL_ORDER_NUBMER,JaxError.SAVE_FAILED,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.ORDER_RELEASED_ALREADY
		,JaxError.NO_DELIVERY_DETAILS,JaxError.NULL_ORDER_YEAR,JaxError.INVALID_EMPLOYEE,JaxError.ORDER_STATUS_MISMATCH})
	AmxApiResponse<BoolRespModel,Object> releaseOrderLock(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID,JaxError.SAVE_FAILED,JaxError.INVALID_EMPLOYEE
		,JaxError.ORDER_IS_NOT_LOCK,JaxError.ORDER_STATUS_MISMATCH,JaxError.NO_DELIVERY_DETAILS,JaxError.FC_CURRENCY_DELIVERY_DETAIL_NOT_FOUND})
	AmxApiResponse<BoolRespModel,Object> dispatchOrder(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID,JaxError.SAVE_FAILED,JaxError.INVALID_EMPLOYEE
		,JaxError.ORDER_IS_NOT_LOCK,JaxError.ORDER_STATUS_MISMATCH,JaxError.NO_DELIVERY_DETAILS})
	AmxApiResponse<BoolRespModel,Object> acknowledgeDrive(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID,JaxError.SAVE_FAILED,JaxError.INVALID_EMPLOYEE
		,JaxError.ORDER_IS_NOT_LOCK,JaxError.ORDER_STATUS_MISMATCH,JaxError.NO_DELIVERY_DETAILS})
	AmxApiResponse<BoolRespModel,Object> returnAcknowledge(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID,JaxError.SAVE_FAILED,JaxError.INVALID_EMPLOYEE
		,JaxError.ORDER_IS_NOT_LOCK,JaxError.ORDER_STATUS_MISMATCH,JaxError.NO_DELIVERY_DETAILS})
	AmxApiResponse<BoolRespModel,Object> acceptCancellation(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER,JaxError.NULL_ORDER_YEAR,JaxError.NULL_EMPLOYEE_ID,JaxError.UNABLE_TO_PRINT_ORDER,JaxError.INVALID_CUSTOMER
		,JaxError.INVALID_COLLECTION_DOCUMENT_NO,JaxError.NO_RECORD_FOUND,JaxError.INVALID_COMPANY_ID,JaxError.PAYMENT_DETAILS_NOT_FOUND})
	AmxApiResponse<FxOrderReportResponseDto,Object> reprintOrder(BigDecimal orderNumber,BigDecimal orderYear);
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FxOrderTransactionHistroyDto, Object> searchOrder(FcDeliveryBranchOrderSearchRequest fcDeliveryBranchOrderSearchRequest);
}
