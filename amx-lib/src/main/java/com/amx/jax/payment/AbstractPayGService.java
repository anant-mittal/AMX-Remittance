package com.amx.jax.payment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.Base64;

import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.amx.jax.scope.Tenant;
import com.bootloaderjs.URLBuilder;

public abstract class AbstractPayGService {

	public abstract String getPayGServiceHost();

	public abstract String getCountryId();

	public String getPaymentUrl(RemittanceApplicationResponseModel remittanceApplicationResponseModel, String callback,
			Tenant tnt, PayGServiceCode code) throws MalformedURLException, URISyntaxException {
		URLBuilder builder = new URLBuilder(getPayGServiceHost());
		String callbackUrl = callback + "?docNo=" + remittanceApplicationResponseModel.getDocumentIdForPayment();
		String callbackd = Base64.getEncoder().encodeToString(callbackUrl.getBytes());
		builder.setPath("app/payment").addParameter("country", getCountryId())
				.addParameter("amount", remittanceApplicationResponseModel.getNetPayableAmount())
				.addParameter("trckid", remittanceApplicationResponseModel.getMerchantTrackId())
				.addParameter("pg", code)
				.addParameter("docNo", remittanceApplicationResponseModel.getDocumentIdForPayment())
				.addParameter("tnt", tnt).addParameter("callbackd", callbackd);
		return builder.getURL();
	}

}
