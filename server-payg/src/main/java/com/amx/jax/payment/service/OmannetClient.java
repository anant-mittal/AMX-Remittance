package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGCodes;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.ResponseCodeBHR;
import com.amx.jax.dict.ResponseCodeOMN;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payg.codes.OmanNetCodes;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGContext.PayGSpecific;
import com.amx.jax.payment.gateway.PaymentGateWayResponse;
import com.amx.jax.payment.gateway.PaymentGateWayResponse.PayGStatus;
import com.amx.jax.payment.gateway.PaymentService;
import com.amx.utils.JsonUtil;
import com.fss.plugin.iPayPipe;

@PayGSpecific(PayGServiceCode.OMANNET)
public class OmannetClient implements PayGClient {

	private static final Logger LOGGER = Logger.getLogger(OmannetClient.class);

	@Value("${omannet.certificate.path}")
	String OmemnetCertpath;

	@Value("${omannet.alias.name}")
	String OmemnetAliasName;

	@Value("${omannet.action}")
	String OmemnetAction;

	@Value("${omannet.currency}")
	String OmemnetCurrency;

	@Value("${omannet.language.code}")
	String OmemnetLanguageCode;

	@Value("${omannet.callback.url}")
	String OmemnetCallbackUrl;

	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpServletRequest request;

	@Autowired
	PayGConfig payGConfig;

	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	ResponseCodeOMN responseCodeOMN;

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.OMANNET;
	}

	@Override
	public void initialize(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		String responseUrl = payGConfig.getServiceCallbackUrl() +
				PaymentConstant.getCalbackUrl(params);

		/**
		 * TODO :- TO be removed *********** DEBUG
		 *****************/
		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", OmemnetAction);
		configMap.put("currency", OmemnetCurrency);
		configMap.put("languageCode", OmemnetLanguageCode);
		configMap.put("responseUrl", responseUrl);
		// configMap.put("responseUrl",
		// OmemnetCallbackUrl+"/app/capture/OMANNET/" + payGParams.getTenant() + "/");
		configMap.put("resourcePath", OmemnetCertpath);
		configMap.put("keystorePath", OmemnetCertpath);
		configMap.put("aliasName", OmemnetAliasName);
		LOGGER.info("Oman omannet payment configuration : " + JsonUtil.toJson(configMap));
		/************ DEBUG *****************/

		iPayPipe pipe = new iPayPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction(OmemnetAction);
			pipe.setCurrency(OmemnetCurrency);
			pipe.setLanguage(OmemnetLanguageCode);
			pipe.setResponseURL(responseUrl);
			pipe.setErrorURL(responseUrl);
			pipe.setResourcePath(OmemnetCertpath);
			pipe.setKeystorePath(OmemnetCertpath);
			pipe.setAlias(OmemnetAliasName);
			pipe.setAmt((String) params.getAmount());
			pipe.setTrackId((String) params.getTrackId());
			pipe.setUdf3(params.getDocNo());

			int pipeValue = pipe.performPaymentInitializationHTTP();
			LOGGER.info("pipeValue : " + pipeValue);

			if (pipeValue != 0) {
				responseMap.put("errorMsg", pipe.getError());
				LOGGER.error(pipe.getError());
				LOGGER.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to Oman.");
			}
			LOGGER.info("Generated web address is ---> " + pipe.getWebAddress());
			params.setRedirectUrl(pipe.getWebAddress());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public PaymentGateWayResponse capture(PayGParams params, PaymentGateWayResponse gatewayResponse) {

		// Capturing GateWay Response
		gatewayResponse.setPaymentId(request.getParameter("paymentid"));
		gatewayResponse.setAuth(request.getParameter("auth"));
		gatewayResponse.setResponseCode(request.getParameter("responseData"));
		gatewayResponse.setUdf1(request.getParameter("udf1"));
		gatewayResponse.setUdf2(request.getParameter("udf2"));
		gatewayResponse.setUdf3(request.getParameter("udf3"));
		gatewayResponse.setUdf4(request.getParameter("udf4"));
		gatewayResponse.setUdf5(request.getParameter("udf5"));

		iPayPipe pipe = new iPayPipe();
		// Initialization
		pipe.setResourcePath(OmemnetCertpath);
		pipe.setKeystorePath(OmemnetCertpath);
		pipe.setAlias(OmemnetAliasName);

		String errorText = request.getParameter("ErrorText");
		String tranresult = request.getParameter("result");
		String tranData = request.getParameter("trandata");
		LOGGER.info("tranData : " + tranData);
		int result = 0;

		if (tranData != null) {
			result = pipe.parseEncryptedRequest(request.getParameter("trandata"));
		}
		if (result != 0) {
			// Merchant to Handle the error scenario
		} else {
			if (errorText != null) {
				gatewayResponse.setErrorText(request.getParameter("ErrorText"));
			}
			if (tranresult != null) {
				gatewayResponse.setResult(request.getParameter("result"));
			}
		}

		if (tranData == null) {
			// Null response from PG. Merchant to handle the error scenario
		} else {

			String resultResponse = pipe.getResult();
			gatewayResponse.setResult(pipe.getResult());
			gatewayResponse.setPostDate(pipe.getDate());
			gatewayResponse.setRef(pipe.getRef());
			gatewayResponse.setTrackId(pipe.getTrackId());
			gatewayResponse.setTranxId(pipe.getTransId());
			gatewayResponse.setUdf3(pipe.getUdf3());
			gatewayResponse.setPaymentId(pipe.getPaymentId());
			if (resultResponse.equals("CAPTURED") || resultResponse.equals("NOT CAPTURED")) {
				gatewayResponse.setError(pipe.getError());
				gatewayResponse.setErrorText(pipe.getError_text());
				gatewayResponse.setResult(resultResponse);
			} else if (resultResponse.contains("cancelled")) {
				gatewayResponse.setResult("CANCELLED");
			} else {
				gatewayResponse.setError(pipe.getResult());
				gatewayResponse.setErrorText(pipe.getResult());
				gatewayResponse.setResult("NOT CAPTURED");
			}

			LOGGER.info("resultResponse ---> " + resultResponse);
			/*OmanNetCodes statusCode = (OmanNetCodes) PayGCodes.getPayGCode(resultResponse, OmanNetCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory().name());*/
			ResponseCodeOMN responseCodeEnum = responseCodeOMN.getResponseCodeEnumByCode(pipe.getResult());
			if(responseCodeEnum != null) {
				gatewayResponse.setErrorCategory(responseCodeEnum.name());
				LOGGER.info("Result from response Values ---> " + gatewayResponse.getErrorCategory());
			}
			gatewayResponse.setError(resultResponse);
		}

		LOGGER.info("Params captured from OMANNET : " + JsonUtil.toJson(gatewayResponse));

		if (Channel.ONLINE.equals(params.getChannel())) {
			paymentService.capturePayment(params, gatewayResponse);
		}

		if ("CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CAPTURED);
		} else if ("CANCELED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CANCELLED);
		} else {
			gatewayResponse.setPayGStatus(PayGStatus.NOT_CAPTURED);
		}
		return gatewayResponse;
	}

}
