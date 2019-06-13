/**
 * 
 */
package com.amx.jax.client.customer;

import java.text.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.configs.JaxMetaInfo;
import com.amx.jax.customer.ICustomerManagementController;
import com.amx.jax.exception.JaxSystemError;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.IdentityTypeDto;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.model.customer.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.rest.RestService;

/**
 * @author prashant
 *
 */
@Component
public class CustomerManagementClient implements ICustomerManagementController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagementClient.class);

	@Autowired
	JaxMetaInfo jaxMetaInfo;
	@Autowired
	RestService restService;
	@Autowired
	AppConfig appConfig;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.customer.ICustomerManagementController#createCustomer(com.amx.jax
	 * .model.customer.CreateCustomerInfoRequest)
	 */
	@Override
	public AmxApiResponse<CustomerInfo, Object> createCustomer(CreateCustomerInfoRequest createCustomerRequest) throws ParseException {
		try {

			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.CREATE_CUSTOMER).meta(new JaxMetaInfo()).post(createCustomerRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<CustomerInfo, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in createCustomer : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.customer.ICustomerManagementController#updateCustomer(com.amx.jax
	 * .model.request.customer.UpdateCustomerInfoRequest)
	 */
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(UpdateCustomerInfoRequest createCustomerRequest) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.amx.jax.customer.ICustomerManagementController#uploadCustomerKyc(com.amx.
	 * jax.model.customer.UploadCustomerKycRequest)
	 */
	@Override
	public AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(UploadCustomerKycRequest uploadCustomerKycRequest) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.UPLOAD_CUSTOMER_KYC).meta(new JaxMetaInfo()).post(uploadCustomerKycRequest)
					.as(new ParameterizedTypeReference<AmxApiResponse<UploadCustomerKycResponse, Object>>() {
					});
		} catch (Exception ae) {

			LOGGER.error("exception in uploadCustomerKyc : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<DuplicateCustomerDto, Object> checkForDuplicateCustomer(CustomerPersonalDetail customerPersonalDetail) {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.DUPLICATE_CUSTOMER_CHECK).meta(new JaxMetaInfo()).post(customerPersonalDetail)
					.as(new ParameterizedTypeReference<AmxApiResponse<DuplicateCustomerDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in checkForDuplicateCustomer : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<IdentityTypeDto, Object> getIdentityTypes() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.GET_IDENTITY_TPYES).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<IdentityTypeDto, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in getIdentityTypes : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> lockOnlineCustomer() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.LOCK_ONLINE_CUSTOMER).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in lockOnlineCustomer : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

	@Override
	public AmxApiResponse<BoolRespModel, Object> unlockOnlineCustomer() {
		try {
			return restService.ajax(appConfig.getJaxURL()).path(ApiPath.UNLOCK_ONLINE_CUSTOMER).meta(new JaxMetaInfo()).get()
					.as(new ParameterizedTypeReference<AmxApiResponse<BoolRespModel, Object>>() {
					});
		} catch (Exception ae) {
			LOGGER.error("exception in unlockOnlineCustomer : ", ae);
			return JaxSystemError.evaluate(ae);
		}
	}

}
