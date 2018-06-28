package com.amx.jax.payg;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.jax.AppConfig;
import com.amx.jax.AppConstants;
import com.amx.jax.AppContext;
import com.amx.jax.AppContextUtil;
import com.amx.jax.payment.PayGService;
import com.amx.utils.URLBuilder;

@Component
public class PayGServiceClient implements PayGService {

	@Autowired
	private AppConfig appConfig;

	public String getPaymentUrl(RemittanceApplicationResponseModel remittanceApplicationResponseModel, String callback)
			throws MalformedURLException, URISyntaxException {

		AppContext context = AppContextUtil.getContext();

		URLBuilder builder = new URLBuilder(appConfig.getPaygURL());

		String callbackUrl = callback + "?docNo=" + remittanceApplicationResponseModel.getDocumentIdForPayment()
				+ "&docFy=" + remittanceApplicationResponseModel.getDocumentFinancialYear();
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());

		builder.setPath("app/payment").addParameter("amount", remittanceApplicationResponseModel.getNetPayableAmount())
				.addParameter("trckid", remittanceApplicationResponseModel.getMerchantTrackId())
				.addParameter("pg", remittanceApplicationResponseModel.getPgCode())
				.addParameter("docFy", remittanceApplicationResponseModel.getDocumentFinancialYear())
				.addParameter("docNo", remittanceApplicationResponseModel.getDocumentIdForPayment())
				.addParameter("tnt", context.getTenant()).addParameter("callbackd", callbackd)
				.addParameter(AppConstants.TRACE_ID_XKEY, context.getTraceId());
		return builder.getURL();
	}

}