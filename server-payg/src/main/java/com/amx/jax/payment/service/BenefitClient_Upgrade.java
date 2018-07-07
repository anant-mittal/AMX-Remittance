package com.amx.jax.payment.service;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.aciworldwide.commerce.gateway.plugins.UniversalPlugin;
import com.amx.amxlib.meta.model.PaymentResponseDto;
import com.amx.jax.AppConstants;
import com.amx.jax.cache.TransactionModel;
import com.amx.jax.dict.PayGServiceCode;
import com.amx.jax.dict.Tenant;
import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGClients;
import com.amx.jax.payment.gateway.PayGConfig;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;
import com.amx.jax.payment.gateway.PayGResponse.PayGStatus;
import com.amx.utils.ContextUtil;
import com.amx.utils.JsonUtil;

@Component
public class BenefitClient_Upgrade implements PayGClient {

	private static final Logger LOGGER = Logger.getLogger(BenefitClient_Upgrade.class);
	
	@Value("${benefit_upgrade.certificate.path}")
	String benefitCertpath;

	@Value("${benefit_upgrade.callback.url}")
	String benefitCallbackUrl;

	@Value("${benefit_upgrade.alias.name}")
	String benefitAliasName;
	
	@Value("${benefit_upgrade.action}")
	String benefitAction;

	@Value("${benefit_upgrade.currency}")
	String benefitCurrency;

	@Value("${benefit_upgrade.language.code}")
	String benefitLanguageCode;
	
	@Autowired
	PayGConfig payGConfig;
	
	@Autowired
	private PaymentService paymentService;
	
	@Autowired
	HttpServletRequest request;
		
	@Override
	public PayGServiceCode getClientCode() {
		return PayGServiceCode.BENEFIT_UPGRADE;
	}

	@Override
	public void initialize(PayGParams payGParams) {
		// Get and Store the quantity and price per unit and total price in session for the reciept page.
			
		UniversalPlugin CGPipe = new UniversalPlugin();
		
		String quantity = request.getParameter("quantity");
		double pricePerUnit = 12.34;
		double price = 5.1;
		/*session.setAttribute("quantity", "" + quantity);
		session.setAttribute("unitPrice", "" + pricePerUnit);
		session.setAttribute("totalPrice", "" + price);*/

	    // Turn off ssl for this test.
		CGPipe.set("action", benefitAction);	// 1 - Purchase, 4 - Authorization
		CGPipe.set("amt", "" + price);
		CGPipe.set("currencycode", benefitCurrency);
		CGPipe.set("trackid", "123456");
		CGPipe.set("langid", benefitLanguageCode);
		CGPipe.set("UDF2", "UDF2 Test Value");
		CGPipe.set("UDF3", "UDF3 Test Value");
		CGPipe.set("UDF4", "UDF4 Test Value");
		CGPipe.set("UDF5", "UDF5 Test Value");

	  
		/*String webURL = request.getRequestURL().toString();
		String fullContextPath = webURL.substring(0, (webURL.lastIndexOf("/") + 1));*/

		String responseURL = payGConfig.getServiceCallbackUrl() + "/app/capture/BENEFIT/" + payGParams.getTenant() + "/";
		//String errorURL = response.encodeRedirectURL(fullContextPath+"UniversalPluginCheckoutFailure.jsp");
		CGPipe.set("responseurl", responseURL);
		CGPipe.set("errorurl", responseURL);


	    CGPipe.setResourcePath(benefitCertpath);
		CGPipe.setTerminalAlias(benefitAliasName);

		CGPipe.setTransactionType("PaymentInit");
		CGPipe.setVersion("1");

		boolean success = CGPipe.performTransaction();
		LOGGER.info("Success : " + success);
		
		if (!success) {
			String error = CGPipe.getErrorText();
			//payGParams.setRedirectUrl(response.encodeURL(fullContextPath + "UniversalPluginCheckoutFailure.jsp?error=" + error));
			payGParams.setRedirectUrl("thymeleaf/pg_error");
			return;
		}
		
		String error = CGPipe.get("error_code_tag");
		String errorText = CGPipe.getErrorText();
		LOGGER.info("Error : " + errorText);
		
		if (error != null) {
		// response.sendRedirect(response.encodeURL(fullContextPath + "UniversalPluginCheckoutFailure.jsp?error=" + error));
			payGParams.setRedirectUrl("thymeleaf/pg_error");
			return ;
	    } else {

	        String url = CGPipe.get("PAYMENTPAGE");
	        String paymentId = CGPipe.get("PAYMENTID");
	        LOGGER.info(url + "-" + paymentId);
			
			payGParams.setRedirectUrl(url + "?PaymentID=" + paymentId);
	  }
		
	}

	@Override
	public PayGResponse capture(PayGResponse gatewayResponse) {
	
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
			/*PaymentResponseDto paymentCacheModel = get();
			LOGGER.info("Values ---> " + paymentCacheModel.toString());
			gatewayResponse.setUdf3(paymentCacheModel.getUdf3());*/
			gatewayResponse.setResponseCode("NOT CAPTURED");
			gatewayResponse.setResult("NOT CAPTURED");
			/*gatewayResponse.setTrackId(paymentCacheModel.getTrackId());*/
		}

		PaymentResponseDto resdto = paymentService.capturePayment(gatewayResponse);

		if ("CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CAPTURED);
			// Capturing JAX Response
			gatewayResponse.setCollectionFinYear(resdto.getCollectionFinanceYear().toString());
			gatewayResponse.setCollectionDocCode(resdto.getCollectionDocumentCode().toString());
			gatewayResponse.setCollectionDocNumber(resdto.getCollectionDocumentNumber().toString());
		} else if ("CANCELED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.CANCELLED);
		} else if ("NOT CAPTURED".equalsIgnoreCase(gatewayResponse.getResult())) {
			gatewayResponse.setPayGStatus(PayGStatus.NOT_CAPTURED);
		} else {
			gatewayResponse.setPayGStatus(PayGStatus.ERROR);
		}
		return gatewayResponse;
	}


}