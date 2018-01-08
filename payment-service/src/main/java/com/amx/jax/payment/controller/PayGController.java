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
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.model.url.PaymentResponse;
import com.amx.jax.payment.service.PaymentService;
import com.amx.jax.payment.util.PaymentUtil;
import com.amx.jax.scope.Tenant;
import com.bootloaderjs.JsonUtil;
import com.fasterxml.jackson.core.JsonProcessingException;

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

	@RequestMapping(value = { "/pay/*", "/pay" }, method = RequestMethod.GET)
	public String pay(@RequestParam(required = false) String name, @RequestParam String country,
			@RequestParam String amount, @RequestParam String trckid, @RequestParam String pg,
			@RequestParam(required = false) BigDecimal pgId, @RequestParam String docNo, @RequestParam Tenant tnt) {

		log.info("Inside handleUrlPaymentRemit with   name-" + name + ", amount-" + amount + ", country-" + country
				+ ", pg-" + pg + ", pg_id-" + pgId);

		payGClients.getPayGClient(pg, tnt);

		PayGParams payGParams = new PayGParams();

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

		// if (PGEnum.KNET.name().equalsIgnoreCase(pg)) {
		// res = paymentService.knetInitialize(paramValueMap);
		// } else if (PGEnum.OMANNET.name().equalsIgnoreCase(pg)) {
		// res = paymentService.omanNetInitialize(paramValueMap);
		// } else if (PGEnum.BAHKNET.name().equalsIgnoreCase(pg)) {
		// res = paymentService.bahKnetInitialize(paramValueMap);
		// }

		String payid = res.get("payid");
		String payurl = res.get("payurl");

		log.info("KNET is initialted for doc number : " + trckid + "  and payid is : " + payid
				+ "  and url formed is : " + payurl);
		return "redirect:" + payurl + "?paymentId=" + payid;
	}

}
