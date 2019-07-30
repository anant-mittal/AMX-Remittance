package com.amx.jax.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;
import com.amx.jax.AppConfig;
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
import com.amx.utils.JsonUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@PayGSpecific(PayGServiceCode.KNET)
public class KnetClient implements PayGClient {

	private static Logger LOGGER = Logger.getLogger(KnetClient.class);

	@Value("${knet.certificate.path}")
	String knetCertpath;

	@Value("${knet.alias.name}")
	String knetAliasName;

	@Value("${knet.action}")
	String knetAction;

	@Value("${knet.currency}")
	String knetCurrency;

	@Value("${knet.language.code}")
	String knetLanguageCode;

	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpServletRequest request;

	@Autowired
	PayGConfig payGConfig;

	@Autowired
	private PaymentService paymentService;

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.KNET;
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

		e24PaymentPipe pipe = new e24PaymentPipe();
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

			LOGGER.debug("pipeValue : " + pipeValue);
			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				LOGGER.error("KNET-ERROR" + pipe.getErrorMsg());
				LOGGER.debug("KNET-DEBUg" + pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to KNET - Error Code KU-KNETINIT");
			} else {
				LOGGER.info(pipe.getDebugMsg());
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();
			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));
			String url = payURL + "?paymentId=" + payID;
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

		//KnetCodes knetCodes = (KnetCodes) PayGCodes.getPayGCode(resultResponse, KnetCodes.UNKNOWN);

		LOGGER.info("resultResponse ---> " + resultResponse);
		
		//gatewayResponse.setErrorCategory(knetCodes.getCategory().name());
		ResponseCodeKWT responseCodeEnum = ResponseCodeKWT.getResponseCodeEnumByCode(resultResponse);
		if(responseCodeEnum != null) {
			gatewayResponse.setErrorCategory(responseCodeEnum.name());
			LOGGER.info("Result from response Values IF---> " + responseCodeEnum);
		}else {
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
}
