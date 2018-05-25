package com.amx.jax.userservice.manager;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.model.CustomerCredential;
import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.AppConstants;
import com.amx.jax.cache.CustomerTransactionModel;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.JaxTransactionModel;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.StateMaster;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.trnx.model.OtpData;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.repository.ContactDetailsRepository;
import com.amx.jax.userservice.repository.CustomerRepository;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.utils.ArgUtil;
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

	/**
	 * Initialization of trnx
	 */
	@Override
	public CustomerRegistrationTrnxModel init() {
		CustomerRegistrationTrnxModel model = get();
		if (model == null) {
			model = new CustomerRegistrationTrnxModel();
			model.setOtpData(new OtpData());
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
		return model;
	}

	/**
	 * revalidate the otp data
	 */
	private void revalidateOtp(OtpData otpData) {
		if (!otpData.isOtpValidated()) {
			throw new GlobalException("otp is not validated", JaxError.OTP_NOT_VALIDATED);
		}
	}

	/** commits online cusotmer in db */
	private void commitOnlineCustomer(CustomerRegistrationTrnxModel model, Customer customer) {
		CustomerOnlineRegistration customerOnlineRegistration = new CustomerOnlineRegistration(customer);
		String userName = customerOnlineRegistration.getUserName();
		List<SecurityQuestionModel> secQuestions = model.getSecurityquestions();
		customerDao.setSecurityQuestions(secQuestions, customerOnlineRegistration);
		customerOnlineRegistration.setCaption(cryptoUtil.encrypt(userName, model.getCaption()));
		customerOnlineRegistration.setImageUrl(model.getImageUrl());
		customerOnlineRegistration.setLoginId(model.getCustomerCredential().getLoginId());
		customerOnlineRegistration
				.setPassword(cryptoUtil.getHash(userName, model.getCustomerCredential().getPassword()));
		customerDao.saveOnlineCustomer(customerOnlineRegistration);
	}

	/**
	 * @param customerHomeAddress
	 *            customer home address saves customer contact in db
	 */
	private void commitCustomerContact(CustomerHomeAddress customerHomeAddress, Customer customer) {

		ContactDetail contactDetail = new ContactDetail();
		contactDetail.setFsCountryMaster(new CountryMaster(customerHomeAddress.getCountryId()));
		contactDetail.setFsDistrictMaster(new DistrictMaster(customerHomeAddress.getDistrictId()));
		contactDetail.setFsStateMaster(new StateMaster(customerHomeAddress.getStateId()));
		contactDetail.setMobile(customerHomeAddress.getMobile());
		contactDetail.setFsCustomer(customer);
		contactDetail.setActiveStatus(ConstantDocument.No);
		contactDetailsRepository.save(contactDetail);
	}

	private Customer commitCustomer(CustomerPersonalDetail customerPersonalDetail) {
		Customer customer = new Customer();
		jaxUtil.convert(customerPersonalDetail, customer);
		BigDecimal customerReference = customerDao.generateCustomerReference();
		customer.setCustomerReference(customerReference);
		customer.setIsActive(ConstantDocument.No);
		LOGGER.info("generated customer ref: {}", customerReference);
		LOGGER.info("Createing new customer record, civil id- {}", customerPersonalDetail.getIdentityInt());
		customerRepository.save(customer);
		return customer;
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
		return model;
	}
}