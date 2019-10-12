package com.amx.jax.client.fx;

/**
 * @author	:Rabil
 * @Date	: 03/11/2018
 * @Purpose	: Fx Order Delivery Client
 */
import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
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
import com.amx.jax.rest.RestService;

@Component
public class FcSaleOrderClient implements IFxOrderService {

	private static final Logger LOGGER = Logger.getLogger(FcSaleOrderClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	/**
	 * 
	 * @return :to get the purpose of Trnx for FC Sale
	 */
	@Override
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		
			LOGGER.debug("in getFcPurposeofTrnx :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_PURPOSEOF_TRNX).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<PurposeOfTransactionDto, Object>>() {
					});
		

	}

	/**
	 * 
	 * @return :to get all the FC Sale currency
	 */
	@Override
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList() {
		
			LOGGER.debug("in getFcPurposeofTrnx :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_CURRENCY_LIST).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<CurrencyMasterDTO, Object>>() {
					});
		

	}

	/**
	 * 
	 * @return : fccurrency rate
	 * 
	 */
	@Override
	public AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId) {
		
			LOGGER.debug("in getFcXRate :" + fxCurrencyId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_XRATE).meta(new JaxMetaInfo())
					.queryParam(Params.FX_CURRENCY_ID, fxCurrencyId)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<FxExchangeRateDto, Object>>() {
					});
		

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(BigDecimal fxCurrencyId,
			BigDecimal fcAmount) {
		
			LOGGER.debug("in getFcXRate :" + fxCurrencyId + "\t fcAmount :" + fcAmount);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_CAL_XRATE).meta(new JaxMetaInfo())
					.queryParam(Params.FX_CURRENCY_ID, fxCurrencyId)
					.queryParam(Params.FC_AMOUNT, fcAmount).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});

	}

	/**
	 * {@inheritDoc}
	 * 
	 */
	@Override
	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi() {
		
			LOGGER.debug("getFcSaleDefaultApi client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_DEFAULT).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderDefaultResponseModel, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			FcSaleOrderTransactionRequestModel requestModel) {
		
			LOGGER.debug(" Fc Sale create application :" + requestModel.toString());
			return restService.ajax(appConfig.getJaxURL() + Path.FCSALE_SAVE_APPLICATION).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress() {
		
			LOGGER.debug("getFcSale shipping Address client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ADDRESS).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddressNew() {
		
			LOGGER.debug("getFcSale address new shipping Address client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ADDRESS_NEW).meta(new JaxMetaInfo())
					.get()
					.as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>() {
					});
		

	}
	
	/**
	 * @purpose : Save FC Sale shipping address CustomerShippingAddressRequestModel
	 **/
	@Override
	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			CustomerShippingAddressRequestModel requestModel) {
		
			LOGGER.debug(" Fc Sale shipping address save :" + requestModel.toString());
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SAVE_SHIPPING_ADDR).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerShippingAddressRequestModel, Object>>() {
					});
		
	}

	/** @ To fetch the time slot **/
	@Override
	public AmxApiResponse<TimeSlotDto, Object> getTimeSlot(BigDecimal addressId) {
		
			LOGGER.debug("in getTime slot client :" + addressId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_TIME_SLOT)
					.meta(new JaxMetaInfo()).queryParam(Params.FX_SHIPPING_ADD_ID, addressId)
					.get().as(new ParameterizedTypeReference<AmxApiResponse<TimeSlotDto, Object>>() {
					});
		
	}

	/** @ Remove cart from list by passing application id **/
	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(BigDecimal applicationId) {
		
			LOGGER.debug(" Fc Sale create application :" + applicationId);
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_REMOVE_ITEM).meta(new JaxMetaInfo())
					.queryParam(Params.RECEIPT_APPL_ID, applicationId)
					.post()
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleOrderApplicationResponseModel, Object>>() {
					});
		

	}

	/** @ To fetch the time slot **/
	@Override
	public AmxApiResponse<FxOrderShoppingCartResponseModel, Object> fetchShoppingCartList() {
		
			LOGGER.debug("in fetchShoppingCartList  client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_SHOPPING_CART).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderShoppingCartResponseModel, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			FcSaleOrderPaynowRequestModel requestModel) {
		
			LOGGER.debug(" Fc Sale create application :" + requestModel.toString());
			return restService.ajax(appConfig.getJaxURL() + Path.FCSALE_SAVE_PAYNOW).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<FcSaleApplPaymentReponseModel, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<PaymentResponseDto, Object> savePaymentId(PaymentResponseDto requestModel) {
		
			LOGGER.debug("client Save pg details  :" + requestModel.toString());
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_SAVE_PAYMENT_ID).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<PaymentResponseDto, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<FxOrderTransactionHistroyDto, Object> getFxOrderTransactionHistroy() {
		LOGGER.debug("in FxOrderTransactionHistroyDto  client :");
		return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ORDER_TRNX_HIST).meta(new JaxMetaInfo()).get()
				.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderTransactionHistroyDto, Object>>() {
				});

	}

	@Override
	public AmxApiResponse<FxOrderReportResponseDto, Object> getFxOrderTransactionReport(BigDecimal collectionDocNo,
			BigDecimal collectionFyear) {
		LOGGER.debug("in getFxOrderTransactionReport  client :");
		
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ORDER_TRNX_REPORT).meta(new JaxMetaInfo())
					.queryParam(Params.COLLECTION_DOC_NO, collectionDocNo)
					.queryParam(Params.COLLECTION_FYEAR, collectionFyear)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<FxOrderReportResponseDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<FxOrderTransactionStatusResponseDto, Object> getFxOrderTransactionStatus(
			BigDecimal documentIdForPayment) {
		
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ORDER_TRNX_STATUS).meta(new JaxMetaInfo())
					.queryParam(Params.DOCUMENT_ID_FOR_PAYMENT, documentIdForPayment).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<FxOrderTransactionStatusResponseDto, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<AddressTypeDto, Object> getAddressTypeList() {
		
			LOGGER.debug("in getAddressTypeList  client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_ORDER_ADD_TYPE).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<AddressTypeDto, Object>>() {
					});
		

	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> deleteFcSaleAddress(BigDecimal addressId) {
		
			LOGGER.debug("in getAddressTypeList  client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_SHIPPING_ADDR_DELETE).meta(new JaxMetaInfo())
					.queryParam(Params.FX_SHIPPING_ADD_ID, addressId)
					.post().as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>() {
					});
		
	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> editShippingAddress(ShippingAddressDto requestModel) {
		
			LOGGER.debug("in getAddressTypeList  client :");
			return restService.ajax(appConfig.getJaxURL() + Path.FC_SALE_SHIPPING_ADDR_EDIT).meta(new JaxMetaInfo())
					.post(requestModel)
					.as(new ParameterizedTypeReference<AmxApiResponse<ShippingAddressDto, Object>>() {
					});
		
	}

}
