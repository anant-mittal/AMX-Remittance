package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.PrefixEnum;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.jax.CustomerCredential;
import com.amx.jax.cache.TransactionModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxTransactionModel;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerCategoryDiscountModel;
import com.amx.jax.dbmodel.CustomerExtendedModel;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.dbmodel.remittance.IDNumberLengthCheckView;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.ResourceDTO;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.model.request.HomeAddressDetails;
import com.amx.jax.model.request.LocalAddressDetails;
import com.amx.jax.model.response.customer.OffsiteCustomerDataDTO;
import com.amx.jax.repository.CustomerEmployeeDetailsRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.ICustomerCategoryDiscountRepo;
import com.amx.jax.repository.ICustomerExtendedRepository;
import com.amx.jax.repository.remittance.IIdNumberLengthCheckRepository;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.service.ContactDetailService;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.userservice.service.UserValidationService;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.validation.CustomerValidationService;
import com.amx.utils.Constants;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRegistrationManager extends TransactionModel<CustomerRegistrationTrnxModel> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationManager.class);

	private String identityInt;
	@Autowired
	private JaxUtil jaxUtil;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private ContactDetailsRepository contactDetailsRepository;
	@Autowired
	private CustomerDao customerDao;
	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private MetaData jaxMetaInfo;

	@Autowired
	CustomerIdProofRepository customerIdProofRepository;
	@Autowired
	BizcomponentDao bizcomponentDao;
	@Autowired
	UserService userService;
	
	
	@Autowired
	ContactDetailService contactDetailService; 
	
	@Autowired
	CustomerEmployeeDetailsRepository customerEmployeeDetailsRepository;
	
	@Autowired
	IIdNumberLengthCheckRepository idnumberLengthCheckRepos;
	
	@Autowired
	CustomerValidationService customerValidationService;
	
	@Autowired
	MetaData metaData;
	
	@Autowired
	private BlackListDao blistDao;
	
	@Autowired
	UserValidationService userValidationService;
	
	@Autowired
	ICustomerExtendedRepository customerExtendedRepo;
	
	@Autowired
	ICustomerCategoryDiscountRepo customerCategoryRepository;
	
	
	
	@Override
	public CustomerRegistrationTrnxModel getDefault() {
		CustomerRegistrationTrnxModel model = new CustomerRegistrationTrnxModel();
		model.setOtpData(new OtpData());
		return model;
	}

	/**
	 * Initialization of trnx
	 */
	@Override
	public CustomerRegistrationTrnxModel init() {
		CustomerRegistrationTrnxModel model = get();
		if (model == null) {
			model = getDefault();
			save(model);
		}
		return model;
	}

	/**
	 * Init method
	 */
	public CustomerRegistrationTrnxModel init(CustomerPersonalDetail customerPersonalDetail) {
		CustomerRegistrationTrnxModel model = get();
		if (model == null) {
			model = new CustomerRegistrationTrnxModel();
			model.setOtpData(new OtpData());
		}
		model.setCustomerPersonalDetail(customerPersonalDetail);
		save(model);
		return model;
	}

	@Override
	@Transactional
	public CustomerRegistrationTrnxModel commit() {
		CustomerRegistrationTrnxModel model = get();
		revalidateOtp(model.getOtpData());
		Customer customer = commitCustomer(model.getCustomerPersonalDetail());
		commitCustomerContact(model.getCustomerHomeAddress(), customer);
		commitOnlineCustomer(model, customer);
		commitOnlineCustomerIdProof(model, customer);
		return model;
	}

	/**
	 * revalidate the otp data
	 */
	private void revalidateOtp(OtpData otpData) {
		if (!otpData.isOtpValidated()) {
			throw new GlobalException(JaxError.OTP_NOT_VALIDATED, "otp is not validated");
		}
	}

	/** commits online cusotmer in db */
	private void commitOnlineCustomer(CustomerRegistrationTrnxModel model, Customer customer) {
		CustomerOnlineRegistration customerOnlineRegistration = new CustomerOnlineRegistration(customer);
		String userName = customerOnlineRegistration.getUserName();
		List<SecurityQuestionModel> secQuestions = model.getSecurityquestions();
		userService.simplifyAnswers(secQuestions);
		customerDao.setSecurityQuestions(secQuestions, customerOnlineRegistration);
		customerOnlineRegistration.setCaption(cryptoUtil.encrypt(userName, model.getCaption()));
		customerOnlineRegistration.setImageUrl(model.getImageUrl());
		customerOnlineRegistration.setLoginId(model.getCustomerCredential().getLoginId());
		customerOnlineRegistration
				.setPassword(cryptoUtil.getHash(userName, model.getCustomerCredential().getPassword()));
		customerOnlineRegistration.setStatus(ConstantDocument.Yes);
		customerDao.saveOnlineCustomer(customerOnlineRegistration);
	}

	/**
	 * save customer home country contact
	 * 
	 * @param customerHomeAddress
	 *            customer home address saves customer contact in db
	 */
	private void commitCustomerContact(CustomerHomeAddress customerHomeAddress, Customer customer) {
		if (customerHomeAddress != null) {
			ContactDetail contactDetail = new ContactDetail();
			contactDetail.setFsCountryMaster(new CountryMaster(customerHomeAddress.getCountryId()));
			contactDetail.setFsDistrictMaster(new DistrictMaster(customerHomeAddress.getDistrictId()));
			contactDetail.setFsStateMaster(new StateMaster(customerHomeAddress.getStateId()));
			contactDetail.setMobile(customerHomeAddress.getMobile());
			contactDetail.setFsCustomer(customer);
			contactDetail.setActiveStatus(ConstantDocument.Yes);
			contactDetail.setLanguageId(customer.getLanguageId());
			contactDetail.setCreatedBy(customer.getCreatedBy());
			contactDetail.setCreationDate(customer.getCreationDate());
			BizComponentData fsBizComponentDataByContactTypeId = new BizComponentData();
			// home type contact
			fsBizComponentDataByContactTypeId.setComponentDataId(new BigDecimal(50));
			contactDetail.setFsBizComponentDataByContactTypeId(fsBizComponentDataByContactTypeId);
			contactDetailsRepository.save(contactDetail);
		}
	}

	private Customer commitCustomer(CustomerPersonalDetail customerPersonalDetail) {
		Customer customer = new Customer();
		jaxUtil.convert(customerPersonalDetail, customer);
		BigDecimal customerReference = customerDao.generateCustomerReference();
		PrefixEnum prefixEnum = PrefixEnum.getPrefixEnum(customerPersonalDetail.getTitle());
		customer.setCustomerReference(customerReference);
		customer.setIsActive(ConstantDocument.No);
		customer.setCountryId(jaxMetaInfo.getCountryId());
		customer.setCreatedBy(
				jaxMetaInfo.getAppType() != null ? jaxMetaInfo.getAppType() : customerPersonalDetail.getIdentityInt());
		customer.setCreationDate(new Date());
		customer.setIsOnlineUser(ConstantDocument.Yes);
		customer.setGender(prefixEnum.getGender());
		customer.setTitleLocal(getTitleLocal(prefixEnum.getTitleLocal()));
		customer.setLoyaltyPoints(BigDecimal.ZERO);
		customer.setCompanyId(jaxMetaInfo.getCompanyId());
		customer.setCustomerTypeId(
				bizcomponentDao.getBizComponentDataByComponmentCode(ConstantDocument.Individual).getComponentDataId());
		customer.setLanguageId(jaxMetaInfo.getLanguageId());
		customer.setBranchCode(jaxMetaInfo.getCountryBranchId());
		customer.setNationalityId(customerPersonalDetail.getNationalityId());
		customer.setMobile(customerPersonalDetail.getMobile());
		customer.setIdentityFor(ConstantDocument.IDENTITY_FOR_ID_PROOF);
		customer.setIdentityTypeId(ConstantDocument.BIZ_COMPONENT_ID_CIVIL_ID);
		customer.setCustomerRegistrationType(CustomerRegistrationType.PARTIAL_REG);

		LOGGER.info("generated customer ref: {}", customerReference);
		LOGGER.info("Createing new customer record, civil id- {}", customerPersonalDetail.getIdentityInt());
		customerRepository.save(customer);
		return customer;
	}

	private String getTitleLocal(String titleLocal) {
		return bizcomponentDao.getBizComponentDataDescByComponmentId(titleLocal).getDataDesc();
	}

	public String getJaxTransactionId() {
		return JaxTransactionModel.CUSTOMER_REGISTRATION_MODEL.toString() + "_" + identityInt;
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	/**
	 * saves otp data in trnx
	 */
	public void saveOtpData(OtpData otpData) {
		CustomerRegistrationTrnxModel model = get();
		model.setOtpData(otpData);
		save(model);
	}

	/**
	 * save home address in trnx
	 */
	public void saveHomeAddress(CustomerHomeAddress addr) {
		CustomerRegistrationTrnxModel model = get();
		model.setCustomerHomeAddress(addr);
		save(model);
	}

	/**
	 * save customer sec question in trnx
	 */
	public void saveCustomerSecQuestions(List<SecurityQuestionModel> securityquestions) {
		CustomerRegistrationTrnxModel model = get();
		model.setSecurityquestions(securityquestions);
		save(model);
	}

	/**
	 * set phishing image
	 */
	public CustomerRegistrationTrnxModel setPhishingImage(String caption, String imageUrl) {
		CustomerRegistrationTrnxModel model = get();
		model.setCaption(caption);
		model.setImageUrl(imageUrl);
		return model;
	}

	/**
	 * save login details in trnx
	 */
	public CustomerRegistrationTrnxModel saveLoginDetail(CustomerCredential customerCredential) {
		CustomerRegistrationTrnxModel model = get();
		model.setCustomerCredential(customerCredential);
		save(model);
		return model;
	}

	private void commitOnlineCustomerIdProof(CustomerRegistrationTrnxModel model, Customer customer) {
		CustomerIdProof custProof = new CustomerIdProof();

		Customer customerData = new Customer();
		customerData.setCustomerId(customer.getCustomerId());
		custProof.setFsCustomer(customerData);

		custProof.setLanguageId(jaxMetaInfo.getLanguageId());

		BizComponentData customerType = new BizComponentData();
		customerType.setComponentDataId(
				bizcomponentDao.getComponentId(Constants.CUSTOMERTYPE_INDU, jaxMetaInfo.getLanguageId())
						.getFsBizComponentData().getComponentDataId());
		custProof.setFsBizComponentDataByCustomerTypeId(customerType);

		BizComponentData idType = new BizComponentData();
		idType.setComponentDataId(null);
		// custProof.setFsBizComponentDataByIdentityTypeId(idType);

		custProof.setIdentityInt(customer.getIdentityInt());
		custProof.setIdentityStatus(Constants.CUST_ACTIVE_INDICATOR);
		custProof.setCreatedBy(customer.getIdentityInt());
		custProof.setCreationDate(new Date());
		custProof.setIdentityTypeId(new BigDecimal(Constants.IDENTITY_TYPE_CIVIL_ID_STR));
		customerIdProofRepository.save(custProof);

	}
	
	public OffsiteCustomerDataDTO getCustomerDeatils(String  identityInt,BigDecimal identityTypeId) {
		OffsiteCustomerDataDTO offsiteCustomer = new OffsiteCustomerDataDTO();
		CustomerPersonalDetail customerDetails = new CustomerPersonalDetail();
		LOGGER.debug("identityInt :"+identityInt+"\t identityTypeId :"+identityTypeId+"\t country id "+jaxMetaInfo.getCountryId());
		
	/*	if(!customerValidationService.validateIdentityInt(identityInt, metaData.getCountry().getISO2Code(), identityTypeId)) {
			throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,"The minimum length should be:"+minimumLength +" and maximum should be :"+maxLength);
		}*/
		
		
		IDNumberLengthCheckView idnumberLengthCheck=idnumberLengthCheckRepos.findByIDTypeId(identityTypeId);
		if(idnumberLengthCheck!=null) {
			BigDecimal maxLength= idnumberLengthCheck.getiDLength();
			BigDecimal minimumLength=idnumberLengthCheck.getMinIDLength();
			BigDecimal identityIntLength = identityInt==null?BigDecimal.ZERO:new BigDecimal(identityInt.length());
			if(identityIntLength.compareTo(minimumLength)<0 || identityIntLength.compareTo(maxLength)>0) {
				throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,"The minimum length should be:"+minimumLength +" and maximum should be :"+maxLength);
			}
		}else {
			throw new GlobalException(JaxError.ID_TYPE_LENGTH_NOT_DEFINED,"Id length setup is missing  in paramter :"+identityInt +" identityTypeId :"+identityTypeId);
		}
		
		List<Customer> customerList = customerRepository.getCustomerDetails(identityInt,jaxMetaInfo.getCountryId());
		if(!customerList.isEmpty()) {
			Customer customer  = null;
			int custSize=0;
			for(Customer cust :customerList) {
				if(cust!=null && cust.getIsActive().equalsIgnoreCase(ConstantDocument.Yes)) {
					custSize++;
					customer =cust;
				}
			}
			if(custSize>1) {
				throw new GlobalException(JaxError.CUSTOMER_INACTIVE,"Duplicate record found"); 
			}else if(customer==null){
				throw new GlobalException(JaxError.NO_RECORD_FOUND,"The customer does not exist in the system or inactive : "+identityInt);
			}
			
		
			
			if(StringUtils.isBlank(customer.getIsActive()) && customer.getIsActive().equalsIgnoreCase(ConstantDocument.No)) {
				throw new GlobalException(JaxError.CUSTOMER_INACTIVE,"Customer is partialy registed :"+identityInt +"\t identityTypeId :"+identityTypeId);
			}else if(StringUtils.isBlank(customer.getIsActive()) && customer.getIsActive().equalsIgnoreCase(ConstantDocument.Deleted)) {
				throw new GlobalException(JaxError.CUSTOMER_INACTIVE,"Customer is deactivated :"+identityInt +"\t identityTypeId :"+identityTypeId);
			}else if(StringUtils.isBlank(customer.getIsActive()) && customer.getIsActive().equalsIgnoreCase(ConstantDocument.Black)) {
				throw new GlobalException(JaxError.CUSTOMER_INACTIVE,"Customer is blacklisted :"+identityInt +"\t identityTypeId :"+identityTypeId);
			}
			if(customer.getIdentityExpiredDate()!=null && customer.getIdentityExpiredDate().compareTo(new Date()) < 0) {
				throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Id proof has been expired.");
			}
			
			if(customer.getSignatureSpecimenClob()==null){
				throw new GlobalException(JaxError.CUSTOMER_SIGNATURE_UNAVAILABLE,"Customer signature not available.");
			}
			
			boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd())|| "N".equals(customer.getMedicalInsuranceInd()));
			if (!insuranceCheck) {
				throw new GlobalException(JaxError.INVALID_INSURANCE_INDICATOR, "INVALID MEDICAL INSURANCE INDICATOR");
			}
			
			userValidationService.validateCustIdProofs(customer.getCustomerId());
			userValidationService.validateOldEmosData(customer);
			userValidationService.validateCustContact(customer);
			userValidationService.validateBlackListedCustomerForLogin(customer);
			//userValidationService.validateBlackListedCustomer(customer);
			
			
			
			offsiteCustomer.setIdentityInt(customer.getIdentityInt());
			offsiteCustomer.setIdentityTypeId(customer.getIdentityTypeId());
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
			
			offsiteCustomer.setCustomerPersonalDetail(customerDetails);
			
			//--- Local Address Data	
			LocalAddressDetails localAddress = new LocalAddressDetails();
			ContactDetail localData = contactDetailService.getContactsForLocal(customer);
			if(localData != null) {
				localAddress.setContactTypeId(localData.getFsBizComponentDataByContactTypeId().getComponentDataId());
				localAddress.setBlock(localData.getBlock());
				localAddress.setStreet(localData.getStreet());
				localAddress.setHouse(localData.getBuildingNo());
				localAddress.setFlat(localData.getFlat());
				if(null != localData.getFsCountryMaster()) {
					localAddress.setCountryId(localData.getFsCountryMaster().getCountryId());
				}
				if(null != localData.getFsStateMaster()) {
					localAddress.setStateId(localData.getFsStateMaster().getStateId());
				}
				if(null != localData.getFsDistrictMaster()) {
					localAddress.setDistrictId(localData.getFsDistrictMaster().getDistrictId());
				}
				if(null != localData.getFsCityMaster()) {
					localAddress.setCityId(localData.getFsCityMaster().getCityId());
				}
				offsiteCustomer.setLocalAddressDetails(localAddress);
			}
			//--- Home Address Data
			HomeAddressDetails homeAddress = new HomeAddressDetails();
			ContactDetail homeData = contactDetailService.getContactsForHome(customer);
			if(homeData != null) {
				homeAddress.setContactTypeId(homeData.getFsBizComponentDataByContactTypeId().getComponentDataId());
				homeAddress.setBlock(homeData.getBlock());
				homeAddress.setStreet(homeData.getStreet());
				homeAddress.setHouse(homeData.getBuildingNo());
				homeAddress.setFlat(homeData.getFlat());
				if(null != homeData.getFsCountryMaster()) {
					homeAddress.setCountryId(homeData.getFsCountryMaster().getCountryId());
				}
				if(null != homeData.getFsStateMaster()) {
					homeAddress.setStateId(homeData.getFsStateMaster().getStateId());
				}	
				if(null != homeData.getFsDistrictMaster()) {
					homeAddress.setDistrictId(homeData.getFsDistrictMaster().getDistrictId());
				}	
				if(null != homeData.getFsCityMaster()) {
					homeAddress.setCityId(homeData.getFsCityMaster().getCityId());		
				}
				offsiteCustomer.setHomeAddressDetails(homeAddress);
			}
		}else {
			throw new GlobalException(JaxError.NO_RECORD_FOUND,"The customer does not exist in the system :"+identityInt);
		}
		
		return offsiteCustomer;
	}
	
	
private ResourceDTO getCustomerCategory(BigDecimal customerId) {
	ResourceDTO dto = new ResourceDTO();
	CustomerExtendedModel customerExtendedModel =  customerExtendedRepo.findByCustomerId(customerId);
	if(customerExtendedModel != null) {
		CustomerCategoryDiscountModel categorydiscountModel = customerCategoryRepository.findByIdAndIsActive(customerExtendedModel.getCustCatMasterId(),ConstantDocument.Yes);
		
		dto.setResourceId(categorydiscountModel.getId());
		dto.setResourceName(categorydiscountModel.getCustomerCatagory());
	}
	return dto ; 
}


	
}
