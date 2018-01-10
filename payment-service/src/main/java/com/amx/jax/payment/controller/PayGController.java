/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import static com.amx.jax.payment.constant.PaymentConstant.PAYMENT_API_ENDPOINT;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.scope.Tenant;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentController.java
 */
@Controller
@RequestMapping(PAYMENT_API_ENDPOINT)
@Api(value = "Payment APIs")
public class PayGController {

	private Logger log = Logger.getLogger(PayGController.class);

	@Autowired
	private PayGClients payGClients;

	@Autowired
	private PayGSession payGSession;

	@RequestMapping(value = { "/payment/*", "/payment" }, method = RequestMethod.GET)
	public String handleUrlPaymentRemit(@RequestParam(required = false) String name, @RequestParam String country,
			@RequestParam String amount, @RequestParam String trckid, @RequestParam String pg,
			@RequestParam(required = false) BigDecimal pgId, @RequestParam String docNo, @RequestParam String docFy,
			@RequestParam String callbackd, @RequestParam Tenant tnt) {

		// public String pay(@RequestParam(required = false) String name,
		// @RequestParam String amount,
		// @RequestParam String trckid,
		// @RequestParam String pg,
		// @RequestParam String docNo,
		// @RequestParam Tenant tnt) {

		byte[] decodedBytes = Base64.getDecoder().decode(callbackd);
		String callback = new String(decodedBytes);
		payGSession.setCallback(callback);

		log.info("Inside pay method with   name-" + name + ", amount-" + amount + ", country-" + tnt.getCode() + ", pg-"
				+ pg);

		PayGClient payGClient = payGClients.getPayGClient(pg, tnt);

		if (amount != null && !(amount.isEmpty())) {
			try {
				BigDecimal bd = new BigDecimal(amount);
				bd = bd.setScale(3, RoundingMode.HALF_UP);
				amount = bd.toPlainString();
			} catch (Exception e) {
				log.error("Amount is not null or empty.");
				e.getMessage();
			}

		} else if (trckid != null && trckid.isEmpty()) {
			log.error("Track Id is empty.");
		}

		PayGParams payGParams = new PayGParams();
		payGParams.setName(name);
		payGParams.setAmount(amount);
		payGParams.setTrackId(trckid);
		payGParams.setDocNo(docNo);

		payGClient.initialize(payGParams);

		payGSession.setPayGParams(payGParams);

		if (payGParams.getRedirectUrl() != null) {
			return "redirect:" + payGParams.getRedirectUrl();
		}
		return null;
	}

	@RequestMapping(value = { "/capture/{paygCode}/{tenant}/*", "/capture/{paygCode}/{tenant}/" })
	public String paymentCapture(HttpServletRequest request, Model model, @PathVariable("tenant") Tenant tnt,
			@PathVariable("paygCode") PayGServiceCode paygCode) {
		PayGClient payGClient = payGClients.getPayGClient(paygCode, tnt);
		return payGClient.capture(model);
	}

}
