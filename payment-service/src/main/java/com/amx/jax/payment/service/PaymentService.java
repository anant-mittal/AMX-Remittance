/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.client.RemitClient;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.model.url.PaymentResponse;
import com.amx.jax.payment.model.url.PaymentResponseData;
import com.amx.jax.payment.util.PaymentUtil;
import com.fss.plugin.iPayPipe;

/**
 * @author Viki Sangani 14-Dec-2017 PaymentService.java
 */
@Component
public class PaymentService implements PayGClient {

	private static Logger LOG = Logger.getLogger(PaymentService.class);

	@Autowired
	private RemitClient remitClient;

	@Autowired
	PayGClients payGClients;
	@Autowired
	PayGSession payGSession;

	@Override
	public void initialize(PayGParams payGParams) {
		PayGClient payGClient = payGClients.getPayGClient(payGParams);
		if (payGClient.getClientCode() != this.getClientCode()) {
			payGClient.initialize(payGParams);
		} else {
			LOG.info("No Client Found");
		}
	}

	@Override
	public Services getClientCode() {
		return Services.DEFAULT;
	}

	@Override
	public void capture(PayGResponse payGResponse) {
		PayGParams payGParams = payGSession.getPayGParams();
		PayGClient payGClient = payGClients.getPayGClient(payGParams);
		if (payGClient.getClientCode() != this.getClientCode()) {
			payGClient.capture(payGResponse);
		} else {
			LOG.info("No Client Found");
		}
	}

	public HashMap<String, String> omanNetInitialize(Map<String, Object> params) {

		/////////////////// Temporary code
		/////////////////// ///////////////////////////////////////////////

		Map<String, Object> configMap = new HashMap<String, Object>();
		;
		configMap.put("action", "1");
		configMap.put("currency", "414");
		configMap.put("languageCode", "ENG");
		configMap.put("responseUrl", "https://applications2.almullagroup.com/payg/app/payment_capture/");
		// configMap.put("responseUrl",
		// "https://applications2.almullagroup.com:8080/payment-service/app/payment_capture/");
		configMap.put("resourcePath", "/home/devenvironment/certificates/amxremit_test/");
		configMap.put("aliasName", "mulla");

		LOG.info("KNET payment configuration : " + PaymentUtil.getMapKeyValue(configMap));
		///////////////////////////////////////////////////////////////////////////////////

		iPayPipe pipe = new iPayPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((configMap.get("currency")).toString());
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setKeystorePath((String) configMap.get("resourcePath"));

			pipe.setAmt((String) params.get("amount"));
			pipe.setTrackId((String) params.get("trckid")); // Customer Reference.

			String udf1 = (String) params.get("udf1");
			String udf2 = (String) params.get("udf2");
			String udf3 = (String) params.get("udf3");
			String udf4 = (String) params.get("udf4");
			String udf5 = (String) params.get("udf5");

			if (udf1 != null) {
				pipe.setUdf1(udf1);
			}
			if (udf2 != null) {
				pipe.setUdf2(udf2);
			}
			if (udf3 != null) {
				pipe.setUdf3(udf3);
			}
			if (udf4 != null) {
				pipe.setUdf4(udf4);
			}
			if (udf5 != null) {
				pipe.setUdf5(udf5);
			}

			// Short pipeValue = pipe.performTransaction();
			int pipeValue = pipe.performPaymentInitializationHTTP();
			LOG.info("pipeValue :" + pipeValue);

			// log.info(pipe.performPaymentInitializationHTTP());

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getWebAddress(); // pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return responseMap;
	}

	@Value("${knet.certificate.path}")
	String knetCertpath;

