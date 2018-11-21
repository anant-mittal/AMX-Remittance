package com.amx.jax.controller;

/**
 * @Author : Rabil
 * @date		: 03/11/2018
 */
import static com.amx.amxlib.constant.ApiEndpoint.FC_SALE_ENDPOINT;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IFxOrderService;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.meta.MetaData;
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
import com.amx.jax.service.TermsAndConditionService;
import com.amx.jax.services.FcSaleService;
import com.amx.jax.util.JaxContextUtil;

@RestController
@RequestMapping(FC_SALE_ENDPOINT)
@SuppressWarnings("rawtypes")
public class FcSaleOrderController implements IFxOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleService fcSaleService;

	@Autowired
	MetaData metaData;

	@Autowired
	TermsAndConditionService termsAndConditionService;

	/**
	 * @return : To get the fx purpose of trnx list
	 */
	@RequestMapping(value = Path.FC_PURPOSEOF_TRNX, method = RequestMethod.GET)
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		return fcSaleService.getPurposeofTrnxList();
	}

	/**
	 * To get the FC Sale currency List *
	 */
	@RequestMapping(value = "/fc-currency-list/", method = RequestMethod.GET)
	public AmxApiResponse fcCurrencyList() {
		BigDecimal countryId = metaData.getCountryId();
		ApiResponse response = fcSaleService.getFcSalecurrencyList(countryId);
		return AmxApiResponse.build(response);
	}

	/** To get the FC Sale currency wise exchnage rate **/
	@RequestMapping(value = "/fc-sale-xrate/", method = RequestMethod.GET)
	public AmxApiResponse fcExchangeRate(
			@RequestParam(value = "fxCurrencyId", required = true) BigDecimal fxCurrencyId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApiResponse response = fcSaleService.getFcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
		return AmxApiResponse.build(response);
	}

	/** To calculate FC Sale currency wise exchnage rate **/

	@RequestMapping(value = "/fc-sale-cal-xrate/", method = RequestMethod.GET)
	public AmxApiResponse fcExchangeRate(@RequestParam(value = "fxCurrencyId", required = true) BigDecimal fxCurrencyId,
			@RequestParam(value = "fxAmount", required = true) BigDecimal fcAmount) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		ApiResponse response = fcSaleService.getFCSaleLcAndFcAmount(applicationCountryId, countryBranchId, fxCurrencyId,
				fcAmount);
		return AmxApiResponse.build(response);
	}

	/** to display the default api **/

	@RequestMapping(value = "/fc-sale-default/", method = RequestMethod.GET)
	public AmxApiResponse fcSaleDefaultApi() {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal languageId = metaData.getLanguageId();
		ApiResponse response = fcSaleService.getDefaultFsSale(applicationCountryId, countryBranchId, languageId);
		return AmxApiResponse.build(response);
	}

	/** To save fc sale application **/

	@RequestMapping(value = "/fcsale-save-application/", method = RequestMethod.POST)
	public AmxApiResponse saveApplication(@RequestBody @Valid FcSaleOrderTransactionRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Fc Sale Save-Application with parameters" + model.toString());
		ApiResponse response = fcSaleService.saveApplication(model);
		return AmxApiResponse.build(response);
	}

	@RequestMapping(value = "/fcsale-save-paynow/", method = RequestMethod.POST)
	public AmxApiResponse fcSaleApplicationPayment(@RequestBody @Valid FcSaleOrderPaynowRequestModel requestmodel) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(requestmodel);
		logger.info("In Fc Sale Save-Application with parameters" + requestmodel.toString());
		return fcSaleService.saveApplicationPayment(requestmodel);

	}

	@RequestMapping(value = "/fc-sale-address/", method = RequestMethod.GET)
	public AmxApiResponse fetchFcSaleAddress() {
		ApiResponse response = fcSaleService.fetchFcSaleAddress();
		return AmxApiResponse.build(response);
	}

	/** Save shipping address */
	@RequestMapping(value = "/fc-save-shipping-addr/", method = RequestMethod.POST)
	public AmxApiResponse saveCustomerShippingAddress(
			@RequestBody @Valid CustomerShippingAddressRequestModel requestModel) {
		logger.info("in saveCustomerHomeAddress: {} ", requestModel);
		ApiResponse response = fcSaleService.saveShippingAddress(requestModel);
		return AmxApiResponse.build(response);
	}

	/**
	 * @ String date @ To fetch time slot for Fx order delviery
	 */
	@RequestMapping(value = "/fc-sale-time-slot/", method = RequestMethod.GET)
	public AmxApiResponse fetchTimeSlot(@RequestParam(value = "fxdate", required = true) String fxdate) {
		return fcSaleService.fetchTimeSlot(fxdate);
	}

	@RequestMapping(value = "/fc-sale-remove-item/", method = RequestMethod.POST)
	public AmxApiResponse removeItemFromCart(
			@RequestParam(value = "receiptApplId", required = true) BigDecimal receiptApplId) {
		return fcSaleService.removeitemFromCart(receiptApplId);
	}

	@RequestMapping(value = "/fc-sale-shopping-cart/", method = RequestMethod.GET)
	public AmxApiResponse shoppingCartDetails() {
		return fcSaleService.fetchShoppingCartList();
	}

	/////////// MTHODS to FIX

	@Override
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(BigDecimal fxCurrencyId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<FxExchangeRateDto, Object> calculateXRate(BigDecimal fxCurrencyId, BigDecimal fcAmount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			FcSaleOrderTransactionRequestModel requestModel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			CustomerShippingAddressRequestModel requestModel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<String, Object> getTimeSlot(String fxDate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			FcSaleOrderPaynowRequestModel requestModel) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
