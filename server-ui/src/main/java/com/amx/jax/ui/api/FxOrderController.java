
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.FcSaleOrderClient;
import com.amx.jax.client.IFxOrderService;
import com.amx.jax.client.IFxOrderService.Params;
import com.amx.jax.model.request.CustomerShippingAddressRequestModel;
import com.amx.jax.model.request.fx.FcSaleOrderTransactionRequestModel;
import com.amx.jax.model.response.CurrencyMasterDTO;
import com.amx.jax.model.response.fx.FcSaleOrderApplicationResponseModel;
import com.amx.jax.model.response.fx.FcSaleOrderDefaultResponseModel;
import com.amx.jax.model.response.fx.FxExchangeRateDto;
import com.amx.jax.model.response.fx.PurposeOfTransactionDto;
import com.amx.jax.model.response.fx.ShippingAddressDto;
import com.amx.jax.model.response.fx.ShoppingCartDetailsDto;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.response.ResponseWrapper;

import io.swagger.annotations.Api;

/**
 * The Class PlaceOrderController.
 */
@RestController
@Api(value = "FX Order Apis")
@ApiStatusService(IFxOrderService.class)
public class FxOrderController {

	@Autowired
	private FcSaleOrderClient fcSaleOrderClient;

	@RequestMapping(value = "/api/fxo/purpose/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<PurposeOfTransactionDto>> getFcPurposeofTrnx() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcPurposeofTrnx());
	}

	@RequestMapping(value = "/api/fxo/ccy/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<CurrencyMasterDTO>> getFcCurrencyList() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcCurrencyList());
	}

	@RequestMapping(value = "/api/fxo/xrate", method = { RequestMethod.GET })
	public ResponseWrapper<FxExchangeRateDto> getFcXRate(@RequestParam BigDecimal forCur) {
		return ResponseWrapper.build(fcSaleOrderClient.getFcXRate(forCur));
	}

	@RequestMapping(value = "/api/fxo/price", method = { RequestMethod.GET })
	public ResponseWrapper<FcSaleOrderApplicationResponseModel> calculateXRate(@RequestParam BigDecimal forCur,
			@RequestParam BigDecimal forAmount) {
		return ResponseWrapper.build(fcSaleOrderClient.calculateXRate(forCur, forAmount));
	}

	@RequestMapping(value = "/api/fxo/default", method = { RequestMethod.GET })
	public ResponseWrapper<FcSaleOrderDefaultResponseModel> getFcSaleDefaultApi() {
		return ResponseWrapper.build(fcSaleOrderClient.getFcSaleDefaultApi());
	}

	@RequestMapping(value = "/api/fxo/application/add", method = { RequestMethod.POST })
	public ResponseWrapper<FcSaleOrderApplicationResponseModel> saveOrder(
			@RequestBody FcSaleOrderTransactionRequestModel requestModel) {
		return ResponseWrapper.build(fcSaleOrderClient.getSaveApplication(requestModel));
	}

	@RequestMapping(value = "/api/fxo/application/remove", method = RequestMethod.POST)
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(
			@RequestParam(value = Params.RECEIPT_APPL_ID, required = true) BigDecimal applicationId) {
		return ResponseWrapper.build(fcSaleOrderClient.removeItemFromCart(applicationId));
	}

	@RequestMapping(value = "/api/fxo/application/list", method = RequestMethod.GET)
	public ResponseWrapper<List<ShoppingCartDetailsDto>> fetchShoppingCartList() {
		return ResponseWrapper.buildList(fcSaleOrderClient.fetchShoppingCartList());
	}

	@RequestMapping(value = "/api/fxo/address/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ShippingAddressDto>> getFcSaleAddress() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcSaleAddress());
	}

	@RequestMapping(value = "/api/fxo/address/add", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerShippingAddressRequestModel> saveFcSaleShippingAddress(
			@RequestParam CustomerShippingAddressRequestModel requestModel) {
		return ResponseWrapper.build(fcSaleOrderClient.saveFcSaleShippingAddress(requestModel));
	}

	@RequestMapping(value = "/api/fxo/slots/list", method = RequestMethod.GET)
	public ResponseWrapper<List<String>> getTimeSlot(String date) {
		return ResponseWrapper.buildList(fcSaleOrderClient.getTimeSlot(date));
	}

}
