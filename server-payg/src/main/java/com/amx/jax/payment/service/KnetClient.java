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
import org.springframework.ui.Model;

import com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;
import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.model.url.PaymentResponse;
import com.amx.jax.payment.model.url.PaymentResponseData;
import com.amx.jax.payment.util.PaymentUtil;
import com.amx.jax.scope.Tenant;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class KnetClient implements PayGClient {

	private Logger log = Logger.getLogger(KnetClient.class);

	@Value("${knet.certificate.path}")
	String knetCertpath;

	@Value("${knet.callback.url}")
	String knetCallbackUrl;
	
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
		configMap.put("responseUrl", knetCallbackUrl + "/app/capture/KNET/"+payGParams.getTenant()+"/");
		configMap.put("resourcePath", knetCertpath);
		configMap.put("aliasName", knetAliasName);

		log.info("KNET payment configuration : " + PaymentUtil.getMapKeyValue(configMap));

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

	     	log.info("Amount to remit is --> "+amount);
	     	
			pipe.setAction((String) configMap.get("action"));
			pipe.setCurrency((String) configMap.get("currency"));
			// pipe.setCurrency((configMap.get("currency")).toString());
			pipe.setLanguage((String) configMap.get("languageCode"));
			pipe.setResponseURL((String) configMap.get("responseUrl"));
			pipe.setErrorURL((String) configMap.get("responseUrl"));
			pipe.setResourcePath((String) configMap.get("resourcePath"));
			pipe.setAlias((String) configMap.get("aliasName"));
			pipe.setAmt(amount);
			pipe.setTrackId((String) payGParams.getTrackId());

			pipe.setUdf3(payGParams.getDocNo());

			Short pipeValue = pipe.performPaymentInitialization();
			log.info("pipeValue : " + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				log.info("KNET-ERROR" + pipe.getErrorMsg());
				log.info("KNET-DEBUg" + pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to KNET - Error Code KU-KNETINIT");
			} else {
				log.info(pipe.getDebugMsg());
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

			String url = payURL + "?paymentId=" + payID;
			log.info("Generated url is ---> "+url);
			payGParams.setRedirectUrl(url);

		} catch(NumberFormatException e) {
			log.error(String.format("Amount entered --> %s ,is not correct number.",amount),e);
		}catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public String capture(Model model) {

		String paymentid = request.getParameter("paymentid");
		String result = request.getParameter("result");
		String auth = request.getParameter("auth");
		String ref = request.getParameter("ref");
		String postdate = request.getParameter("postdate");
		String trackid = request.getParameter("trackid");
		String tranid = request.getParameter("tranid");
		String responsecode = request.getParameter("responsecode");
		String udf1 = request.getParameter("udf1");
		String udf2 = request.getParameter("udf2");
		String udf3 = request.getParameter("udf3");
		String udf4 = request.getParameter("udf4");
		String udf5 = request.getParameter("udf5");

		HashMap<String, String> paramMap = new HashMap<String, String>();

		paramMap.put("paymentId", paymentid);
		paramMap.put("result", result);
		paramMap.put("auth_appNo", auth);
		paramMap.put("referenceId", ref);
		paramMap.put("postDate", postdate);
		paramMap.put("trackId", trackid);
		paramMap.put("tranId", tranid);
		paramMap.put("responsecode", responsecode);
		paramMap.put("udf1", udf1);
		paramMap.put("udf2", udf2);
		paramMap.put("udf3", udf3);
		paramMap.put("udf4", udf4);
		paramMap.put("udf5", udf5);
		paramMap.put("applicationCountryId", Tenant.KWT.getCode());

		log.info("In Payment capture method with params : " + PaymentUtil.getMapAsString(paramMap));

		PaymentResponse res = paymentService.capturePayment(paramMap);

		String doccode = null;
		String docno = null;
		String finyear = null;
		PaymentResponseData data = null;

		String redirectUrl = null;
		if ("CAPTURED".equalsIgnoreCase(result)) {

			try {
				data = (PaymentResponseData) res.getData();
				doccode = data.getResponseDTO().getCollectionDocumentCode().toString();
				docno = data.getResponseDTO().getCollectionDocumentNumber().toString();
				finyear = data.getResponseDTO().getCollectionFinanceYear().toString();
			} catch (NullPointerException e) {
				log.error("Error while fetching doccode, docno, finyear.");
				e.printStackTrace();
			}

			redirectUrl = String.format(knetCallbackUrl + "/callback/success?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s&doccode=%s&docno=%s&finyear=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5, doccode,
					docno, finyear);
		} else if ("CANCELED".equalsIgnoreCase(result)) {
			redirectUrl = String.format(knetCallbackUrl + "/callback/cancelled?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s&doccode=%s&docno=%s&finyear=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5, doccode,
					docno, finyear);
		} else {
			redirectUrl = String.format(knetCallbackUrl + "/callback/error?"
					+ "PaymentID=%s&result=%s&auth=%s&ref=%s&postdate=%s&trackid=%s&tranid=%s&udf1=%s&udf2=%s&udf3=%s&udf4=%s&udf5=%s&doccode=%s&docno=%s&finyear=%s",
					paymentid, result, auth, ref, postdate, trackid, tranid, udf1, udf2, udf3, udf4, udf5, doccode,
					docno, finyear);
		}

		model.addAttribute("REDIRECT", redirectUrl);

		return "thymeleaf/repback";
	}

}
