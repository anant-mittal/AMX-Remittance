package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.jax.dict.Channel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payg.PayGCodes;
import com.amx.jax.payg.PaymentResponseDto;
import com.amx.jax.payg.codes.BenefitCodes;
import com.amx.jax.payg.codes.OmanNetCodes;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.utils.JsonUtil;
import com.fss.plugin.iPayPipe;

@Component
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

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.OMANNET;
	}

	@Override
	public void initialize(PayGParams payGParams) {

		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", OmemnetAction);
		configMap.put("currency", OmemnetCurrency);
		configMap.put("languageCode", OmemnetLanguageCode);
		configMap.put("responseUrl", OmemnetCallbackUrl + "/app/capture/OMANNET/" + payGParams.getTenant() + "/"
				+ payGParams.getChannel() + "/");
		// configMap.put("responseUrl",
		// OmemnetCallbackUrl+"/app/capture/OMANNET/" + payGParams.getTenant() + "/");
		configMap.put("resourcePath", OmemnetCertpath);
		configMap.put("keystorePath", OmemnetCertpath);
		configMap.put("aliasName", OmemnetAliasName);

		LOGGER.info("Oman omannet payment configuration : " + JsonUtil.toJson(configMap));

		iPayPipe pipe = new iPayPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setKeystorePath((String) configMap.get("keystorePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt((String) payGParams.getAmount());
			pipe.setTrackId((String) payGParams.getTrackId());
			pipe.setUdf3(payGParams.getDocNo());

			int pipeValue = pipe.performPaymentInitializationHTTP();
			LOGGER.info("pipeValue : " + pipeValue);

			if (pipeValue != 0) {
				responseMap.put("errorMsg", pipe.getError());
				LOGGER.error(pipe.getError());
				LOGGER.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to Oman.");
			}
			LOGGER.info("Generated web address is ---> " + pipe.getWebAddress());
			payGParams.setRedirectUrl(pipe.getWebAddress());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@SuppressWarnings("finally")
	@Override
	public PayGResponse capture(PayGResponse gatewayResponse, Channel channel) {

		// Capturing GateWay Response
		gatewayResponse.setPaymentId(request.getParameter("paymentid"));
		gatewayResponse.setAuth(request.getParameter("auth"));
		gatewayResponse.setResponseCode(request.getParameter("responseData"));
		gatewayResponse.setUdf1(request.getParameter("udf1"));
		gatewayResponse.setUdf2(request.getParameter("udf2"));
		gatewayResponse.setUdf3(request.getParameter("udf3"));
		gatewayResponse.setUdf4(request.getParameter("udf4"));
		gatewayResponse.setUdf5(request.getParameter("udf5"));
		gatewayResponse.setCountryId(Tenant.OMN.getCode());

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
			if (resultResponse.equals("CAPTURED")) {
				gatewayResponse.setError(pipe.getError());
				gatewayResponse.setErrorText(pipe.getError_text());
			} else {
				gatewayResponse.setError(pipe.getResult());
				gatewayResponse.setErrorText(pipe.getResult());
			}

			LOGGER.info("resultResponse ---> " + resultResponse);
			OmanNetCodes statusCode = (OmanNetCodes) PayGCodes.getPayGCode(resultResponse, OmanNetCodes.UNKNOWN);
			gatewayResponse.setErrorCategory(statusCode.getCategory());

			LOGGER.info("Result from response Values ---> " + gatewayResponse.getErrorCategory());
			gatewayResponse.setError(resultResponse);
		}

		LOGGER.info("Params captured from OMANNET : " + JsonUtil.toJson(gatewayResponse));

		if (channel.equals(Channel.ONLINE)) {
			PaymentResponseDto resdto = paymentService.capturePayment(gatewayResponse);
			// Capturing JAX Response
			gatewayResponse.setCollectionFinYear(resdto.getCollectionFinanceYear().toString());
			gatewayResponse.setCollectionDocCode(resdto.getCollectionDocumentCode().toString());
			gatewayResponse.setCollectionDocNumber(resdto.getCollectionDocumentNumber().toString());
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
