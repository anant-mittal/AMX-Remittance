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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.customer.ICustomerManagementController;
import com.amx.jax.customer.document.manager.CustomerDocMasterManager;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.IdentityTypeDto;
import com.amx.jax.model.customer.document.CustomerDocumentCategoryDto;
import com.amx.jax.model.customer.document.CustomerDocumentTypeDto;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.userservice.manager.OnlineCustomerManager;
import com.amx.libjax.model.jaxfield.JaxFieldDto;
import com.amx.utils.JsonUtil;

@RestController
public class CustomerManagementController implements ICustomerManagementController {

	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerManagementManager customerManagementManager;
	@Autowired
	CustomerService customerService;
	@Autowired
	OnlineCustomerManager onlineCustomerManager;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerDocMasterManager customerDocMasterManager;

	private static final Logger log = LoggerFactory.getLogger(CustomerManagementController.class);

	@RequestMapping(path = CREATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<CustomerInfo, Object> createCustomer(@RequestBody @Valid CreateCustomerInfoRequest createCustomerRequest)
			throws ParseException {
		log.debug("request createCustomer  {}", JsonUtil.toJson(createCustomerRequest));
		AmxApiResponse<CustomerInfo, Object> createCustomerResponse = customerManagementManager.createCustomer(createCustomerRequest);
		customerManagementManager.moveCustomerDataUsingProcedures(createCustomerResponse.getResult().getCustomerId());
		return createCustomerResponse;
	}

	@RequestMapping(path = UPDATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(@RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest) {
		log.debug("request updateCustomer {}", updateCustomerInfoRequest);
		customerManagementManager.updateCustomer(updateCustomerInfoRequest);
		customerManagementManager.moveCustomerDataUsingProcedures();
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

	@RequestMapping(path = UPLOAD_CUSTOMER_DOCUMENT, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<UploadCustomerDocumentResponse, Object> uploadCustomerDocument(
			@RequestBody @Valid UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		log.info("request uploadCustomerDocumentRequest {}", uploadCustomerDocumentRequest);
		UploadCustomerDocumentResponse uploadReference = customerDocumentManager.uploadDocument(uploadCustomerDocumentRequest);
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

	@Override
	@RequestMapping(path = LOCK_ONLINE_CUSTOMER, method = { RequestMethod.GET })
	public AmxApiResponse<BoolRespModel, Object> lockOnlineCustomer() {
		onlineCustomerManager.lockCustomer(metaData.getCustomerId());
		return AmxApiResponse.build();
	}

	@Override
	@RequestMapping(path = UNLOCK_ONLINE_CUSTOMER, method = { RequestMethod.GET })
	public AmxApiResponse<BoolRespModel, Object> unlockOnlineCustomer() {
		onlineCustomerManager.unlockCustomer(metaData.getCustomerId());
		return AmxApiResponse.build();
	}

	@Override
	@RequestMapping(path = DOCUMENT_CATEGORY_GET, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerDocumentCategoryDto, Object> getDocumentCategory() {
		List<CustomerDocumentCategoryDto> list = customerDocMasterManager.getDocumentCategory();
		return AmxApiResponse.buildList(list);
	}

	@Override
	@RequestMapping(path = DOCUMENT_TYPE_GET, method = { RequestMethod.POST })
	public AmxApiResponse<CustomerDocumentTypeDto, Object> getDocumentType(
			@RequestParam(name = ApiParams.DOCUMENT_CATEGORY) String documentCategory) {
		List<CustomerDocumentTypeDto> list = customerDocMasterManager.getDocumentType(documentCategory);
		return AmxApiResponse.buildList(list);
	}

	@Override
	@RequestMapping(path = DOCUMENT_FIELD_GET, method = { RequestMethod.POST })
	public AmxApiResponse<JaxFieldDto, Object> getDocumentFields(@RequestParam(name = ApiParams.DOCUMENT_CATEGORY) String documentCategory,
			@RequestParam(name = ApiParams.DOCUMENT_TYPE) String documentType) {
		List<JaxFieldDto> list = customerDocMasterManager.getDocumentFields(documentCategory, documentType);
		return AmxApiResponse.buildList(list);
	}
}
