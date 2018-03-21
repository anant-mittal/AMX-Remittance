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
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.payment.PayGServiceCode;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.jax.scope.Tenant;
import com.bootloaderjs.JsonUtil;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
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
		configMap.put("responseUrl",
                payGConfig.getServiceCallbackUrl() + "/app/capture/BENEFIT/" + payGParams.getTenant() + "/");
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

			String url = payURL + "?PaymentID=" + payID;
			LOGGER.info("Generated url is ---> " + url);
			payGParams.setRedirectUrl(url);

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
        gatewayResponse.setCountryId(Tenant.BHR.getCode());
        gatewayResponse.setErrorText(request.getParameter("ErrorText"));
        
        LOGGER.info("Params captured from BENEFIT : " + JsonUtil.toJson(gatewayResponse));

        try {
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
        } catch (Exception e) {
            LOGGER.error("payment service error in capturePayment method : ", e);
            gatewayResponse.setPayGStatus(PayGStatus.ERROR);
        }finally {
            return gatewayResponse;
        }// end of try-catch-finally
    }// end of capture

}
