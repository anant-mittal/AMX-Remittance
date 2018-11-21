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
		public static final String FC_AMOUNT = "fcAmount";
		public static final String FXDATE2 = "fxdate";
		public static final String RECEIPT_APPL_ID = "receiptApplId";

	}

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.INVALID_APPLICATION_COUNTRY_ID,JaxError.INVALID_CURRENCY_ID,JaxError.INVALID_COMPANY_ID})
	AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND,JaxError.INVALID_APPLICATION_COUNTRY_ID,JaxError.INVALID_CURRENCY_ID,JaxError.INVALID_COMPANY_ID})
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(BigDecimal fxCurrencyId,BigDecimal fcAmount);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi();

	@ApiJaxStatus({ JaxError.FS_APPLIATION_CREATION_FAILED,JaxError.INVALID_APPLICATION_COUNTRY_ID,JaxError.INVALID_COMPANY_ID,JaxError.INVALID_COUNTRY_BRANCH})
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(FcSaleOrderTransactionRequestModel requestModel) throws Exception;

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND ,JaxError.INVALID_APPLICATION_COUNTRY_ID,JaxError.INVALID_COMPANY_ID})
	AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(CustomerShippingAddressRequestModel requestModel) throws Exception;

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND ,JaxError.INVALID_APPLICATION_COUNTRY_ID,JaxError.INVALID_COMPANY_ID,JaxError.FC_SALE_TIME_SLOT_SETUP_MISSING})
	AmxApiResponse<String, Object> getTimeSlot(String fxDate);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList();

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_ID })
	AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(FcSaleOrderPaynowRequestModel requestModel) throws Exception;

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_ID})
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId)throws Exception;

}
