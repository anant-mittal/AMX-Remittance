package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amx.jax.dict.PayGCodes;
import com.amx.jax.dict.PayGCodes.CodeCategory;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.ResponseCode;
import com.amx.jax.dict.ResponseCodeBHR;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payg.codes.BenefitCodes;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGContext.PayGSpecific;
import com.amx.jax.payment.gateway.PayGSession;
import com.amx.jax.payment.gateway.PaymentGateWayResponse;
import com.amx.jax.payment.gateway.PaymentGateWayResponse.PayGStatus;
import com.amx.jax.payment.gateway.PaymentService;
import com.amx.utils.JsonUtil;

import bhr.com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;

/**
 * 
 * @author lalittanwar
 * @param <T>
 *
 */
@PayGSpecific(PayGServiceCode.BENEFIT)
public class BenefitClient implements PayGClient {

	private static final Logger LOGGER = Logger.getLogger(BenefitClient.class);

	@Value("${benefit.certificate.path}")
	String benefitCertpath;

	@Value("${benefit.callback.url}")
	String benefitCallbackUrl;

	@Value("${benefit.alias.name}")
	String benefitAliasName;

	@Value("${benefit.action}")
	String benefitAction;

	@Value("${benefit.currency}")
	String benefitCurrency;

	@Value("${benefit.language.code}")
	String benefitLanguageCode;

	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpServletRequest request;

	@Autowired
	PayGConfig payGConfig;

	@Autowired
	private PaymentService paymentService;

	@Autowired
	PayGSession payGSession;
	
	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.BENEFIT;
	}

	@Override
	public void initialize(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		String responseUrl = payGConfig.getServiceCallbackUrl() +
				PaymentConstant.getCalbackUrl(params);

		/**
		 * TODO :- TO be removed *********** DEBUG
		 *****************/
		Map<String, Object> configMap = new HashMap<String, Object>();
		configMap.put("action", benefitAction);
		configMap.put("currency", benefitCurrency);
		configMap.put("languageCode", benefitLanguageCode);
		configMap.put("responseUrl", responseUrl);
		configMap.put("resourcePath", benefitCertpath);
		configMap.put("aliasName", benefitAliasName);
		LOGGER.info("Baharain BENEFIT payment configuration : " + JsonUtil.toJson(configMap));
		/************ DEBUG *****************/

		e24PaymentPipe pipe = new e24PaymentPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction(benefitAction);
			pipe.setCurrency(benefitCurrency);
			pipe.setLanguage(benefitLanguageCode);
			pipe.setResponseURL(responseUrl);
			pipe.setErrorURL(responseUrl);
			pipe.setResourcePath(benefitCertpath);
			pipe.setAlias(benefitAliasName);
			pipe.setAmt((String) params.getAmount());
			pipe.setTrackId((String) params.getTrackId());

			pipe.setUdf3(params.getDocNo());

			Short pipeValue = pipe.performPaymentInitialization();
			LOGGER.info("pipeValue : " + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				LOGGER.error(pipe.getErrorMsg());
				LOGGER.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to Benefit.");
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

			PaymentGateWayResponse paymentDto = payGSession.get().getResponse();
			paymentDto.setPaymentId(payID);

//			paymentDto.setCustomerId(new BigDecimal(payGParams.getTrackId()));
//			paymentDto.setUdf3(payGParams.getDocNo());
//			paymentDto.setTrackId(payGParams.getTrackId());
//			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, payID);
//			payGSession.save(paymentDto);

			String url = payURL + "?PaymentID=" + payID;
			LOGGER.info("Generated url is ---> " + url);
			params.setRedirectUrl(url);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public PaymentGateWayResponse capture(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		// Capturing GateWay Response
		String resultResponse = request.getParameter("Error");
		String responseCode = request.getParameter("responsecode");
		String resultCode = request.getParameter("result");
		gatewayResponse.setPaymentId(request.getParameter("paymentid"));
		gatewayResponse.setResult(request.getParameter("result"));
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
		gatewayResponse.setErrorText(request.getParameter("ErrorText"));
		gatewayResponse.setError(request.getParameter("Error"));

		LOGGER.info("Params captured from BENEFIT : " + JsonUtil.toJson(gatewayResponse));

		// to handle error scenario
		if (gatewayResponse.getUdf3() == null) {
			//ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, request.getParameter("paymentid"));
			LOGGER.info("Values ---> " + JsonUtil.toJson(params));
			gatewayResponse.setUdf3(params.getDocNo());
			gatewayResponse.setResponseCode("NOT CAPTURED");
			gatewayResponse.setResult("NOT CAPTURED");
			gatewayResponse.setTrackId(params.getTrackId());
		}

		BenefitCodes statusCode;

		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			statusCode = (BenefitCodes) PayGCodes.getPayGCode(resultCode, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory().name());
			LOGGER.info("CAPTURED ---> " + gatewayResponse.getErrorCategory());
		} else if (resultResponse == null) {
			statusCode = (BenefitCodes) PayGCodes.getPayGCode(responseCode, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory().name());
			LOGGER.info("resultResponse else if ---> " + gatewayResponse.getErrorCategory());
			gatewayResponse.setError(responseCode);
		} else {
			LOGGER.info("resultResponse ---> " + resultResponse);
			/*statusCode = (BenefitCodes) PayGCodes.getPayGCode(resultResponse, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory());*/
			ResponseCodeBHR responseCodeEnum = ResponseCodeBHR.getResponseCodeEnumByCode(gatewayResponse.getError());
			gatewayResponse.setErrorCategory(responseCodeEnum.name());
			LOGGER.info("Result from response Values ---> " + responseCodeEnum);
			gatewayResponse.setError(resultResponse);
		}
		
		LOGGER.info("Params Captured From BENEFIT : " + JsonUtil.toJson(gatewayResponse));

		paymentService.capturePayment(params, gatewayResponse);

		if ("CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CAPTURED);
			// Capturing JAX Response
		} else if ("CANCELED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CANCELLED);
		} else if ("NOT CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.NOT_CAPTURED);
		} else {
			gatewayResponse.setPayGStatus(PayGStatus.ERROR);
		}
		return gatewayResponse;
	}// end of capture

}
