package com.amx.jax.payment.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import com.amx.jax.payment.gateway.PayGClient;
import com.amx.jax.payment.gateway.PayGParams;
import com.amx.jax.payment.gateway.PayGResponse;

/**
 * 
 * @author lalittanwar
 *
 */
@Component
public class KnetClient implements PayGClient {

	@Value("${knet.certificate.path}")
	String knetCertpath;

	@Autowired
	HttpServletResponse response;

	@Autowired
	HttpServletRequest request;

	@Override
	public ServiceCode getClientCode() {
		return ServiceCode.KNET;
	}

	@Override
	public void initialize(PayGParams payGParams) {
		// TODO Auto-generated method stub

	}

	@Override
	public String capture(Model model) {
		return null;
	}

}
