/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import static com.amx.jax.payment.PaymentConstant.PAYMENT_API_ENDPOINT;

import java.util.Base64;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.scope.Tenant;
import com.amx.jax.scope.TenantContextHolder;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentController.java
 */
@Controller
@RequestMapping(PAYMENT_API_ENDPOINT)
@Api(value = "Payment APIs")
public class PayGController {

	private static final Logger LOGGER = Logger.getLogger(PayGController.class);

	private static String URL_PARAMS = "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s&doccode=%s&docno=%s&finyear=%s";

	@Autowired
	private PayGClients payGClients;

	@Autowired
	private PayGSession payGSession;

	@Value("${app.url}")
	String redirectURL;

	@Autowired
	PayGConfig payGConfig;

	@RequestMapping(value = { "/payment/*", "/payment" }, method = RequestMethod.GET)
	public String handleUrlPaymentRemit(@RequestParam Tenant tnt, @RequestParam String pg, @RequestParam String amount,
			@RequestParam String trckid, @RequestParam String docNo, @RequestParam String docFy,
			@RequestParam(required = false) String callbackd, Model model) {

		TenantContextHolder.setCurrent(tnt);

		if (callbackd != null) {
			byte[] decodedBytes = Base64.getDecoder().decode(callbackd);
			String callback = new String(decodedBytes);
			payGSession.setCallback(callback);
		}

		LOGGER.info(String.format(
				"Inside payment method with parameters --> TrackId: %s, amount: %s, docNo: %s, country: %s, pg: %s",
				trckid, amount, docNo, tnt, pg));

		PayGClient payGClient = payGClients.getPayGClient(pg);

		PayGParams payGParams = new PayGParams();
		payGParams.setAmount(amount);
		payGParams.setTrackId(trckid);
		payGParams.setDocNo(docNo);
		payGParams.setTenant(tnt);

		try {
			payGClient.initialize(payGParams);
		} catch (RuntimeException e) {
			model.addAttribute("REDIRECTURL", redirectURL);
			return "thymeleaf/pg_error";
		}

		payGSession.setPayGParams(payGParams);

		if (payGParams.getRedirectUrl() != null) {
			return "redirect:" + payGParams.getRedirectUrl();
		}
		return null;
	}

	@RequestMapping(value = { "/capture/{paygCode}/{tenant}/*", "/capture/{paygCode}/{tenant}/" })
	public String paymentCapture(Model model, @PathVariable("tenant") Tenant tnt,
			@PathVariable("paygCode") PayGServiceCode paygCode) {
		TenantContextHolder.setCurrent(tnt);
		LOGGER.info("Inside capture method with parameters tenant : " + tnt + " paygCode : " + paygCode);
		PayGClient payGClient = payGClients.getPayGClient(paygCode);

		PayGResponse payGResponse = payGClient.capture(new PayGResponse());

		String redirectUrl;

		String urlParams = String.format(URL_PARAMS, payGResponse.getPaymentiId(), payGResponse.getResult(),
				payGResponse.getAuth(), payGResponse.getRef(), payGResponse.getPostDate(), payGResponse.getTrackId(),
				payGResponse.getTranxId(), payGResponse.getUdf1(), payGResponse.getUdf2(), payGResponse.getUdf3(),
				payGResponse.getUdf4(), payGResponse.getUdf5(), payGResponse.getCollectionDocCode(),
				payGResponse.getCollectionDocNumber(), payGResponse.getCollectionFinYear());

		if (payGResponse.getPayGStatus() == PayGStatus.CAPTURED) {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/success?" + urlParams;
		} else if (payGResponse.getPayGStatus() == PayGStatus.CANCELLED) {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/cancelled?" + urlParams;
		} else {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/error?" + urlParams;
		}

		model.addAttribute("REDIRECT", redirectUrl);

		return "thymeleaf/repback";
	}

}
