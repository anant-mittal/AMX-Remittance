package com.amx.jax.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint.EKyc;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.rest.RestService;

import org.apache.log4j.Logger;
@Component
public class EKycClient extends AbstractJaxServiceClient {
		private static final Logger logger = Logger.getLogger(UserClient.class);
		
		
		@Autowired
		RestService restService;
		
		public BoolRespModel eKycsaveDetails(String image, String expiryDate) {
			try {

				return restService.ajax(appConfig.getJaxURL())
						.path(EKyc.PREFIX + EKyc.EKYC_SAVE_CUSTOMER).meta(new JaxMetaInfo())
						.queryParam(EKyc.IMAGE, image).queryParam(EKyc.EXPIRYDATE, expiryDate)
						.post()
						.as(new ParameterizedTypeReference<BoolRespModel>() {
						});
			} catch (Exception ae) {

				logger.error("exception in save eKyc details : ", ae);
				return JaxSystemError.evaluate(ae);
			}
		}
}
