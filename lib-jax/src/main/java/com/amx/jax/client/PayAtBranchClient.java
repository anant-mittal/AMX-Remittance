package com.amx.jax.client;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;


import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.response.payatbranch.PayAtBranchTrnxListDTO;
import com.amx.jax.rest.RestService;
import com.amx.jax.service.IWireTransferService;
@Component
public class PayAtBranchClient implements IWireTransferService{
	private static final Logger logger = Logger.getLogger(PayAtBranchClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public AmxApiResponse<ResourceDTO, Object> getPaymentModes() {

		try {
			return restService.ajax(appConfig.getJaxURL())
					.path(Path.PREFIX + Path.PAYMENT_MODES)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<ResourceDTO, Object>>() {
					});
		} catch (Exception ae) {
			logger.error("exception in Annual Income details : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}
	
	@Override
	public AmxApiResponse<PayAtBranchTrnxListDTO, Object> getPbTrnxList() {

		try {
			return restService.ajax(appConfig.getJaxURL())
					.path(Path.PREFIX + Path.PB_TRNX_LIST)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<PayAtBranchTrnxListDTO, Object>>() {
					});
		} catch (Exception ae) {
			logger.error("exception in get wire transaction list : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<PayAtBranchTrnxListDTO, Object> getPbTrnxListBranch() {

		try {
			return restService.ajax(appConfig.getJaxURL())
					.path(Path.PREFIX + Path.PB_TRNX_LIST_BRANCH)
					.meta(new JaxMetaInfo()).post()
					.as(new ParameterizedTypeReference<AmxApiResponse<PayAtBranchTrnxListDTO, Object>>() {
					});
		} catch (Exception ae) {
			logger.error("exception in get wire transaction list for branch : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}
}
