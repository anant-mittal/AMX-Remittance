package com.amx.jax.customer.manager;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.customer.service.OffsitCustRegService;
import com.amx.jax.customer.validation.CustomerManagementValidation;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCategoryDiscountModel;
import com.amx.jax.dbmodel.CustomerExtendedModel;
import com.amx.jax.dbmodel.EmployeeDetails;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.request.CustomerEmploymentDetails;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.ICustomerCategoryDiscountRepo;
import com.amx.jax.repository.ICustomerExtendedRepository;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.userservice.service.ContactDetailService;
import com.amx.jax.userservice.service.UserValidationService;

@Component
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
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerManagementManager.class);

	public OffsiteCustomerDataDTO getCustomerDeatils(String identityInt, BigDecimal identityTypeId) {
		OffsiteCustomerDataDTO offsiteCustomer = new OffsiteCustomerDataDTO();
		LOGGER.debug("identityInt :" + identityInt + "\t identityTypeId :" + identityTypeId + "\t country id "
				+ metaData.getCountryId());
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
			offsiteCustomer.setLocalAddressDetails(createLocalAddressDetails(customer));
			offsiteCustomer.setHomeAddressDestails(createHomeAddressDetails(customer));
			offsiteCustomer.setCustomerFlags(customerFlagManager.getCustomerFlags(customer.getCustomerId()));
			offsiteCustomer.setCustomerEmploymentDetails(createCustomerEmploymentDetail(customer));
		} else {
			jaxError = JaxError.CUSTOMER_NOT_FOUND;
		}
		if (jaxError != null) {
			offsiteCustomer.setStatusKey(jaxError.toString());
		}
		return offsiteCustomer;
	}

	private HomeAddressDetails createHomeAddressDetails(Customer customer) {
		// --- Home Address Data
		HomeAddressDetails homeAddress = new HomeAddressDetails();
		ContactDetail homeData = contactDetailService.getContactsForHome(customer);
		if (homeData != null) {
			homeAddress.setContactTypeId(homeData.getFsBizComponentDataByContactTypeId().getComponentDataId());
			homeAddress.setBlock(homeData.getBlock());
			homeAddress.setStreet(homeData.getStreet());
			homeAddress.setHouse(homeData.getBuildingNo());
			homeAddress.setFlat(homeData.getFlat());
			if (null != homeData.getFsCountryMaster()) {
				homeAddress.setCountryId(homeData.getFsCountryMaster().getCountryId());
			}
			if (null != homeData.getFsStateMaster()) {
				homeAddress.setStateId(homeData.getFsStateMaster().getStateId());
			}
			if (null != homeData.getFsDistrictMaster()) {
				homeAddress.setDistrictId(homeData.getFsDistrictMaster().getDistrictId());
			}
			if (null != homeData.getFsCityMaster()) {
				homeAddress.setCityId(homeData.getFsCityMaster().getCityId());
			}
		}
		return homeAddress;
	}

	private LocalAddressDetails createLocalAddressDetails(Customer customer) {
		// --- Local Address Data
		LocalAddressDetails localAddress = new LocalAddressDetails();
		ContactDetail localData = contactDetailService.getContactsForLocal(customer);
		if (localData != null) {
			localAddress.setContactTypeId(localData.getFsBizComponentDataByContactTypeId().getComponentDataId());
			localAddress.setBlock(localData.getBlock());
			localAddress.setStreet(localData.getStreet());
			localAddress.setHouse(localData.getBuildingNo());
			localAddress.setFlat(localData.getFlat());
			if (null != localData.getFsCountryMaster()) {
				localAddress.setCountryId(localData.getFsCountryMaster().getCountryId());
			}
			if (null != localData.getFsStateMaster()) {
				localAddress.setStateId(localData.getFsStateMaster().getStateId());
			}
			if (null != localData.getFsDistrictMaster()) {
				localAddress.setDistrictId(localData.getFsDistrictMaster().getDistrictId());
			}
			if (null != localData.getFsCityMaster()) {
				localAddress.setCityId(localData.getFsCityMaster().getCityId());
			}
		}
		return localAddress;
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

	public CustomerEmploymentDetails createCustomerEmploymentDetail(Customer customer) {
		// --- Customer Employment Data
		CustomerEmploymentDetails employmentDetails = new CustomerEmploymentDetails();
		EmployeeDetails employmentData = customerEmployeeDetailsRepository.getCustomerEmploymentData(customer);
		if (employmentData != null) {
			employmentDetails.setEmployer(employmentData.getEmployerName());
			employmentDetails
					.setEmploymentTypeId(employmentData.getFsBizComponentDataByEmploymentTypeId().getComponentDataId());
			if (employmentData.getFsBizComponentDataByOccupationId() != null) {
				employmentDetails
						.setProfessionId(employmentData.getFsBizComponentDataByOccupationId().getComponentDataId());
			}
			employmentDetails.setStateId(employmentData.getFsStateMaster());
			employmentDetails.setDistrictId(employmentData.getFsDistrictMaster());
			employmentDetails.setCountryId(employmentData.getFsCountryMaster().getCountryId());
			employmentDetails.setArticleDetailsId(customer.getFsArticleDetails().getArticleDetailId());
			employmentDetails.setArticleId(customer.getFsArticleDetails().getFsArticleMaster().getArticleId());
			employmentDetails.setIncomeRangeId(customer.getFsIncomeRangeMaster().getIncomeRangeId());
		}
		return employmentDetails;
	}
}
