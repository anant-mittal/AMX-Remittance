package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.customer.document.manager.CustomerDocumentManager;
import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.customer.validation.CustomerManagementValidation;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCategoryDiscountModel;
import com.amx.jax.dbmodel.CustomerExtendedModel;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.customer.CreateCustomerInfoRequest;
import com.amx.jax.model.customer.CustomerStatusModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.customer.UpdateCustomerInfoRequest;
import com.amx.jax.model.response.CustomerInfo;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.repository.ICustomerCategoryDiscountRepo;
import com.amx.jax.repository.ICustomerExtendedRepository;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.userservice.manager.OnlineCustomerManager;
import com.amx.jax.userservice.service.ContactDetailService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerManagementManager {

	@Autowired
	IIdNumberLengthCheckRepository idnumberLengthCheckRepos;
	@Autowired
	MetaData metaData;
	@Autowired
	CustomerManagementValidation customerManagementValidation;
	@Autowired
	UserValidationService userValidationService;
	@Autowired
	ICustomerExtendedRepository customerExtendedRepo;
	@Autowired
	ICustomerCategoryDiscountRepo customerCategoryRepository;
	@Autowired
	ContactDetailService contactDetailService;
	@Autowired
	CustomerFlagManager customerFlagManager;
	@Autowired
	OffsitCustRegService offsitCustRegService;
	@Autowired
	CustomerDocumentManager customerDocumentManager;
	@Autowired
	CustomerEmployementManager customerEmployementManager;
	@Autowired
	UserService userService;
	@Autowired
	OnlineCustomerManager onlineCustomerManager;
	@Autowired
	CustomerDao custDao;
	@Autowired
	CustomerUpdateManager customerUpdateManager;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagementManager.class);

	public OffsiteCustomerDataDTO getCustomerDeatils(String identityInt, BigDecimal identityTypeId) {
		OffsiteCustomerDataDTO offsiteCustomer = new OffsiteCustomerDataDTO();
		LOGGER.debug("identityInt :" + identityInt + "\t identityTypeId :" + identityTypeId + "\t country id " + metaData.getCountryId());
		customerManagementValidation.validateIdentityIntLength(identityInt, identityTypeId);
		List<Customer> customerList = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(identityInt,
				JaxApiFlow.OFFSITE_REGISTRATION);
		JaxError jaxError = null;
		if (!customerList.isEmpty()) {
			Customer customer = customerList.get(0);
			jaxError = getJaxErrorForCustomer(customer);
			userValidationService.validateBlackListedCustomerForLogin(customer);
			if (ConstantDocument.Yes.equals(customer.getIsActive())) {
				userValidationService.validateOldEmosData(customer);
			}

			offsiteCustomer.setIdentityInt(customer.getIdentityInt());
			offsiteCustomer.setIdentityTypeId(customer.getIdentityTypeId());
			offsiteCustomer.setCustomerPersonalDetail(createCustomerPersonalDetail(customer));
			offsiteCustomer.setLocalAddressDetails(contactDetailService.createLocalAddressDetails(customer));
			offsiteCustomer.setHomeAddressDetails(contactDetailService.createHomeAddressDetails(customer));
			offsiteCustomer.setCustomerFlags(customerFlagManager.getCustomerFlags(customer.getCustomerId()));
			offsiteCustomer.setCustomerEmploymentDetails(customerEmployementManager.createCustomerEmploymentDetail(customer));
			offsiteCustomer.setCustomerDocuments(customerDocumentManager.getCustomerUploadDocuments(customer.getCustomerId()));
		} else {
			jaxError = JaxError.CUSTOMER_NOT_FOUND;
		}
		if (jaxError != null) {
			offsiteCustomer.setStatusKey(jaxError.toString());
		}
		return offsiteCustomer;
	}

	private CustomerPersonalDetail createCustomerPersonalDetail(Customer customer) {
		CustomerPersonalDetail customerDetails = new CustomerPersonalDetail();
		customerDetails.setCustomerId(customer.getCustomerId());
		customerDetails.setCountryId(customer.getCountryId());
		customerDetails.setNationalityId(customer.getNationalityId());
		customerDetails.setIdentityInt(customer.getIdentityInt());
		customerDetails.setTitle(customer.getTitle());
		customerDetails.setFirstName(customer.getFirstName());
		customerDetails.setLastName(customer.getLastName());
		customerDetails.setEmail(customer.getEmail());
		customerDetails.setMobile(customer.getMobile());
		customerDetails.setTelPrefix(customer.getPrefixCodeMobile());
		customerDetails.setFirstNameLocal(customer.getFirstNameLocal());
		customerDetails.setLastNameLocal(customer.getLastNameLocal());
		customerDetails.setExpiryDate(customer.getIdentityExpiredDate());
		customerDetails.setDateOfBirth(customer.getDateOfBirth());
		customerDetails.setIdentityTypeId(customer.getIdentityTypeId());
		customerDetails.setInsurance(customer.getMedicalInsuranceInd());
		customerDetails.setWatsAppMobileNo(customer.getMobileOther());
		customerDetails.setWatsAppTelePrefix(customer.getPrefixCodeMobileOther());
		customerDetails.setIsWatsApp(customer.getIsMobileWhatsApp());
		customerDetails.setRegistrationType(customer.getCustomerRegistrationType());
		customerDetails.setCustomerSignature(customer.getSignatureSpecimenClob());
		customerDetails.setCustomerCategory(getCustomerCategory(customer.getCustomerId()));
		return customerDetails;
	}

	private JaxError getJaxErrorForCustomer(Customer customer) {
		String isActive = customer.getIsActive();
		JaxError jaxError = null;
		if (ConstantDocument.No.equals(isActive)) {
			jaxError = JaxError.CUSTOMER_INACTIVE;
		}
		if (ConstantDocument.Deleted.equals(isActive)) {
			jaxError = JaxError.CUSTOMER_DEACTIVATED;
		}
		if (ConstantDocument.Black.equals(isActive)) {
			jaxError = JaxError.CUSTOMER_BLACK_LISTED;
		}

		try {
			userValidationService.validateCustIdProofs(customer.getCustomerId());
			userValidationService.validateCustContact(customer);
			customerManagementValidation.validateCustomerRecord(customer);
		} catch (GlobalException ex) {
			jaxError = (JaxError) ex.getError();
		}
		return jaxError;
	}

	private ResourceDTO getCustomerCategory(BigDecimal customerId) {
		ResourceDTO dto = new ResourceDTO();
		CustomerExtendedModel customerExtendedModel = customerExtendedRepo.findByCustomerId(customerId);
		if (customerExtendedModel != null) {
			CustomerCategoryDiscountModel categorydiscountModel = customerCategoryRepository
					.findByIdAndIsActive(customerExtendedModel.getCustCatMasterId(), ConstantDocument.Yes);

			dto.setResourceId(categorydiscountModel.getId());
			dto.setResourceName(categorydiscountModel.getCustomerCatagory());
		}
		return dto;
	}

	public CustomerStatusModel getCustomerStatusModel(BigDecimal customerId) {
		return getCustomerStatusModel(userService.getCustById(customerId));
	}

	public CustomerStatusModel getCustomerStatusModel(Customer customer) {
		CustomerStatusModel customerStatusModel = new CustomerStatusModel();
		if (ConstantDocument.Yes.equals(customer.getIsActive())) {
			customerStatusModel.setActiveBranch(true);
		}
		CustomerOnlineRegistration onlineCustomer = onlineCustomerManager.getOnlineCustomerByCustomerId(customer.getCustomerId());
		if (onlineCustomer != null && ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			customerStatusModel.setActiveOnline(true);
		}
		if (onlineCustomer != null && onlineCustomer.getLockDt() != null) {
			customerStatusModel.setLockedOnline(true);
		}
		return customerStatusModel;
	}

	@Transactional
	public AmxApiResponse<CustomerInfo, Object> createCustomer(CreateCustomerInfoRequest createCustomerInfoRequest) throws ParseException {
		customerManagementValidation.validateCustomerDataForCreate(createCustomerInfoRequest);
		customerManagementValidation.validateDocumentsData(createCustomerInfoRequest);
		AmxApiResponse<CustomerInfo, Object> response = offsitCustRegService.saveCustomerInfo(createCustomerInfoRequest);
		BigDecimal customerId = response.getResult().getCustomerId();
		setAdditionalDataForCreateCustomer(createCustomerInfoRequest, customerId);
		return response;
	}

	private void setAdditionalDataForCreateCustomer(CreateCustomerInfoRequest createCustomerInfoRequest, BigDecimal customerId) {
		Customer customer = custDao.getCustById(customerId);
		customer.setCustomerRegistrationType(CustomerRegistrationType.NEW_BRANCH);
		customer.setPepsIndicator(createCustomerInfoRequest.getPepsIndicator() ? ConstantDocument.Yes : ConstantDocument.No);
		customer.setIsOnlineUser(ConstantDocument.No);
		custDao.saveCustomer(customer);

	}

	public void moveCustomerDataUsingProcedures(BigDecimal customerId) {
		customerDocumentManager.moveCustomerDBDocuments(customerId);
		custDao.callProcedurePopulateCusmas(customerId);
	}

	public void moveCustomerDataUsingProcedures() {
		moveCustomerDataUsingProcedures(metaData.getCustomerId());
	}

	public void updateCustomer(UpdateCustomerInfoRequest updateCustomerInfoRequest) throws ParseException {
		customerManagementValidation.validateDocumentsData(updateCustomerInfoRequest);
		customerUpdateManager.updateCustomer(updateCustomerInfoRequest);
	}

}
