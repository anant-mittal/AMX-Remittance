/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import static com.amx.jax.payment.PaymentConstant.PAYMENT_API_ENDPOINT;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.logger.AuditService;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGEvent;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.payment.gateway.PaymentGateWayResponse;
import com.amx.jax.payment.gateway.PaymentGateWayResponse.PayGStatus;
import com.amx.jax.scope.TenantContextHolder;
import com.amx.utils.ArgUtil;

import io.swagger.annotations.Api;

/**
 * @author Viki Sangani 13-Dec-2017 PaymentController.java
 */
@Controller
@RequestMapping(PAYMENT_API_ENDPOINT)
@Api(value = "Payment APIs")
public class PayGController {

	private static final Logger LOGGER = Logger.getLogger(PayGController.class);

	@Autowired
	private PayGClients payGClients;

	@Autowired
	private PayGSession payGSession;

	@Autowired
	private AuditService auditService;

	@Value("${app.url.kwt}")
	String kwtRedirectURL;

	@Value("${app.url.bhr}")
	String bhrRedirectURL;

	@Value("${app.url.omn}")
	String omnRedirectURL;

	@Value("${app.url.omn.kiosk}")
	String kioskOmnRedirectURL;

	@Autowired
	PayGConfig payGConfig;

	@RequestMapping(value = { "/payment/*", "/payment" }, method = RequestMethod.GET)

	public String handleUrlPaymentRemit(
			@RequestParam String trckid,
			@RequestParam(required = false) String docId, @RequestParam(required = false) String docNo,
			@RequestParam(required = false) String docFy,
			@RequestParam String amount,

			@RequestParam Tenant tnt, @RequestParam String pg, @RequestParam(required = false) Channel channel,
			@RequestParam(required = false) String prod,

			@RequestParam(required = false) String callbackd,
			Model model) {

		TenantContextHolder.setCurrent(tnt);
		String uuid = payGSession.uuid(true);

		String appRedirectUrl = null;

		if (tnt.equals(Tenant.BHR)) {
			pg = "BENEFIT";
			appRedirectUrl = bhrRedirectURL;
		} else if (tnt.equals(Tenant.KWT)) {
			appRedirectUrl = kwtRedirectURL;
		} else if (tnt.equals(Tenant.OMN)) {
			pg = "OMANNET";
			appRedirectUrl = omnRedirectURL;
		}

		if (callbackd != null) {
			byte[] decodedBytes = Base64.getDecoder().decode(callbackd);
			String callback = new String(decodedBytes);
			payGSession.setCallback(callback);
		}

		LOGGER.info("call back ---->" + payGSession.getCallback());
		LOGGER.info(String.format(
				"Inside payment method with parameters --> TrackId: %s, amount: %s, docNo: %s, country: %s, pg: %s",
				trckid, amount, docNo, tnt, pg));

		PayGClient payGClient = payGClients.getPayGClient(pg);

		PayGParams payGParams = new PayGParams();
		payGParams.setAmount(amount);
		payGParams.setTrackId(trckid);
		payGParams.setDocNo(docNo);
		payGParams.setDocId(docId);
		payGParams.setDocFy(docFy);
		payGParams.setTenant(tnt);
		if (channel == null)
			channel = Channel.ONLINE;
		payGParams.setChannel(channel);
		payGParams.setProduct(prod);
		payGParams.setServiceCode(payGClient.getClientCode());
		payGParams.setUuid(uuid);

		auditService.log(new PayGEvent(PayGEvent.Type.PAYMENT_INIT, payGParams));

		PaymentGateWayResponse respModel = new PaymentGateWayResponse();

		payGSession.init();
		payGSession.get().setParams(payGParams);
		payGSession.get().setResponse(respModel);

		try {
			payGClient.initialize(payGParams, respModel);
		} catch (RuntimeException e) {
			SimpleDateFormat df = new SimpleDateFormat("HH:mm");
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, 15);
			String returnTime = df.format(cal.getTime());
			model.addAttribute("REDIRECTURL", appRedirectUrl);
			model.addAttribute("RETURN_TIME", returnTime);
			return "thymeleaf/pg_error";
		}

		payGSession.save();

		if (payGParams.getRedirectUrl() != null) {
			return "redirect:" + payGParams.getRedirectUrl();
		}

		return null;
	}

	@RequestMapping(value = { PaymentConstant.Path.PAYMENT_CAPTURE_CALLBACK_V1_WILDCARD,
			PaymentConstant.Path.PAYMENT_CAPTURE_CALLBACK_V1,
			PaymentConstant.Path.PAYMENT_CAPTURE_CALLBACK_V2,
			PaymentConstant.Path.PAYMENT_CAPTURE_CALLBACK_V2_WILDCARD
	})
	public String paymentCapture(Model model, RedirectAttributes ra,
			@PathVariable("tenant") Tenant tnt,
			@PathVariable("paygCode") PayGServiceCode paygCode,
			@PathVariable("channel") Channel channel,
			@PathVariable(value = "product", required = false) String product,
			@PathVariable(value = "uuid", required = false) String uuid) {

		TenantContextHolder.setCurrent(tnt);

		payGSession.uuid(uuid);

		LOGGER.info("Inside capture method with parameters tenant : " + tnt + " paygCode : " + paygCode);
		PayGClient payGClient = payGClients.getPayGClient(paygCode);

		PaymentGateWayResponse payGResponse = payGSession.get().getResponse();
		payGResponse.setApplicationCountryId(tnt.getBDCode());

		try {
			payGResponse = payGClient.capture(payGSession.get().getParams(), payGResponse);
		} catch (Exception e) {
			LOGGER.error("payment service error in capturePayment method : ", e);
			payGResponse.setPayGStatus(PayGStatus.ERROR);
		}

		auditService.log(new PayGEvent(PayGEvent.Type.PAYMENT_CAPTURED, payGResponse));

		String redirectUrl;

		if (payGResponse.getPayGStatus() == PayGStatus.CAPTURED) {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/success";
		} else if (payGResponse.getPayGStatus() == PayGStatus.CANCELLED) {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/cancelled";
		} else {
			redirectUrl = payGConfig.getServiceCallbackUrl() + "/callback/error";
		}

		model.addAttribute("REDIRECT", redirectUrl);

		// return "thymeleaf/repback";
		// if (paygCode.toString().equals("OMANNET")) {
		if (paygCode.toString().equals("OMANNET") && channel.equals(Channel.ONLINE)) {
			return "redirect:" + redirectUrl;
			// }else if (paygCode.toString().equals("KOMANNET")) {
		} else if (paygCode.toString().equals("OMANNET") && channel.equals(Channel.KIOSK)) {
			ra.addAttribute("paymentId", payGResponse.getPaymentId());
			ra.addAttribute("result", payGResponse.getResult());
			ra.addAttribute("auth", payGResponse.getAuth());
			ra.addAttribute("referenceId", payGResponse.getRef());
			ra.addAttribute("postDate", payGResponse.getPostDate());
			ra.addAttribute("trackId", payGResponse.getTrackId());
			ra.addAttribute("tranId", payGResponse.getTranxId());
			ra.addAttribute("udf1", payGResponse.getUdf1());
			ra.addAttribute("udf2", payGResponse.getUdf2());
			ra.addAttribute("udf3", payGResponse.getUdf3());
			ra.addAttribute("udf4", payGResponse.getUdf4());
			ra.addAttribute("udf5", payGResponse.getUdf5());
			LOGGER.info("PAYG Response is ----> " + payGResponse.toString());
			return "redirect:" + kioskOmnRedirectURL;
		} else {
			return "thymeleaf/repback";
		}
	}

}
