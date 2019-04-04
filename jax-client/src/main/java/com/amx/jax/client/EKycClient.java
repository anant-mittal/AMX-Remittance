package com.amx.jax.client;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;

import com.amx.amxlib.constant.ApiEndpoint.EKyc;
import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.exception.JaxSystemError;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.model.request.ImageSubmissionRequest;
import com.amx.jax.rest.RestService;
@Component
public class EKycClient extends AbstractJaxServiceClient {
		private static final Logger logger = Logger.getLogger(EKycClient.class);
		
		
		@Autowired
		RestService restService;
		
		public BoolRespModel eKycsaveDetails(ImageSubmissionRequest imageSubmissionRequest) {
			try {
				HttpEntity<ImageSubmissionRequest> requestEntity = new HttpEntity<ImageSubmissionRequest>(imageSubmissionRequest,getHeader());
				String eKycSaveCustomer = this.getBaseUrl() + EKyc.PREFIX + EKyc.EKYC_SAVE_CUSTOMER;
				
				return restService.ajax(eKycSaveCustomer).post(requestEntity)
						.as(new ParameterizedTypeReference<BoolRespModel>() {
						});
			} catch (AbstractJaxException ae) {
				throw ae;
			} catch (Exception e) {
				logger.error("exception in ekycsaveDetails : ", e);
				throw new JaxSystemError();
			} // end of try-catch
		}
}
