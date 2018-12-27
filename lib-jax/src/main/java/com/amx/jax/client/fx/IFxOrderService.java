package com.amx.jax.client.fx;

import java.math.BigDecimal;

import com.amx.jax.IJaxService;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.error.ApiJaxStatusBuilder.ApiJaxStatus;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.fx.AddressTypeDto;
import com.amx.jax.model.response.fx.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateDto;
import com.amx.jax.model.response.fx.FxOrderReportResponseDto;
import com.amx.jax.model.response.fx.FxOrderShoppingCartResponseModel;
import com.amx.jax.model.response.fx.FxOrderTransactionHistroyDto;
import com.amx.jax.model.response.fx.FxOrderTransactionStatusResponseDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.model.response.fx.TimeSlotDto;
import com.amx.jax.payg.PaymentResponseDto;

public interface IFxOrderService extends IJaxService {

	public static class Path {
		public static final String PREFIX = "/fc/sale";
		public static final String FC_PURPOSEOF_TRNX = PREFIX + "/fc-purposeof-trnx/";
		public static final String FCSALE_SAVE_PAYNOW = PREFIX + "/fcsale-save-paynow/";
		public static final String FC_SALE_SHOPPING_CART = PREFIX + "/fc-sale-shopping-cart/";
		public static final String FC_SALE_REMOVE_ITEM = PREFIX + "/fc-sale-remove-item/";
		public static final String FC_SALE_TIME_SLOT = PREFIX + "/fc-sale-time-slot/";
		public static final String FC_SAVE_SHIPPING_ADDR = "/fc-save-shipping-addr/";
		public static final String FC_SALE_ADDRESS = PREFIX + "/fc-sale-address/";
		public static final String FC_SALE_ADDRESS_NEW = PREFIX + "/fc-sale-address-new/";
		public static final String FCSALE_SAVE_APPLICATION = PREFIX + "/fcsale-save-application/";
		public static final String FC_SALE_DEFAULT = PREFIX + "/fc-sale-default/";
		public static final String FC_SALE_CAL_XRATE = PREFIX + "/fc-sale-cal-xrate/";
		public static final String FC_SALE_XRATE = PREFIX + "/fc-sale-xrate/";
		public static final String FC_CURRENCY_LIST = PREFIX + "/fc-currency-list/";
		public static final String FC_SALE_SAVE_PAYMENT_ID = PREFIX + "/fc_sale_save-payment-id/";
		public static final String FC_SALE_ORDER_TRNX_HIST = PREFIX + "/fc-sale-order-trnx-hist/";
		public static final String FC_SALE_ORDER_TRNX_REPORT = PREFIX + "/fc-sale-order-trnx-report/";
		public static final String FC_SALE_ORDER_TRNX_STATUS = PREFIX + "/fc-sale-order-trnx-status/";
		public static final String FC_SALE_ORDER_ADD_TYPE = PREFIX + "/fc-sale-order-address-type/";
		public static final String FC_SALE_SHIPPING_ADDR_DELETE = PREFIX + "/fc-sale-shipp-address-delete/";
		public static final String FC_SALE_SHIPPING_ADDR_EDIT = PREFIX + "/fc-sale-shipp-address-edit/";
	}

	public static class Params {

		public static final String TERMINAL_ID = "countryBranchSystemInventoryId";
		public static final String EMPLOYEE_ID = "employeeId";
		public static final String FX_CURRENCY_ID = "fxCurrencyId";
		public static final String FC_AMOUNT = "fcAmount";
		public static final String FXDATE2 = "fxdate";
		public static final String RECEIPT_APPL_ID = "receiptApplId";
		public static final String COLLECTION_DOC_NO = "collectionDocNo";
		public static final String COLLECTION_FYEAR = "collectionFyear";
		public static final String DOCUMENT_ID_FOR_PAYMENT = "documentIdForPayment";
		public static final String FX_SHIPPING_ADD_ID = "addressId";

	}

	/**
	 * @return
	 */
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList();

	/**
	 * fccurrency rate
	 * 
	 * @param fxCurrencyId
	 * @return
	 */
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_CURRENCY_ID,
			JaxError.INVALID_COMPANY_ID })
	AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId);

	/**
	 * 
	 * @param fxCurrencyId
	 * @param fcAmount
	 * @return
	 */
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_CURRENCY_ID,
			JaxError.INVALID_COMPANY_ID })
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(BigDecimal fxCurrencyId,
			BigDecimal fcAmount);

	/**
	 * 
	 * @return
	 */
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi();

	@ApiJaxStatus({ JaxError.FS_APPLIATION_CREATION_FAILED, JaxError.INVALID_APPLICATION_COUNTRY_ID,
			JaxError.INVALID_COMPANY_ID, JaxError.INVALID_COUNTRY_BRANCH,JaxError.FC_SALE_DAY_LIMIT_SETUP_NOT_DIFINED,
			JaxError.FC_SALE_TRANSACTION_MAX_ALLOWED_LIMIT_EXCEED,JaxError.CUSTOMER_NOT_REGISTERED_ONLINE})
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			FcSaleOrderTransactionRequestModel requestModel);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress();
	
	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddressNew();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_COMPANY_ID,JaxError.INVALID_STATE,JaxError.INVALID_DISTRICT,JaxError.CITY_NOT_AVAILABLE,JaxError.INVALID_ADDRESS_TYPE})
	AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			CustomerShippingAddressRequestModel requestModel);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.INVALID_APPLICATION_COUNTRY_ID, JaxError.INVALID_COMPANY_ID,
			JaxError.FC_SALE_TIME_SLOT_SETUP_MISSING })
	AmxApiResponse<TimeSlotDto, Object> getTimeSlot(BigDecimal addressId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FxOrderShoppingCartResponseModel, Object> fetchShoppingCartList();

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_ID })
	AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			FcSaleOrderPaynowRequestModel requestModel);

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_ID })
	AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId);

	@ApiJaxStatus({ JaxError.NULL_APPLICATION_ID })
	AmxApiResponse<PaymentResponseDto, Object> savePaymentId(PaymentResponseDto paymentRequestDto);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FxOrderTransactionHistroyDto, Object> getFxOrderTransactionHistroy();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FxOrderReportResponseDto, Object> getFxOrderTransactionReport(BigDecimal collectionDocNo,
			BigDecimal collectionFyear);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<FxOrderTransactionStatusResponseDto, Object> getFxOrderTransactionStatus(
			BigDecimal documentIdForPayment);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND, JaxError.ADDRESS_TYPE_SETUP_IS_MISSING })
	AmxApiResponse<AddressTypeDto, Object> getAddressTypeList();

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShippingAddressDto, Object> deleteFcSaleAddress(BigDecimal addressId);

	@ApiJaxStatus({ JaxError.NO_RECORD_FOUND })
	AmxApiResponse<ShippingAddressDto, Object> editShippingAddress(ShippingAddressDto shippingAddressDto);


}
