package com.amx.jax.controller;

import static com.amx.jax.customer.ICustomerManagementController.ApiPath.CREATE_CUSTOMER;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.DOCUMENT_CATEGORY_GET;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.DOCUMENT_CAT_TYPE_LIST_GET;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.DOCUMENT_FIELD_GET;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.DOCUMENT_TYPE_GET;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.DUPLICATE_CUSTOMER_CHECK;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.GET_CUSTOMER_SHORT_DETAIL;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.GET_IDENTITY_TPYES;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.LOCK_ONLINE_CUSTOMER;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.UNLOCK_ONLINE_CUSTOMER;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.UPDATE_CUSTOMER;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.UPLOAD_CUSTOMER_DOCUMENT;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.UPLOAD_CUSTOMER_KYC;
import static com.amx.jax.customer.ICustomerManagementController.ApiPath.VERIFY_CONTACT;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

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
import com.amx.jax.customer.document.manager.CustomerDocumentUploadManager;
import com.amx.jax.customer.manager.CustomerManagementManager;
import com.amx.jax.customer.manager.CustomerPersonalDetailManager;
import com.amx.jax.customer.service.CustomerManagementService;
import com.amx.jax.customer.service.CustomerService;
import com.amx.jax.dbmodel.customer.CustomerDocumentTypeMaster;
import com.amx.jax.dbmodel.customer.CustomerDocumentUploadReferenceTemp;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.DuplicateCustomerDto;
import com.amx.jax.model.customer.IdentityTypeDto;
import com.amx.jax.model.customer.document.CustomerDocCatTypeDto;
import com.amx.jax.model.customer.document.CustomerDocumentCategoryDto;
import com.amx.jax.model.customer.document.CustomerDocumentTypeDto;
import com.amx.jax.model.customer.document.UploadCustomerDocumentRequest;
import com.amx.jax.model.customer.document.UploadCustomerDocumentResponse;
import com.amx.jax.model.customer.document.UploadCustomerKycRequest;
import com.amx.jax.model.customer.document.UploadCustomerKycResponse;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.VerifyCustomerContactRequest;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.customer.CustomerShortInfo;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.services.NotificationTaskService;
import com.amx.jax.userservice.manager.OnlineCustomerManager;
import com.amx.jax.userservice.service.UserService;
import com.amx.libjax.model.jaxfield.JaxConditionalFieldDto;
import com.amx.libjax.model.jaxfield.JaxFieldDto;
import com.amx.utils.ArgUtil;
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
	@Autowired
	CustomerPersonalDetailManager customerPersonalDetailManager;
	@Autowired
	NotificationTaskService notificationTaskService;
	@Autowired
	CustomerDocumentUploadManager customerDocumentUploadManager;
	@Autowired
	UserService userService;
	@Autowired
	CustomerManagementService customerManagementService;

	private static final Logger log = LoggerFactory.getLogger(CustomerManagementController.class);

	@RequestMapping(path = CREATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<CustomerInfo, Object> createCustomer(@RequestBody @Valid CreateCustomerInfoRequest createCustomerRequest)
			throws ParseException {
		log.debug("request createCustomer  {}", JsonUtil.toJson(createCustomerRequest));
		AmxApiResponse<CustomerInfo, Object> createCustomerResponse = customerManagementManager.createCustomer(createCustomerRequest);
		metaData.setCustomerId(createCustomerResponse.getResult().getCustomerId());
		UpdateCustomerInfoRequest updateInfoRequest = new UpdateCustomerInfoRequest();
		updateInfoRequest.setCalledFromAddApi(true);
		updateCustomer(updateInfoRequest);
		return createCustomerResponse;
	}

	@RequestMapping(path = UPDATE_CUSTOMER, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<BoolRespModel, Object> updateCustomer(@RequestBody @Valid UpdateCustomerInfoRequest updateCustomerInfoRequest)
			throws ParseException {
		log.debug("request updateCustomer {}", JsonUtil.toJson(updateCustomerInfoRequest));
		List<CustomerDocumentTypeMaster> customerTempUploadMasters = customerDocumentUploadManager.fetchCustomerUploadedDocMasterList();
		List<CustomerDocumentUploadReferenceTemp> customerTempUploads = customerDocumentUploadManager.fetchCustomerUploadsTemp();
		customerManagementManager.updateCustomer(updateCustomerInfoRequest);
		notificationTaskService.updateDocUploadNotificationTask(customerTempUploadMasters);
		customerManagementManager.moveCustomerDataUsingProcedures(customerTempUploads);
		return AmxApiResponse.build();
	}

	@RequestMapping(path = UPLOAD_CUSTOMER_KYC, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<UploadCustomerKycResponse, Object> uploadCustomerKyc(
			@RequestBody @Valid UploadCustomerKycRequest uploadCustomerKycRequest) {
		log.info("request uploadCustomerKycRequest {}", JsonUtil.toJson(uploadCustomerKycRequest));
		UploadCustomerKycResponse uploadReference = customerDocumentManager.uploadKycDocument(uploadCustomerKycRequest);
		return AmxApiResponse.build(uploadReference);
	}

	@RequestMapping(path = UPLOAD_CUSTOMER_DOCUMENT, method = { RequestMethod.POST })
	@Override
	public AmxApiResponse<UploadCustomerDocumentResponse, Object> uploadCustomerDocument(
			@RequestBody @Valid UploadCustomerDocumentRequest uploadCustomerDocumentRequest) {
		log.info("request uploadCustomerDocumentRequest {}", JsonUtil.toJson(uploadCustomerDocumentRequest));
		UploadCustomerDocumentResponse uploadReference = customerDocumentManager.uploadDocument(uploadCustomerDocumentRequest);
		return AmxApiResponse.build(uploadReference);
	}

	@Override
	@RequestMapping(path = DUPLICATE_CUSTOMER_CHECK, method = { RequestMethod.POST })
	public AmxApiResponse<DuplicateCustomerDto, Object> checkForDuplicateCustomer(@RequestBody @Valid CustomerPersonalDetail customerPersonalDetail) {
		log.debug("request checkForDuplicateCustomer {}", JsonUtil.toJson(customerPersonalDetail));
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
	public AmxApiResponse<JaxConditionalFieldDto, Object> getDocumentFields(@RequestParam(name = ApiParams.DOCUMENT_CATEGORY) String documentCategory,
			@RequestParam(name = ApiParams.DOCUMENT_TYPE) String documentType) {
		List<JaxFieldDto> list = customerDocMasterManager.getDocumentFields(documentCategory, documentType);
		List<JaxConditionalFieldDto> outputDtoList = list.stream().map(i -> {
			return new JaxConditionalFieldDto(i);
		}).collect(Collectors.toList());
		return AmxApiResponse.buildList(outputDtoList);
	}

	@Override
	@RequestMapping(path = VERIFY_CONTACT, method = { RequestMethod.POST })
	public AmxApiResponse<BoolRespModel, Object> verifyContact(@RequestBody VerifyCustomerContactRequest request) {
		customerPersonalDetailManager.verifyContact(request);
		return AmxApiResponse.build(new BoolRespModel());
	}

	@Override
	@RequestMapping(path = GET_CUSTOMER_SHORT_DETAIL, method = RequestMethod.GET)
	public AmxApiResponse<CustomerShortInfo, Object> getCustomerShortDetail(
			@RequestParam(value = ApiParams.IDENTITY, required = false) String identityInt,
			@RequestParam(value = ApiParams.IDENTITY_TYPE_ID, required = false) BigDecimal identityType,
			@RequestParam(value = ApiParams.CUSTOMER_ID, required = false) BigDecimal customerId) {

		customerManagementService.validateCustomerField(identityInt, identityType, customerId);

		CustomerShortInfo customerShortDetail = null;
		if (!ArgUtil.isEmpty(customerId)) {
			PersonInfo personInfo = userService.getPersonInfo(customerId);
			String identityIntByCustId = personInfo.getIdentityInt();
			customerShortDetail = customerManagementManager.getCustomerShortDetail(identityIntByCustId, personInfo.getIdentityTypeId());

		} else {
			customerShortDetail = customerManagementManager.getCustomerShortDetail(identityInt, identityType);
		}

		return AmxApiResponse.build(customerShortDetail);
	}

	@Override
	@RequestMapping(path = DOCUMENT_CAT_TYPE_LIST_GET, method = { RequestMethod.GET })
	public AmxApiResponse<CustomerDocCatTypeDto, Object> listDocCatType() {
		List<CustomerDocCatTypeDto> resultList = customerDocMasterManager.listDocCatType();
		return AmxApiResponse.buildList(resultList);
	}
}
