package com.amx.jax.payment.gateway;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class PayGService implements PayGClient {

	private static Logger LOG = Logger.getLogger(PayGService.class);

	@Autowired
	PayGClients payGClients;
	@Autowired
	PayGSession payGSession;
	@Autowired
	PayGRequest payGRequest;
	
	@Override
	public void initialize(PayGParams payGParams) {
		PayGClient payGClient = payGClients.getPayGClient(payGRequest.getService(), payGRequest.getCountryCode());
		if (payGClient.getClientCode() != this.getClientCode()) {
			payGSession.setPayGParams(payGParams);
			payGClient.initialize(payGParams);
		} else {
			LOG.info("No Client Found");
		}
	}

	@Override
	public Services getClientCode() {
		return Services.DEFAULT;
	}

	@Override
	public void capture(PayGResponse payGResponse) {
		PayGClient payGClient = payGClients.getPayGClient(payGRequest.getService(), payGRequest.getCountryCode());
		if (payGClient.getClientCode() != this.getClientCode()) {
			payGClient.capture(payGResponse);
		} else {
			LOG.info("No Client Found");
		}
	}

}
