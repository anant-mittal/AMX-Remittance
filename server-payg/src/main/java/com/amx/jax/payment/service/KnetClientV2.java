package com.amx.jax.payment.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.amx.jax.dict.PayGCodes;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.ResponseCodeKWT;
import com.amx.jax.payg.PayGParams;
import com.amx.jax.payg.codes.KnetCodes;
import com.amx.jax.payment.PaymentConstant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGContext.PayGSpecific;
import com.amx.jax.payment.gateway.PaymentGateWayResponse.PayGStatus;
import com.amx.utils.JsonUtil;
import com.amx.jax.payment.gateway.PaymentGateWayResponse;
import com.amx.jax.payment.gateway.PaymentService;
import kwt.com.fss.plugin.iPayPipe;

@PayGSpecific(PayGServiceCode.KNET2)
public class KnetClientV2 implements PayGClient, InitializingBean {

	private static Logger LOGGER = Logger.getLogger(KnetClient.class);

	@Value("${knetv2.certificate.path}")
	String knetCertpath;

	@Value("${knetv2.callback.url}")
	String knetCallbackUrl;

	@Value("${knetv2.alias.name}")
	String knetAliasName;

	@Value("${knetv2.action}")
	String knetAction;

	@Value("${knetv2.currency}")
	String knetCurrency;

	@Value("${knetv2.language.code}")
	String knetLanguageCode;

	String udf1 = "Udf1";
	String udf2 = "Udf2";
	String udf4 = "Udf4";
	String udf5 = "Udf5";

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
		return PayGServiceCode.KNET2;
	}

	@Override
	public void initialize(PayGParams params, PaymentGateWayResponse gatewayResponse) {
		
		String responseUrl = payGConfig.getServiceCallbackUrl() + PaymentConstant.getCalbackUrl(params);

		LOGGER.info("------ KNET VERSION2 Initialize ------");
		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", knetAction);
		configMap.put("currency", knetCurrency);
		configMap.put("languageCode", knetLanguageCode);
		configMap.put("responseUrl", responseUrl);
		configMap.put("resourcePath", knetCertpath);
		configMap.put("keystorePath", knetCertpath);
		configMap.put("aliasName", knetAliasName);
		LOGGER.info("KNET version2 payment configuration : " + JsonUtil.toJson(configMap));

		iPayPipe pipe = new iPayPipe();
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

			pipe.setResourcePath(knetCertpath);
			pipe.setKeystorePath(knetCertpath);
			pipe.setAlias(knetAliasName);
			pipe.setAction(knetAction);
			pipe.setCurrency(knetCurrency);
			pipe.setLanguage(knetLanguageCode);
			pipe.setResponseURL(responseUrl);
			pipe.setErrorURL(responseUrl);
			pipe.setAmt(amount);
			pipe.setTrackId(params.getTrackId());
			pipe.setUdf3(params.getDocNo());
			pipe.setUdf1(udf1);
			pipe.setUdf2(udf2);
			pipe.setUdf4(udf4);

			// Set Payment Secrete
			String hashedSeceret = PaymentConstant.getHashedSecrete(
					getPaymentParamStr(params.getDocNo(), params.getTrackId()), PaymentConstant.KWT_SECRETE_SALT);

			pipe.setUdf5(hashedSeceret);

			int pipeValue = pipe.performPaymentInitializationHTTP();
			LOGGER.info("pipeValue : " + pipeValue);

			if (pipeValue != 0) {
				responseMap.put("errorMsg", pipe.getError());
				LOGGER.error(pipe.getError());
				LOGGER.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to KNET V2 ");
			}
			LOGGER.info("Generated web address is ---> " + pipe.getWebAddress());

			String payID = pipe.getPaymentId();
			String payURL = pipe.getWebAddress();
			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));
			// String url = payURL + "?paymentId=" + payID;
			String url = payURL;
			LOGGER.info("Generated url is ---> " + url);

			// params.setRedirectUrl(pipe.getWebAddress());
			params.setRedirectUrl(url);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
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

		iPayPipe pipe = new iPayPipe();

		// Initialization
		pipe.setResourcePath(knetCertpath);
		pipe.setKeystorePath(knetCertpath);
		pipe.setAlias(knetAliasName);

		String errorText = request.getParameter("ErrorText");
		int result = pipe.parseEncryptedRequest(request.getParameter("trandata"));

		if (result != 0) {
			pipe.getError();
		} else {
			if (errorText == null) {
				resultResponse = pipe.getResult();
				gatewayResponse.setResult(pipe.getResult());
				gatewayResponse.setPostDate(pipe.getDate());
				gatewayResponse.setRef(pipe.getRef());
				gatewayResponse.setTrackId(pipe.getTrackId());
				gatewayResponse.setTranxId(pipe.getTransId());
				gatewayResponse.setUdf3(pipe.getUdf3());
				gatewayResponse.setUdf1(pipe.getUdf1());
				gatewayResponse.setUdf2(pipe.getUdf2());
				gatewayResponse.setUdf4(pipe.getUdf4());
				gatewayResponse.setUdf5(pipe.getUdf5());
				gatewayResponse.setPaymentId(pipe.getPaymentId());
			} else {
				gatewayResponse.setErrorText(request.getParameter("ErrorText"));
				gatewayResponse.setPaymentId(request.getParameter("paymentid"));
				gatewayResponse.setError(request.getParameter("Error"));
				gatewayResponse.setTrackId(request.getParameter("trackid"));
				gatewayResponse.setTranxId(request.getParameter("tranid"));
			}
		}
		
		String expectedSecrete = PaymentConstant.getHashedSecrete(
				getPaymentParamStr(gatewayResponse.getUdf3().trim(), gatewayResponse.getTrackId()),
				PaymentConstant.KWT_SECRETE_SALT);
		
		if(null != gatewayResponse.getUdf5() && null != expectedSecrete) {
			LOGGER.info("Secrete ====>  " +  expectedSecrete);
			LOGGER.info("UDF5 ====>  " +  gatewayResponse.getUdf5());
			if(!expectedSecrete.equals(gatewayResponse.getUdf5())) {
				throw new RuntimeException("Transation Parmeters are mismatch");
			}
		}

		LOGGER.info("Params captured from KNET : " + JsonUtil.toJson(gatewayResponse));

		//KnetCodes knetCodes = (KnetCodes) PayGCodes.getPayGCode(resultResponse, KnetCodes.UNKNOWN);

		LOGGER.info("resultResponse ---> " + resultResponse);
		//gatewayResponse.setErrorCategory(knetCodes.getCategory());
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

	private String getPaymentParamStr(String docId, String trackId) {
		return docId + "-" + trackId;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Security.addProvider(new BouncyCastleProvider());		
	}
}