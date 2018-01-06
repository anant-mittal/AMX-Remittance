/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import static com.amx.jax.payment.constant.PaymentConstant.PAYMENT_API_ENDPOINT;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.amx.jax.payment.constant.PGEnum;
import com.amx.jax.payment.model.url.PaymentResponse;
import com.amx.jax.payment.service.PaymentService;
import com.amx.jax.payment.util.PaymentUtil;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentController.java
 */
@Controller
@RequestMapping(PAYMENT_API_ENDPOINT)
@Api(value = "Payment APIs")
public class PaymentController {

	private Logger log = Logger.getLogger(PaymentController.class);

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = { "/payment/*", "/payment" }, method = RequestMethod.GET)
	public String handleUrlPaymentRemit(@RequestParam(required = false) String name, @RequestParam String country,
			@RequestParam String amount, @RequestParam String trckid, @RequestParam String pg,
			@RequestParam(required = false) BigDecimal pgId, @RequestParam String docNo) {

		log.info("Inside handleUrlPaymentRemit with   name-" + name + ", amount-" + amount + ", country-" + country
				+ ", pg-" + pg + ", pg_id-" + pgId);

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

		Map<String, Object> paramValueMap = new HashMap<>();
		paramValueMap.put("name", name);
		paramValueMap.put("country", country);
		paramValueMap.put("amount", amount);
		paramValueMap.put("trckid", trckid);
		paramValueMap.put("pgId", pgId);
		paramValueMap.put("docNo", docNo);

		HashMap<String, String> res = null;

		if (PGEnum.KNET.name().equalsIgnoreCase(pg)) {
			res = paymentService.knetInitialize(paramValueMap);
		} else if (PGEnum.OMANNET.name().equalsIgnoreCase(pg)) {
			res = paymentService.omanNetInitialize(paramValueMap);
		} else if (PGEnum.BAHKNET.name().equalsIgnoreCase(pg)) {
			res = paymentService.bahKnetInitialize(paramValueMap);
		}

		String payid = res.get("payid");
		String payurl = res.get("payurl");

		log.info("KNET is initialted for doc number : " + trckid + "  and payid is : " + payid
				+ "  and url formed is : " + payurl);
		return "redirect:" + payurl + "?paymentId=" + payid;
	}

	@RequestMapping(value = { "/payment_capture/*", "/payment_capture/" })
	public String paymentCapture(HttpServletRequest request, Model model,
			@RequestParam(required = false) String paymentid, @RequestParam(required = false) String result,
			@RequestParam(required = false) String auth, @RequestParam(required = false) String ref,
			@RequestParam(required = false) String postdate, @RequestParam(required = false) String trackid,
			@RequestParam(required = false) String tranid, @RequestParam(required = false) String responsecode,
			@RequestParam(required = false) String udf1, @RequestParam(required = false) String udf2,
			@RequestParam(required = false) String udf3, @RequestParam(required = false) String udf4,
			@RequestParam(required = false) String udf5) {

		Map<String, String[]> parameters = request.getParameterMap();
		log.info("In Payment capture method with params : " + PaymentUtil.getMapKeyValueAsString(parameters));
		
		 HashMap<String, String> paramMap = PaymentUtil.generateParameterMapForPaymentCapture(parameters);
		 PaymentResponse res = paymentService.capturePayment(paramMap);

		String redirectUrl = null;
		if ("CAPTURED".equalsIgnoreCase(result)) {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/success?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		} else if ("CANCELED".equalsIgnoreCase(result)) {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/cancelled?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		} else {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/error?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		}

		model.addAttribute("REDIRECT", redirectUrl);

		return "thymeleaf/repback";

	}

	@RequestMapping(value = { "/payment_capture/oman/*","/payment_capture/oman" })
	public @ResponseBody String paymentCaptureOman(HttpServletRequest request, Model model,
			@RequestParam(required = false) String paymentid, @RequestParam(required = false) String result,
			@RequestParam(required = false) String auth, @RequestParam(required = false) String ref,
			@RequestParam(required = false) String postdate, @RequestParam(required = false) String trackid,
			@RequestParam(required = false) String tranid, @RequestParam(required = false) String responsecode,
			@RequestParam(required = false) String udf1, @RequestParam(required = false) String udf2,
			@RequestParam(required = false) String udf3, @RequestParam(required = false) String udf4,
			@RequestParam(required = false) String udf5) {

		Map<String, String[]> parameters = request.getParameterMap();
		log.info("In Payment capture method with params : " + PaymentUtil.getMapKeyValueAsString(parameters));

		String redirectUrl = null;
		if ("CAPTURED".equalsIgnoreCase(result)) {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/success?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		} else if ("CANCELED".equalsIgnoreCase(result)) {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/cancelled?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		} else {
			redirectUrl = String.format("https://applications2.almullagroup.com/payg/callback/error?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5);
		}

		model.addAttribute("REDIRECT", redirectUrl);
		// HashMap<String, String> paramMap =
		// generateParameterMapForPaymentCapture(parameters);
		// PaymentResponse res = paymentService.capturePayment(paramMap);
		return "thymeleaf/repback";

	}

}
