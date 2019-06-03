package com.amx.jax.customer.api;

import static com.amx.jax.customer.ICustomerManagementController.ApiPath.*;

import java.text.ParseException;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.customer.ICustomerManagementController;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.model.customer.UploadCustomerKycResponse;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.utils.JsonUtil;

@RestController
public class CustomerManagementController implements ICustomerManagementController {

	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerManagementManager customerManagementManager;

	private static final Logger log = LoggerFactory.getLogger(CustomerManagementController.class);

	@RequestMapping(path = CREATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<BoolRespModel, Object> createCustomer(
			@RequestBody @Valid CreateCustomerInfoRequest createCustomerRequest) throws ParseException {
		log.debug("request createCustomer  {}", JsonUtil.toJson(createCustomerRequest));
		customerManagementManager.createCustomer(createCustomerRequest);
		return AmxApiResponse.build();

	}

	@RequestMapping(path = UPDATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(
			@RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
		log.debug("request updateCustomer {}", updateCustomerInfoRequest);
		return AmxApiResponse.build();

	}

	@RequestMapping(path = UPLOAD_CUSTOMER_KYC, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(
			@RequestBody @Valid UploadCustomerKycRequest uploadCustomerKycRequest) {
		log.info("request uploadCustomerKycRequest {}", uploadCustomerKycRequest);
		UploadCustomerKycResponse uploadReference = customerDocumentManager.uploadKycDocument(uploadCustomerKycRequest);
		return AmxApiResponse.build(uploadReference);

	}
}
