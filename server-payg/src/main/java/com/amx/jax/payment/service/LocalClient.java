package com.amx.jax.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.ResponseCodeKWT;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGContext.PayGSpecific;
import com.amx.jax.payment.gateway.PaymentGateWayResponse;
import com.amx.jax.payment.gateway.PaymentGateWayResponse.PayGStatus;
import com.amx.jax.payment.gateway.PaymentService;
import com.amx.utils.CryptoUtil;
import com.amx.utils.JsonUtil;
import com.amx.utils.Random;

/**
 * 
 * @author lalittanwar
 *
 */
@PayGSpecific(PayGServiceCode.LOCAL)
public class LocalClient implements PayGClient {

	private static Logger LOGGER = Logger.getLogger(LocalClient.class);

	public static final String LOCAL_PAYG = "/local/payg";

	@Value("${knet.certificate.path}")
	private String knetCertpath;

	@Value("${knet.alias.name}")
	private String knetAliasName;

	@Value("${knet.action}")
	private String knetAction;

	@Value("${knet.currency}")
	private String knetCurrency;

	@Value("${knet.language.code}")
	private String knetLanguageCode;

	@Autowired
	private HttpServletRequest request;

	@Autowired
	private PayGConfig payGConfig;

	@Autowired
	private PaymentService paymentService;

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.LOCAL;
	}

	@Override
	public void initialize(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		/**
		 * TODO :- TO be removed *********** DEBUG
		 *****************/
		Map<String, Object> configMap = new HashMap<String, Object>();
		configMap.put("action", knetAction);
		configMap.put("currency", knetCurrency);
		configMap.put("languageCode", knetLanguageCode);
		configMap.put("responseUrl", payGConfig.getServiceCallbackUrl() + PaymentConstant.getCalbackUrl(params));
		configMap.put("resourcePath", knetCertpath);
		configMap.put("aliasName", knetAliasName);
		LOGGER.info("KNET payment configuration : " + JsonUtil.toJson(configMap));
		/************ DEBUG *****************/

		LocalPipe pipe = new LocalPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		String amount = (String) params.getAmount();

		try {
			BigDecimal bd = new BigDecimal(amount);
			if (!(bd.signum() > 0)) {
				throw new NumberFormatException("Negative value not allowed.");
			}

			bd = bd.setScale(3, RoundingMode.HALF_UP);
			amount = bd.toPlainString();

			LOGGER.debug("Amount to remit is --> " + amount);

			pipe.setAction((String) knetAction);
			pipe.setCurrency((String) knetCurrency);
			pipe.setLanguage((String) knetLanguageCode);

			/**
			 * KNET expects us to specify response URL where KNET can send its response, and
			 * we are supposed to capture payment status and process accordingly and
			 * redirect user to client application (server-ui,kiosk,branch-ui) with
			 * transaction identifier
			 */
			String responseUrl = payGConfig.getServiceCallbackUrl() + PaymentConstant.getCalbackUrl(params);
			pipe.setResponseURL(responseUrl);
			pipe.setErrorURL(responseUrl);
			pipe.setResourcePath(knetCertpath);
			pipe.setAlias(knetAliasName);
			pipe.setAmt(amount);
			pipe.setTrackId(params.getTrackId());
			pipe.setUdf3(params.getDocNo());
			

			Short pipeValue = pipe.performPaymentInitialization();

			// get results
			String payID = pipe.getPaymentId();
			pipe.setPaymentId(Random.randomNumeric(12));
			String payURL = "/local/payg?piped="
					+ new CryptoUtil.Encoder().obzect(pipe).encrypt().encodeBase64().toString()
					+ "&paramsd="
						+ new CryptoUtil.Encoder().obzect(params).encrypt().encodeBase64().toString()
					;
			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", payURL);
			String url = payURL + "&paymentId=" + payID;
			LOGGER.debug("Generated url is ---> " + url);
			params.setRedirectUrl(url);

		} catch (NumberFormatException e) {
			LOGGER.error(String.format("Amount entered --> %s ,is not correct number.", amount), e);
		} catch (RuntimeException e) {
			LOGGER.error("Problem while sending transaction to KNET", e);
			throw new RuntimeException(e);
		} catch (Exception e) {
			LOGGER.error("Error while sending request to KNET.", e);
		}
	}

	@Override
	public PaymentGateWayResponse capture(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		// Capturing GateWay Response
		String resultResponse = request.getParameter("result");
		gatewayResponse.setPaymentId(request.getParameter("paymentid"));
		gatewayResponse.setResult(resultResponse);
		gatewayResponse.setAuth(request.getParameter("auth"));
		gatewayResponse.setRef(request.getParameter("ref"));
		gatewayResponse.setPostDate(request.getParameter("postdate"));
		gatewayResponse.setTrackId(request.getParameter("trackid"));
		gatewayResponse.setTranxId(request.getParameter("tranid"));
		gatewayResponse.setResponseCode(request.getParameter("responsecode"));
		gatewayResponse.setUdf1(request.getParameter("udf1"));
		gatewayResponse.setUdf2(request.getParameter("udf2"));
		gatewayResponse.setUdf3(request.getParameter("udf3"));
		gatewayResponse.setUdf4(request.getParameter("udf4"));
		gatewayResponse.setUdf5(request.getParameter("udf5"));

		LOGGER.info("Params captured from KNET : " + JsonUtil.toJson(gatewayResponse));

		// KnetCodes knetCodes = (KnetCodes) PayGCodes.getPayGCode(resultResponse,
		// KnetCodes.UNKNOWN);

		LOGGER.info("resultResponse ---> " + resultResponse);

		// gatewayResponse.setErrorCategory(knetCodes.getCategory().name());
		ResponseCodeKWT responseCodeEnum = ResponseCodeKWT.getResponseCodeEnumByCode(resultResponse);
		if (responseCodeEnum != null) {
			gatewayResponse.setErrorCategory(responseCodeEnum.name());
			LOGGER.info("Result from response Values IF---> " + responseCodeEnum);
		} else {
			gatewayResponse.setErrorCategory(ResponseCodeKWT.UNKNOWN.name());
			LOGGER.info("Result from response Values ELSE---> " + responseCodeEnum);
		}

		LOGGER.info("Result from response Values ---> " + gatewayResponse.getErrorCategory());
		gatewayResponse.setError(resultResponse);

		paymentService.capturePayment(params, gatewayResponse);
		// Capturing JAX Response
		if ("CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CAPTURED);
		} else if ("CANCELED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CANCELLED);
		} else {
			gatewayResponse.setPayGStatus(PayGStatus.ERROR);
		}
		return gatewayResponse;
	}

	public static class LocalPipe {

		private String knetAction;
		private String knetCurrency;
		private String knetLanguageCode;
		private String responseUrl;
		private String errorURL;
		private String knetCertpath;
		private String knetAliasName;
		private String amount;
		private String trackId;
		private String docNo;
		private String paymentId;
		private String udf1;
		private String udf2;
		private String udf3;
		private String udf4;
		private String udf5;
		private String udf6;

		public void setAction(String knetAction) {
			this.knetAction = knetAction;
		}

		public String getPaymentId() {
			return this.paymentId;
		}

		public Short performPaymentInitialization() {
			return 1;
		}

		public void setCurrency(String knetCurrency) {
			this.knetCurrency = knetCurrency;
		}

		public void setLanguage(String knetLanguageCode) {
			this.knetLanguageCode = knetLanguageCode;
		}

		public void setResponseURL(String responseUrl) {
			this.responseUrl = responseUrl;
		}

		public void setErrorURL(String errorURL) {
			this.errorURL = errorURL;
		}

		public void setResourcePath(String knetCertpath) {
			this.knetCertpath = knetCertpath;
		}

		public void setAlias(String knetAliasName) {
			this.knetAliasName = knetAliasName;
		}

		public void setAmt(String amount) {
			this.amount = amount;
		}

		public void setTrackId(String trackId) {
			this.trackId = trackId;
		}

		public void setUdf3(String udf3) {
			this.udf3 = udf3;
		}

		public String getKnetAction() {
			return knetAction;
		}

		public void setKnetAction(String knetAction) {
			this.knetAction = knetAction;
		}

		public String getKnetCurrency() {
			return knetCurrency;
		}

		public void setKnetCurrency(String knetCurrency) {
			this.knetCurrency = knetCurrency;
		}

		public String getKnetLanguageCode() {
			return knetLanguageCode;
		}

		public void setKnetLanguageCode(String knetLanguageCode) {
			this.knetLanguageCode = knetLanguageCode;
		}

		public String getResponseUrl() {
			return responseUrl;
		}

		public void setResponseUrl(String responseUrl) {
			this.responseUrl = responseUrl;
		}

		public String getKnetCertpath() {
			return knetCertpath;
		}

		public void setKnetCertpath(String knetCertpath) {
			this.knetCertpath = knetCertpath;
		}

		public String getKnetAliasName() {
			return knetAliasName;
		}

		public void setKnetAliasName(String knetAliasName) {
			this.knetAliasName = knetAliasName;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getDocNo() {
			return docNo;
		}

		public void setDocNo(String docNo) {
			this.docNo = docNo;
		}

		public String getErrorURL() {
			return errorURL;
		}

		public String getTrackId() {
			return trackId;
		}

		public void setPaymentId(String paymentId) {
			this.paymentId = paymentId;
		}

		public String getUdf1() {
			return udf1;
		}

		public void setUdf1(String udf1) {
			this.udf1 = udf1;
		}

		public String getUdf2() {
			return udf2;
		}

		public void setUdf2(String udf2) {
			this.udf2 = udf2;
		}

		public String getUdf4() {
			return udf4;
		}

		public void setUdf4(String udf4) {
			this.udf4 = udf4;
		}

		public String getUdf5() {
			return udf5;
		}

		public void setUdf5(String udf5) {
			this.udf5 = udf5;
		}

		public String getUdf6() {
			return udf6;
		}

		public void setUdf6(String udf6) {
			this.udf6 = udf6;
		}

		public String getUdf3() {
			return udf3;
		}

	}
}
