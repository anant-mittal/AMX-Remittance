package com.amx.jax.client;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IFxOrderService.Path;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.FxExchangeRateDto;
import com.amx.jax.model.response.PurposeOfTransactionDto;
import com.amx.jax.model.response.ShippingAddressDto;
import com.amx.jax.model.response.ShoppingCartDetailsDto;

public interface IFxOrderService extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale/";
		public static final String FC_PURPOSEOF_TRNX = PREFIX + "/fc-purposeof-trnx/";
		public static final String FCSALE_SAVE_PAYNOW = "/fcsale-save-paynow/";
		public static final String FC_SALE_SHOPPING_CART = "/fc-sale-shopping-cart/";
		public static final String FC_SALE_REMOVE_ITEM = "/fc-sale-remove-item/";
		public static final String FC_SALE_TIME_SLOT = "/fc-sale-time-slot/";
		public static final String FC_SAVE_SHIPPING_ADDR = "/fc-save-shipping-addr/";
		public static final String FC_SALE_ADDRESS = "/fc-sale-address/";
		public static final String FCSALE_SAVE_APPLICATION = "/fcsale-save-application/";
		public static final String FC_SALE_DEFAULT = "/fc-sale-default/";
		public static final String FC_SALE_CAL_XRATE = "/fc-sale-cal-xrate/";
		public static final String FC_SALE_XRATE = "/fc-sale-xrate/";
		public static final String FC_CURRENCY_LIST = "/fc-currency-list/";

	}

	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String FX_CURRENCY_ID = "fxCurrencyId";

	}

	@ApiJaxStatus({ JaxError.CLIENT_ALREADY_REGISTERED })
	AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx();

	AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList();

	AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId);

	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(BigDecimal fxCurrencyId, BigDecimal fcAmount);

	AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi();

	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			FcSaleOrderTransactionRequestModel requestModel) throws Exception;

	AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress();

	AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			CustomerShippingAddressRequestModel requestModel) throws Exception;

	AmxApiResponse<String, Object> getTimeSlot(String fxDate);

	AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList();

	AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			FcSaleOrderPaynowRequestModel requestModel) throws Exception;

	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId)
			throws Exception;

}
