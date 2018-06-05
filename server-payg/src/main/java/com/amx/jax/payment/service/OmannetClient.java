package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
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
	
	@Value("${omannet.keystore.path}")
	String OmemnetKeyStorepath;
	
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
		configMap.put("responseUrl",
				payGConfig.getServiceCallbackUrl() + "/app/capture/OMANNET/" + payGParams.getTenant() + "/");
		configMap.put("resourcePath", OmemnetCertpath);
		configMap.put("keystorePath", OmemnetKeyStorepath);
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
				/*responseMap.put("debugMsg", pipe.getDebugMsg());*/
				LOGGER.error(pipe.getError());
				LOGGER.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to Oman.");
			}
			LOGGER.info("Generated web address is ---> "+pipe.getWebAddress());
			
			String payID = pipe.getPaymentId();
			String payURL = pipe.getWebAddress();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));
			

			String url = payURL + "?paymentId=" + payID;
			LOGGER.info("Generated url is ---> " + url);
			payGParams.setRedirectUrl(payURL);
			
		
	    } catch (Exception e) {
		e.printStackTrace();
		throw new RuntimeException(e);
	    }
		
	}

	@SuppressWarnings("finally")
	@Override
	public PayGResponse capture(PayGResponse gatewayResponse) {

		// Capturing GateWay Response
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
		gatewayResponse.setCountryId(Tenant.OMN.getCode());
		
		LOGGER.info("Params captured from OMANNET : " + JsonUtil.toJson(gatewayResponse));

		PaymentResponseDto resdto = paymentService.capturePayment(gatewayResponse);
		// Capturing JAX Response
		gatewayResponse.setCollectionFinYear(resdto.getCollectionFinanceYear().toString());
		gatewayResponse.setCollectionDocCode(resdto.getCollectionDocumentCode().toString());
		gatewayResponse.setCollectionDocNumber(resdto.getCollectionDocumentNumber().toString());

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
