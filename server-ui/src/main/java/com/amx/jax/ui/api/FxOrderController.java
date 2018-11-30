
package com.amx.jax.ui.api;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.fx.FcSaleOrderClient;
import com.amx.jax.client.fx.IFxOrderService;
import com.amx.jax.client.fx.IFxOrderService.Params;
import com.amx.jax.dict.AmxEnums;
import com.amx.jax.http.CommonHttpRequest.CommonMediaType;
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
import com.amx.jax.payg.PayGService;
import com.amx.jax.payg.Payment;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.File;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.utils.ArgUtil;
import com.amx.utils.HttpUtils;
import com.amx.utils.JsonUtil;

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

	@Autowired
	private PayGService payGService;

	@Autowired
	private PostManService postManService;

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
	public ResponseWrapper<FcSaleOrderApplicationResponseModel> getSaveApplication(
			@RequestBody FcSaleOrderTransactionRequestModel requestModel) {
		return ResponseWrapper.build(fcSaleOrderClient.getSaveApplication(requestModel));
	}

	@RequestMapping(value = "/api/fxo/application/remove", method = RequestMethod.POST)
	public AmxApiResponse<FcSaleOrderApplicationResponseModel, Object> removeItemFromCart(
			@RequestParam(value = Params.RECEIPT_APPL_ID, required = true) BigDecimal applicationId) {
		return ResponseWrapper.build(fcSaleOrderClient.removeItemFromCart(applicationId));
	}

	@RequestMapping(value = "/api/fxo/application/list", method = RequestMethod.GET)
	public ResponseWrapper<FxOrderShoppingCartResponseModel> fetchShoppingCartList() {
		return ResponseWrapper.build(fcSaleOrderClient.fetchShoppingCartList());
	}

	@RequestMapping(value = "/api/fxo/address/list", method = { RequestMethod.GET })
	public ResponseWrapper<List<ShippingAddressDto>> getFcSaleAddress() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFcSaleAddress());
	}

	@RequestMapping(value = "/api/fxo/address/type", method = { RequestMethod.GET })
	public ResponseWrapper<AddressTypeDto> getAddressTypeList() {
		return ResponseWrapper.build(fcSaleOrderClient.getAddressTypeList());
	}

	@RequestMapping(value = "/api/fxo/address/add", method = { RequestMethod.POST })
	public ResponseWrapper<CustomerShippingAddressRequestModel> saveFcSaleShippingAddress(
			@RequestBody CustomerShippingAddressRequestModel requestModel) {
		return ResponseWrapper.build(fcSaleOrderClient.saveFcSaleShippingAddress(requestModel));
	}

	@RequestMapping(value = "/api/fxo/address/update", method = { RequestMethod.POST })
	public ResponseWrapper<ShippingAddressDto> editShippingAddress(
			@RequestBody ShippingAddressDto requestModel) {
		return ResponseWrapper.build(fcSaleOrderClient.editShippingAddress(requestModel));
	}

	@RequestMapping(value = "/api/fxo/address/delete", method = { RequestMethod.POST })
	public ResponseWrapper<ShippingAddressDto> deleteFcSaleAddress(
			@RequestParam BigDecimal addressId) {
		return ResponseWrapper.build(fcSaleOrderClient.deleteFcSaleAddress(addressId));
	}

	@RequestMapping(value = "/api/fxo/slots/list", method = RequestMethod.GET)
	public ResponseWrapper<List<TimeSlotDto>> getTimeSlot(@RequestParam BigDecimal addressId) {
		return ResponseWrapper.buildList(fcSaleOrderClient.getTimeSlot(addressId));
	}

	@RequestMapping(value = "/api/fxo/checkout", method = RequestMethod.POST)
	public ResponseWrapper<FcSaleApplPaymentReponseModel> getSavePayNowApplication(
			@RequestBody FcSaleOrderPaynowRequestModel requestModel,
			HttpServletRequest request) throws MalformedURLException, URISyntaxException {

		ResponseWrapper<FcSaleApplPaymentReponseModel> wrapper = ResponseWrapper
				.build(fcSaleOrderClient.getSavePayNowApplication(requestModel));

		Payment payment = new Payment();
		payment.setDocFinYear(wrapper.getData().getDocumentFinancialYear());
		payment.setDocNo(wrapper.getData().getDocumentIdForPayment());
		payment.setMerchantTrackId(wrapper.getData().getMerchantTrackId());
		payment.setNetPayableAmount(wrapper.getData().getNetPayableAmount());
		payment.setPgCode(wrapper.getData().getPgCode());
		payment.setProduct(AmxEnums.Products.FXORDER);

		wrapper.redirectUrl(payGService.getPaymentUrl(payment,
				HttpUtils.getServerName(request)
						+ "/app/landing/fxorder"));
		return wrapper;
	}

	@RequestMapping(value = "/api/fxo/tranx/list", method = RequestMethod.GET)
	public ResponseWrapper<List<FxOrderTransactionHistroyDto>> getFxOrderTransactionHistroy() {
		return ResponseWrapper.buildList(fcSaleOrderClient.getFxOrderTransactionHistroy());
	}

	@RequestMapping(value = { "/api/fxo/tranx/report.{ext}", "/pub/fxo/tranx/report.{ext}" },
			method = RequestMethod.GET, produces = {
					CommonMediaType.APPLICATION_JSON_VALUE, CommonMediaType.APPLICATION_V0_JSON_VALUE,
					CommonMediaType.APPLICATION_PDF_VALUE, CommonMediaType.TEXT_HTML_VALUE })
	public ResponseEntity<byte[]> getFxOrderTransactionReport(
			@RequestParam(required = false) BigDecimal collectionDocumentCode,
			@RequestParam(required = false) BigDecimal collectionDocumentFinYear,
			@PathVariable("ext") File.Type ext,
			@RequestParam(required = false) Boolean duplicate,
			HttpServletResponse response) {

		duplicate = ArgUtil.parseAsBoolean(duplicate, false);

		AmxApiResponse<FxOrderReportResponseDto, Object> wrapper = fcSaleOrderClient.getFxOrderTransactionReport(
				collectionDocumentCode, collectionDocumentFinYear);

		if (File.Type.PDF.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT : TemplatesMX.FXO_RECEIPT,
							wrapper, File.Type.PDF))
					.getResult();
			// file.create(response, false);
			// return null;
			return ResponseEntity.ok().contentLength(file.getBody().length)
					.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());

		} else if (File.Type.HTML.equals(ext)) {
			File file = postManService.processTemplate(
					new File(duplicate ? TemplatesMX.FXO_RECEIPT : TemplatesMX.FXO_RECEIPT,
							wrapper, File.Type.HTML))
					.getResult();
			// return file.getContent();
			return ResponseEntity.ok().contentLength(file.getBody().length)
					.contentType(MediaType.valueOf(file.getType().getContentType())).body(file.getBody());
		} else {
			// return JsonUtil.toJson(wrapper);
			String json = JsonUtil.toJson(ResponseWrapper.build(wrapper));
			return ResponseEntity.ok().contentLength(json.length())
					.contentType(MediaType.valueOf(File.Type.JSON.getContentType())).body(json.getBytes());
		}

	}

	@RequestMapping(value = "/api/fxo/tranx/status", method = RequestMethod.GET)
	public ResponseWrapper<FxOrderTransactionStatusResponseDto> getFxOrderTransactionStatus(
			@RequestParam(required = false) BigDecimal documentIdForPayment) {
		return ResponseWrapper
				.build(fcSaleOrderClient.getFxOrderTransactionStatus(documentIdForPayment));
	}
}