	public HashMap<String, String> knetInitialize(Map<String, Object> params) {

		// Map<String, Object> configMap =
		// this.getPGConfig((BigDecimal)params.get("pgId"));
		// log.info("KNET payment configuration : " +
		// PaymentUtil.getMapKeyValue(configMap));

		/////////////////// Temporary code
		/////////////////// ///////////////////////////////////////////////

		Map<String, Object> configMap = new HashMap<String, Object>();
		;
		configMap.put("action", "1");
		configMap.put("currency", "414");
		configMap.put("languageCode", "ENG");
		configMap.put("responseUrl", "https://applications2.almullagroup.com/payg/app/payment_capture/");
		// configMap.put("responseUrl",
		// "https://applications2.almullagroup.com:8080/payment-service/app/payment_capture/");
		configMap.put("resourcePath", knetCertpath);
		configMap.put("aliasName", "mulla");

		LOG.info("KNET payment configuration : " + PaymentUtil.getMapKeyValue(configMap));
		///////////////////////////////////////////////////////////////////////////////////

		e24PaymentPipe pipe = new e24PaymentPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			// pipe.setCurrency((configMap.get("currency")).toString());
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt((String) params.get("amount"));
			pipe.setTrackId((String) params.get("trckid")); // Customer Reference.

			String udf1 = (String) params.get("udf1");
			String udf2 = (String) params.get("udf2");
			String udf3 = (String) params.get("docNo");
			String udf4 = (String) params.get("udf4");
			String udf5 = (String) params.get("udf5");

			if (udf1 != null) {
				pipe.setUdf1(udf1);
			}
			if (udf2 != null) {
				pipe.setUdf2(udf2);
			}
			if (udf3 != null) {
				pipe.setUdf3(udf3);
			}
			if (udf4 != null) {
				pipe.setUdf4(udf4);
			}
			if (udf5 != null) {
				pipe.setUdf5(udf5);
			}
			Short pipeValue = pipe.performPaymentInitialization();
			System.out.println("pipeValue :" + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				LOG.info(pipe.getErrorMsg());
				LOG.info(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to KNET - Error Code KU-KNETINIT");
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return responseMap;
	}

	public HashMap<String, String> bahKnetInitialize(Map<String, Object> params) {

		// Map<String, Object> configMap =
		// this.getPGConfig((BigDecimal)params.get("pgId"));
		// log.info("KNET payment configuration : " +
		// PaymentUtil.getMapKeyValue(configMap));

		/////////////////// Temporary code
		/////////////////// ///////////////////////////////////////////////

		Map<String, Object> configMap = new HashMap<String, Object>();
		;
		configMap.put("action", "1");
		configMap.put("currency", "414");
		configMap.put("languageCode", "ENG");
		configMap.put("responseUrl",
				"https://applications2.almullagroup.com:8080/payment-service/app/payment_capture/");
		configMap.put("resourcePath", "/home/devenvironment/certificates/amxremit_bah/");
		configMap.put("aliasName", "test_MEC");

		LOG.info("KNET payment configuration : " + PaymentUtil.getMapKeyValue(configMap));
		///////////////////////////////////////////////////////////////////////////////////

		e24PaymentPipe pipe = new e24PaymentPipe();
		HashMap<String, String> responseMap = new HashMap<String, String>();

		try {

			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			// pipe.setCurrency((configMap.get("currency")).toString());
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt((String) params.get("amount"));
			pipe.setTrackId((String) params.get("trckid")); // Customer Reference.

			String udf1 = (String) params.get("udf1");
			String udf2 = (String) params.get("udf2");
			String udf3 = (String) params.get("docNo");
			String udf4 = (String) params.get("udf4");
			String udf5 = (String) params.get("udf5");

			if (udf1 != null) {
				pipe.setUdf1(udf1);
			}
			if (udf2 != null) {
				pipe.setUdf2(udf2);
			}
			if (udf3 != null) {
				pipe.setUdf3(udf3);
			}
			if (udf4 != null) {
				pipe.setUdf4(udf4);
			}
			if (udf5 != null) {
				pipe.setUdf5(udf5);
			}
			Short pipeValue = pipe.performPaymentInitialization();
			System.out.println("pipeValue :" + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				LOG.info(pipe.getErrorMsg());
				LOG.info(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to KNET - Error Code KU-KNETINIT");
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		return responseMap;
	}

	public PaymentResponse capturePayment(HashMap<String, String> paramMap) {

		PaymentResponseData data = new PaymentResponseData();
		PaymentResponse response = new PaymentResponse();

		try {

			PaymentResponseDto paymentResponseDto = generatePaymentResponseDTO(paramMap);

			LOG.info("Calling saveRemittanceTransaction with ...  " + paymentResponseDto.toString());
			ApiResponse<PaymentResponseDto> resp = remitClient.saveRemittanceTransaction(paymentResponseDto);

			StringBuilder sb = new StringBuilder();

			for (Map.Entry<String, String> entry : paramMap.entrySet()) {
				String keyValue = entry.getKey() + "/" + entry.getValue();
				sb.append(keyValue);
				LOG.info(keyValue);
			}

			data.setMsg(sb.toString());
			response.setResponseCode("SUCCESS");
			response.setResponseMessage("Payment is captured successfully.");
			response.setData(data);
		} catch (Exception e) {
			LOG.error("Exception while making FROM address : " + e.getMessage());
			// response.setError(e.getMessage());
			response.setResponseCode("FAIL");
			response.setResponseMessage("Exception while capturing payment.");
		}

		response.setError(null);
		return response;

	}

	public PaymentResponse captureOmanPayment(String param, HttpServletRequest request) {

		PaymentResponseData data = new PaymentResponseData();
		PaymentResponse response = new PaymentResponse();

		try {
			StringBuilder sb = new StringBuilder();

			// Map<String, Object> configMap = this.getPGConfig(new BigDecimal(3));
			Map<String, Object> configMap = new HashMap<String, Object>();

			String ref = null;
			String result = null;
			String postdate = null;
			String tranid = null;
			String auth = null;
			String trackid = null;
			String errorText = null;
			String payid = null;
			String paymentId = null;
			String udf1 = null;
			String udf2 = null;
			String udf3 = null;
			String udf4 = null;
			String udf5 = null;
			int val = -1;

			iPayPipe pipe = new iPayPipe();
			errorText = request.getParameter("ErrorText");
			result = request.getParameter("result");
			String tranData = request.getParameter("trandata");
			System.out
					.println("JSP1 tranData :" + tranData + "\n errorText :" + errorText + "\n tranresult :" + result);
			// D:\Resource\oman_amxremit_test
			System.out.println("JSP from Session setAttribute :" + configMap.get("action"));
			System.out.println("JSP from Session domainUrl :" + configMap.get("domainUrl"));
			// From DB Session
			pipe.setAction(configMap.get("action") == null ? "" : configMap.get("action").toString());
			pipe.setCurrency(configMap.get("currency") == null ? "" : configMap.get("currency").toString());
			pipe.setLanguage(configMap.get("languageCode") == null ? "" : configMap.get("languageCode").toString());
			pipe.setResourcePath(configMap.get("resourcePath") == null ? "" : configMap.get("resourcePath").toString());
			pipe.setKeystorePath(configMap.get("resourcePath") == null ? "" : configMap.get("resourcePath").toString());
			pipe.setAlias(configMap.get("aliasName") == null ? "" : configMap.get("aliasName").toString());
			if (tranData != null) {
				val = pipe.parseEncryptedRequest(request.getParameter("trandata"));
			}

			paymentId = pipe.getPaymentId();
			udf1 = pipe.getUdf1();
			udf2 = pipe.getUdf2();
			udf3 = pipe.getUdf3();
			udf4 = pipe.getUdf4();
			udf5 = pipe.getUdf5();
			result = pipe.getResult();
			trackid = pipe.getTrackId();
			tranid = pipe.getTransId();
			ref = pipe.getRef();
			auth = pipe.getAuth();
			postdate = pipe.getDate();

			data.setMsg(sb.toString());
			response.setResponseCode("SUCCESS");
			response.setResponseMessage("Payment is captured successfully.");
			response.setData(data);
		} catch (Exception e) {
			LOG.error("Exception while making FROM address : " + e.getMessage());
			// response.setError(e.getMessage());
			response.setResponseCode("FAIL");
			response.setResponseMessage("Exception while capturing payment.");
		}

		response.setError(null);
		return response;

	}

	/*
	 * public Map<String,Object> getPGConfig(BigDecimal id) {
	 * 
	 * OnlineConfiguration pgConfig = onlineConfigurationDao.getPGConfig(id);
	 * 
	 * Map<String,Object> paramValueMap=new HashMap<String,Object>();
	 * 
	 * paramValueMap.put("action", pgConfig.getAction());
	 * paramValueMap.put("currency", pgConfig.getCurrency());
	 * paramValueMap.put("languageCode", pgConfig.getLanguageCode());
	 * paramValueMap.put("resourcePath", pgConfig.getResourcePath());
	 * paramValueMap.put("responseUrl", pgConfig.getResponseUrl());
	 * paramValueMap.put("aliasName", pgConfig.getAliasName()); return
	 * paramValueMap; }
	 */

	public PaymentResponseDto generatePaymentResponseDTO(HashMap<String, String> params) {
		PaymentResponseDto paymentResponseDto = new PaymentResponseDto();

		// paymentResponseDto.setApplicationCountryId(params.get(""));
		paymentResponseDto.setAuth_appNo(params.get("auth_appNo"));
		paymentResponseDto.setTransactionId(params.get("tranId"));
		paymentResponseDto.setResultCode(params.get("result"));
		paymentResponseDto.setPostDate(params.get("postDate"));
		// paymentResponseDto.setCustomerId(params.get(""));
		paymentResponseDto.setTrackId(params.get("trackId"));
		paymentResponseDto.setReferenceId(params.get("referenceId"));
		paymentResponseDto.setUdf1(params.get("udf1"));
		paymentResponseDto.setUdf2(params.get("udf2"));
		paymentResponseDto.setUdf3(params.get("udf3"));
		paymentResponseDto.setUdf4(params.get("udf4"));
		paymentResponseDto.setUdf5(params.get("udf5"));
		paymentResponseDto.setPaymentId(params.get("paymentId"));

		return paymentResponseDto;
	}

}
