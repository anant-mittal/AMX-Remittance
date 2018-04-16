package com.amx.jax.payment;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

import com.amx.amxlib.model.response.RemittanceApplicationResponseModel;

public interface PayGService {

	public String getPaymentUrl(RemittanceApplicationResponseModel remittanceApplicationResponseModel, String callback)
			throws MalformedURLException, URISyntaxException;

}
