package com.amx.jax.payment.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aciworldwide.commerce.gateway.plugins.e24PaymentPipe;
import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.util.PaymentUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class BenefitClient implements PayGClient {

	private Logger log = Logger.getLogger(BenefitClient.class);

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
	private PaymentService paymentService;

	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.KNET;
	}

	@Override
	public void initialize(PayGParams payGParams) {

		Map<String, Object> configMap = new HashMap<String, Object>();

		configMap.put("action", benefitAction);
		configMap.put("currency", benefitCurrency);
		configMap.put("languageCode", benefitLanguageCode);
		configMap.put("responseUrl", benefitCallbackUrl + "/app/capture/KNET/BRN/");
		configMap.put("resourcePath", benefitCertpath);
		configMap.put("aliasName", benefitAliasName);

		log.info("Baharain KNET payment configuration : " + PaymentUtil.getMapKeyValue(configMap));

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
			pipe.setAmt((String) payGParams.getAmount());
			pipe.setTrackId((String) payGParams.getTrackId());

			pipe.setUdf3(payGParams.getDocNo());

			Short pipeValue = pipe.performPaymentInitialization();
			System.out.println("pipeValue :" + pipeValue);

			if (pipeValue != e24PaymentPipe.SUCCESS) {
				responseMap.put("errorMsg", pipe.getErrorMsg());
				responseMap.put("debugMsg", pipe.getDebugMsg());
				log.error(pipe.getErrorMsg());
				log.debug(pipe.getDebugMsg());
				throw new RuntimeException("Problem while sending transaction to Benefit.");
			}

			// get results
			String payID = pipe.getPaymentId();
			String payURL = pipe.getPaymentPage();

			responseMap.put("payid", new String(payID));
			responseMap.put("payurl", new String(payURL));

			String url = payURL + "?paymentId=" + payID;
			payGParams.setRedirectUrl(url);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}

	}

	@Override
	public PayGResponse capture(PayGResponse response) {
		return null;
	}

}
