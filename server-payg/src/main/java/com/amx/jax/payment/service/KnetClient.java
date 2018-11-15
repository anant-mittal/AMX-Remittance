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
import org.springframework.stereotype.Component;

import com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;
import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payg.PayGCodes;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.payg.codes.KnetCodes;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.utils.JsonUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
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
	public void initialize(PayGParams payGParams) {

		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", knetAction);
		configMap.put("currency", knetCurrency);
		configMap.put("languageCode", knetLanguageCode);
		configMap.put("responseUrl", payGConfig.getServiceCallbackUrl() + "/app/capture/KNET/" + payGParams.getTenant()
				+ "/" + payGParams.getChannel() + "/");
		configMap.put("resourcePath", knetCertpath);
		configMap.put("aliasName", knetAliasName);

		LOGGER.info("KNET payment configuration : " + JsonUtil.toJson(configMap));

		e24PaymentPipe pipe = new e24PaymentPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		String amount = (String) payGParams.getAmount();

		try {
			BigDecimal bd = new BigDecimal(amount);

			if (!(bd.signum() > 0)) {
				throw new NumberFormatException("Negative value not allowed.");
			}

			bd = bd.setScale(3, RoundingMode.HALF_UP);
			amount = bd.toPlainString();

			LOGGER.info("Amount to remit is --> " + amount);

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt(amount);
			pipe.setTrackId((String) payGParams.getTrackId());

			pipe.setUdf3(payGParams.getDocNo());

			Short pipeValue = pipe.performPaymentInitialization();
			LOGGER.info("pipeValue : " + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				LOGGER.info("KNET-ERROR" + pipe.getErrorMsg());
				LOGGER.info("KNET-DEBUg" + pipe.getDebugMsg());
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
			LOGGER.info("Generated url is ---> " + url);
			payGParams.setRedirectUrl(url);

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
	public PayGResponse capture(PayGResponse gatewayResponse, Channel channel) {

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
		gatewayResponse.setCountryId(Tenant.KWT.getCode());

		LOGGER.info("Params captured from KNET : " + JsonUtil.toJson(gatewayResponse));

		KnetCodes knetCodes = (KnetCodes) PayGCodes.getPayGCode(resultResponse, KnetCodes.UNKNOWN);

		LOGGER.info("resultResponse ---> " + resultResponse);
		gatewayResponse.setErrorCategory(knetCodes.getCategory());

		LOGGER.info("Result from response Values ---> " + gatewayResponse.getErrorCategory());
		/* gatewayResponse.setError(resultResponse); */

		PaymentResponseDto resdto = paymentService.capturePayment(gatewayResponse);
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
