package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

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
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppConstants;
import com.amx.jax.CustomerCredential;
import com.amx.jax.cache.CustomerTransactionModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxTransactionModel;
import com.amx.jax.constants.CustomerRegistrationType;
import com.amx.jax.dal.BizcomponentDao;
import com.amx.jax.dbmodel.BizComponentData;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.error.JaxError;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.OtpData;
import com.amx.jax.model.request.CustomerPersonalDetail;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerIdProofRepository;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.userservice.service.UserService;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.ArgUtil;
import com.amx.utils.Constants;
import com.amx.utils.ContextUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRegistrationManager extends CustomerTransactionModel<CustomerRegistrationTrnxModel> {

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

	@Override
	protected String getTranxId() {
		String key = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		if (ArgUtil.isEmptyString(key)) {
			key = getJaxTransactionId();
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, key);
			LOGGER.info("************ Creating New Tranx Id {} *******************", key);
		}
		return super.getTranxId();
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
		custProof.setIdentityTypeId(new BigDecimal(Constants.IDENTITY_TYPE_ID));
		customerIdProofRepository.save(custProof);

	}
}
