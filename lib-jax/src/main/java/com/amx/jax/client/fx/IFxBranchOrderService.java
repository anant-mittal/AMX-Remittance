package com.amx.jax.client.fx;

import java.math.BigDecimal;

import org.springframework.web.bind.annotation.RequestBody;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.error.JaxError;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.model.request.fx.FcSaleBranchDispatchRequest;
import com.amx.jax.model.response.fx.FcEmployeeDetailsDto;
import com.amx.jax.model.response.fx.FcSaleOrderManagementDTO;
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
	}

	public static class Params {
		public static final String FX_ORDER_NUMBER = "orderNumber";
		public static final String FX_ORDER_YEAR = "orderYear";
		public static final String FX_CURRENCY_ID = "foreignCurrencyId";
		public static final String FX_DRIVER_ID = "driverId";
	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID })
	AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderManagement();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_ORDER_NUBMER })
	AmxApiResponse<FcSaleOrderManagementDTO,Object> fetchBranchOrderDetails(BigDecimal orderNumber,BigDecimal orderYear);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_CURRENCY_ID,JaxError.NULL_EMPLOYEE_ID })
	AmxApiResponse<UserStockDto,Object> fetchBranchStockDetailsByCurrency(BigDecimal foreignCurrencyId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_EMPLOYEE_ID })
	AmxApiResponse<UserStockDto,Object> fetchBranchStockDetails();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FcEmployeeDetailsDto, Object> fetchBranchEmployee();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.NULL_APPLICATION_COUNTRY_ID,JaxError.NULL_DRIVER_ID,JaxError.NULL_ORDER_NUBMER,JaxError.SAVE_FAILED })
	AmxApiResponse<BoolRespModel,Object> assignDriver(BigDecimal orderNumber,BigDecimal orderYear,BigDecimal driverId);
	
	AmxApiResponse<BoolRespModel,Object> dispatchOrder(FcSaleBranchDispatchRequest fcSaleBranchDispatchRequest);
}
