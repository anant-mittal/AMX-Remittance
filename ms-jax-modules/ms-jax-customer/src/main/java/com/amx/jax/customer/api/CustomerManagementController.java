package com.amx.jax.customer.api;

import static com.amx.jax.customer.ICustomerManagementController.ApiPath.*;

import java.text.ParseException;
import java.util.List;

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
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.IdentityTypeDto;
import com.amx.jax.model.customer.UploadCustomerKycRequest;
import com.amx.jax.model.customer.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.utils.JsonUtil;

@RestController
public class CustomerManagementController implements ICustomerManagementController {

	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerManagementManager customerManagementManager;
	@Autowired
	CustomerService customerService;

	private static final Logger log = LoggerFactory.getLogger(CustomerManagementController.class);

	@RequestMapping(path = CREATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<CustomerInfo, Object> createCustomer(@RequestBody @Valid CreateCustomerInfoRequest createCustomerRequest)
			throws ParseException {
		log.debug("request createCustomer  {}", JsonUtil.toJson(createCustomerRequest));
		AmxApiResponse<CustomerInfo, Object> createCustomerResponse = customerManagementManager.createCustomer(createCustomerRequest);
		customerManagementManager.moveCustomerDataUsingProcedures(createCustomerResponse);
		return createCustomerResponse;
	}

	@RequestMapping(path = UPDATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(@RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
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

	@Override
	@RequestMapping(path = DUPLICATE_CUSTOMER_CHECK, method = { RequestMethod.POST })
	public AmxApiResponse<DuplicateCustomerDto, Object> checkForDuplicateCustomer(@RequestBody @Valid CustomerPersonalDetail customerPersonalDetail) {
		log.debug("request checkForDuplicateCustomer {}", customerPersonalDetail);
		List<DuplicateCustomerDto> duplicateCustomerDtoList = customerService.checkForDuplicateCustomer(customerPersonalDetail);
		return AmxApiResponse.buildList(duplicateCustomerDtoList);
	}

	@Override
	@RequestMapping(path = GET_IDENTITY_TPYES, method = { RequestMethod.GET })
	public AmxApiResponse<IdentityTypeDto, Object> getIdentityTypes() {
		List<IdentityTypeDto> identityTypesDto = customerService.getIdentityTypes();
		return AmxApiResponse.buildList(identityTypesDto);
	}

}
