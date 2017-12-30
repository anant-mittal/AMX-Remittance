package com.amx.jax.ui.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amx.amxlib.service.AbstractPayGService;

@Component
public class PayGService extends AbstractPayGService {

	@Value("${jax.payment.url}")
	private String paymentUrl;

	@Autowired
	private JaxService jaxService;

	@Override
	public String getPayGServiceHost() {
		return paymentUrl;
	}

	@Override
	public String getCountryId() {
		return jaxService.DEFAULT_COUNTRY_ID;
	}

}
