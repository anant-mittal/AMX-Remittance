package com.amx.amxlib.service;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;
import com.bootloaderjs.URLBuilder;

public abstract class AbstractPayGService {

	public abstract String getPayGServiceHost();

	public abstract String getCountryId();

	public String getPaymentUrl(RemittanceApplicationResponseModel remittanceApplicationResponseModel)
			throws MalformedURLException, URISyntaxException {
		URLBuilder builder = new URLBuilder(getPayGServiceHost());
		builder.setPath("app/payment").addParameter("country", getCountryId())
				.addParameter("amount", remittanceApplicationResponseModel.getNetPayableAmount())
				.addParameter("trckid", remittanceApplicationResponseModel.getMerchantTrackId())
				.addParameter("pg", "knet")
				.addParameter("docNo", remittanceApplicationResponseModel.getDocumentIdForPayment());
		return builder.getURL();
	}

}
