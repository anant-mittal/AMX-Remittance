package com.amx.jax.controller;

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

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.IFxOrderService;
import com.amx.jax.constant.JaxEvent;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderPaynowRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.fx.FcSaleApplPaymentReponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.service.TermsAndConditionService;
import com.amx.jax.services.FcSaleService;
import com.amx.jax.util.JaxContextUtil;

/**
 *
 * @author : Rabil
 * @date : 03/11/2018
 */
@RestController
public class FcSaleOrderController implements IFxOrderService {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	FcSaleService fcSaleService;

	@Autowired
	MetaData metaData;

	@Autowired
	TermsAndConditionService termsAndConditionService;

	/**
	 * To get the fx purpose of trnx list
	 * 
	 */
	@RequestMapping(value = Path.FC_PURPOSEOF_TRNX, method = RequestMethod.GET)
	public AmxApiResponse<PurposeOfTransactionDto, Object> getFcPurposeofTrnx() {
		return fcSaleService.getPurposeofTrnxList();
	}

	/**
	 * To get the FC Sale currency List
	 * 
	 * @return *
	 */
	@RequestMapping(value = Path.FC_CURRENCY_LIST, method = RequestMethod.GET)
	public AmxApiResponse<CurrencyMasterDTO, Object> getFcCurrencyList() {
		BigDecimal countryId = metaData.getCountryId();
		return fcSaleService.getFcSalecurrencyList(countryId);
	}

	/** To get the FC Sale currency wise exchnage rate **/
	@RequestMapping(value = Path.FC_SALE_XRATE, method = RequestMethod.GET)
	public AmxApiResponse<FxExchangeRateDto, Object> getFcXRate(
			@RequestParam(value = Params.FX_CURRENCY_ID, required = true) BigDecimal fxCurrencyId) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		return fcSaleService.getFcSaleExchangeRate(applicationCountryId, countryBranchId, fxCurrencyId);
	}

	/** To calculate FC Sale currency wise exchnage rate **/

	@RequestMapping(value = Path.FC_SALE_CAL_XRATE, method = RequestMethod.GET)
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> calculateXRate(
			@RequestParam(value = Params.FX_CURRENCY_ID, required = true) BigDecimal fxCurrencyId,
			@RequestParam(value = Params.FC_AMOUNT, required = true) BigDecimal fcAmount) {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		return fcSaleService.getFCSaleLcAndFcAmount(applicationCountryId, countryBranchId, fxCurrencyId, fcAmount);
	}

	/**
	 * to display the default api
	 */
	@RequestMapping(value = Path.FC_SALE_DEFAULT, method = RequestMethod.GET)
	public AmxApiResponse<FcSaleOrderDefaultResponseModel, Object> getFcSaleDefaultApi() {
		BigDecimal applicationCountryId = metaData.getCountryId();
		BigDecimal countryBranchId = metaData.getCountryBranchId();
		BigDecimal languageId = metaData.getLanguageId();
		return fcSaleService.getDefaultFsSale(applicationCountryId, countryBranchId, languageId);
	}

	/**
	 * To save fc sale application
	 */
	@RequestMapping(value = Path.FCSALE_SAVE_APPLICATION, method = RequestMethod.POST)
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> getSaveApplication(
			@RequestBody @Valid FcSaleOrderTransactionRequestModel model) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(model);
		logger.info("In Fc Sale Save-Application with parameters" + model.toString());
		return fcSaleService.saveApplication(model);
	}

	@RequestMapping(value = Path.FCSALE_SAVE_PAYNOW, method = RequestMethod.POST)
	public AmxApiResponse<FcSaleApplPaymentReponseModel, Object> getSavePayNowApplication(
			@RequestBody @Valid FcSaleOrderPaynowRequestModel requestmodel) {
		JaxContextUtil.setJaxEvent(JaxEvent.CREATE_APPLICATION);
		JaxContextUtil.setRequestModel(requestmodel);
		logger.info("In Fc Sale Save-Application with parameters" + requestmodel.toString());
		return fcSaleService.saveApplicationPayment(requestmodel);

	}

	@RequestMapping(value = Path.FC_SALE_ADDRESS, method = RequestMethod.GET)
	public AmxApiResponse<ShippingAddressDto, Object> getFcSaleAddress() {
		return fcSaleService.fetchFcSaleAddress();
	}

	/**
	 * Save shipping address
	 */
	@RequestMapping(value = Path.FC_SAVE_SHIPPING_ADDR, method = RequestMethod.POST)
	public AmxApiResponse<CustomerShippingAddressRequestModel, Object> saveFcSaleShippingAddress(
			@RequestBody @Valid CustomerShippingAddressRequestModel requestModel) {
		logger.info("in saveCustomerHomeAddress: {} ", requestModel);
		return fcSaleService.saveShippingAddress(requestModel);
	}

	/**
	 * @ String date @ To fetch time slot for Fx order delviery
	 */
	@RequestMapping(value = Path.FC_SALE_TIME_SLOT, method = RequestMethod.GET)
	public AmxApiResponse<String, Object> getTimeSlot(
			@RequestParam(value = Params.FXDATE2, required = true) String fxdate) {
		return fcSaleService.fetchTimeSlot(fxdate);
	}

	@RequestMapping(value = Path.FC_SALE_REMOVE_ITEM, method = RequestMethod.POST)
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(
			@RequestParam(value = Params.RECEIPT_APPL_ID, required = true) BigDecimal receiptApplId) {
		return fcSaleService.removeitemFromCart(receiptApplId);
	}

	@RequestMapping(value = Path.FC_SALE_SHOPPING_CART, method = RequestMethod.GET)
	public AmxApiResponse<ShoppingCartDetailsDto, Object> fetchShoppingCartList() {
		return fcSaleService.fetchShoppingCartList();
	}

}
