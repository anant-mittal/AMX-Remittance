package com.amx.jax.payment.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amx.jax.AppConstants;
import com.amx.jax.cache.TransactionModel;
import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payg.PayGCodes;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.payg.codes.BenefitCodes;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGContext.PayGSpecific;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;

import bhr.com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;

/**
 * 
 * @author lalittanwar
 * @param <T>
 *
 */
@PayGSpecific(PayGServiceCode.BENEFIT)
public class BenefitClient extends TransactionModel<PaymentResponseDto> implements PayGClient {

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

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.BENEFIT;
	}

	@Override
	public void initialize(PayGParams payGParams) {

		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", benefitAction);
		configMap.put("currency", benefitCurrency);
		configMap.put("languageCode", benefitLanguageCode);
		configMap.put("responseUrl", payGConfig.getServiceCallbackUrl() + "/app/capture/BENEFIT/"
				+ payGParams.getTenant() + "/" + payGParams.getChannel() + "/");
		configMap.put("resourcePath", benefitCertpath);
		configMap.put("aliasName", benefitAliasName);

		LOGGER.info("Baharain BENEFIT payment configuration : " + JsonUtil.toJson(configMap));

		e24PaymentPipe pipe = new e24PaymentPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt((String) payGParams.getAmount());
			pipe.setTrackId((String) payGParams.getTrackId());

			pipe.setUdf3(payGParams.getDocNo());

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

			PaymentResponseDto paymentDto = new PaymentResponseDto();
			paymentDto.setPaymentId(payID);
			paymentDto.setCustomerId(new BigDecimal(payGParams.getTrackId()));
			paymentDto.setUdf3(payGParams.getDocNo());
			paymentDto.setTrackId(payGParams.getTrackId());

			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, payID);
			save(paymentDto);

			String url = payURL + "?PaymentID=" + payID;
			LOGGER.info("Generated url is ---> " + url);
			payGParams.setRedirectUrl(url);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	@Override
	public PayGResponse capture(PayGResponse gatewayResponse, Channel channel, Object product) {

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
		gatewayResponse.setCountryId(Tenant.BHR.getCode());
		gatewayResponse.setErrorText(request.getParameter("ErrorText"));
		gatewayResponse.setError(request.getParameter("Error"));

		LOGGER.info("Params captured from BENEFIT : " + JsonUtil.toJson(gatewayResponse));

		// to handle error scenario
		if (gatewayResponse.getUdf3() == null) {
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, request.getParameter("paymentid"));
			PaymentResponseDto paymentCacheModel = get();
			LOGGER.info("Values ---> " + paymentCacheModel.toString());
			gatewayResponse.setUdf3(paymentCacheModel.getUdf3());
			gatewayResponse.setResponseCode("NOT CAPTURED");
			gatewayResponse.setResult("NOT CAPTURED");
			gatewayResponse.setTrackId(paymentCacheModel.getTrackId());
		}

		BenefitCodes statusCode;

		if ("CAPTURED".equalsIgnoreCase(resultCode)) {
			statusCode = (BenefitCodes) PayGCodes.getPayGCode(resultCode, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory());
		} else if (resultResponse == null) {
			statusCode = (BenefitCodes) PayGCodes.getPayGCode(responseCode, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory());
			gatewayResponse.setError(responseCode);
		} else {
			LOGGER.info("resultResponse ---> " + resultResponse);
			statusCode = (BenefitCodes) PayGCodes.getPayGCode(resultResponse, BenefitCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory());
			LOGGER.info("Result from response Values ---> " + gatewayResponse.getErrorCategory());
			gatewayResponse.setError(resultResponse);
		}

		LOGGER.info("Params captured from BENEFIT : " + JsonUtil.toJson(gatewayResponse));

		PaymentResponseDto resdto = paymentService.capturePayment(gatewayResponse, channel, product);

		if ("CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CAPTURED);
			// Capturing JAX Response

			if (resdto.getCollectionFinanceYear() != null) {
				gatewayResponse.setCollectionFinYear(resdto.getCollectionFinanceYear().toString());
			}

			if (resdto.getCollectionDocumentCode() != null) {
				gatewayResponse.setCollectionDocCode(resdto.getCollectionDocumentCode().toString());
			}

			if (resdto.getCollectionDocumentNumber() != null) {
				gatewayResponse.setCollectionDocNumber(resdto.getCollectionDocumentNumber().toString());
			}

		} else if ("CANCELED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CANCELLED);
		} else if ("NOT CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.NOT_CAPTURED);
		} else {
			gatewayResponse.setPayGStatus(PayGStatus.ERROR);
		}
		return gatewayResponse;
	}// end of capture

	@Override
	public PaymentResponseDto init() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PaymentResponseDto commit() {
		// TODO Auto-generated method stub
		return null;
	}

}
