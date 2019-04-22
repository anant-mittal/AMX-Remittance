package com.amx.jax.userservice.service;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.logger.AuditService;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.customer.SecurityQuestionModel;
import com.amx.jax.model.response.customer.PersonInfo;
import com.amx.jax.postman.PostManException;
import com.amx.jax.postman.PostManService;
import com.amx.jax.postman.model.Email;
import com.amx.jax.postman.model.TemplatesMX;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.repository.CustomerRepository;
import com.amx.jax.repository.IBeneficiaryOnlineDao;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.repository.ICustomerRepository;
import com.amx.jax.repository.ITransactionHistroyDAO;
import com.amx.jax.repository.IViewCityDao;
import com.amx.jax.repository.IViewDistrictDAO;
import com.amx.jax.repository.IViewStateDao;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.services.JaxNotificationService;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.repository.LoginLogoutHistoryRepository;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.util.CryptoUtil;
import com.amx.utils.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class FingerprintService {
	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private UserValidationService userValidationService;
	@Autowired
	private LoginLogoutHistoryRepository loginLogoutHistoryRepositoryRepo;

	@Autowired
	IViewCityDao cityDao;

	@Autowired
	IViewStateDao stateDao;

	@Autowired
	CountryRepository countryDao;

	@Autowired
	ICustomerRepository customerDao;

	@Autowired
	IContactDetailDao contactDao;

	@Autowired
	IViewDistrictDAO districtDao;

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;

	@Autowired
	ITransactionHistroyDAO tranxHistDao;

	@Autowired
	MetaData metaData;

	@Autowired
	JaxNotificationService jaxNotificationService;

	@Autowired
	CustomerVerificationService customerVerificationService;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	private CustomerRepository repo;

	@Autowired
	AuditService auditService;
	@Autowired
	CustomerIdProofDao customerIdProofDao;

	@Autowired
	UserService userService;
	
	@Autowired
	private PostManService postManService;
	
	
	
	protected LoginLogoutHistory getLoginLogoutHistoryByUserName(String userName) {

		Sort sort = new Sort(Direction.DESC, "loginLogoutId");
		List<LoginLogoutHistory> last2HistoryList = loginLogoutHistoryRepositoryRepo.findFirst2ByuserName(userName,
				sort);
		LoginLogoutHistory output = null;

		if (last2HistoryList != null && last2HistoryList.size() > 1) {
			output = last2HistoryList.get(1);
		}
		return output;
	}
	
	public CustomerModel convert(CustomerOnlineRegistration cust) {
		CustomerModel model = new CustomerModel();
		model.setIdentityId(cust.getUserName());
		if (cust.getCaption() != null) {
			model.setCaption(cryptoUtil.decrypt(cust.getUserName(), cust.getCaption()));
		}
		model.setEmail(cust.getEmail());
		model.setImageUrl(cust.getImageUrl());
		model.setMobile(cust.getMobileNumber());
		model.setCustomerId(cust.getCustomerId());
		model.setIsActive(ConstantDocument.Yes.equals(cust.getStatus()));
		List<SecurityQuestionModel> securityquestions = new ArrayList<>();
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion1(), cust.getSecurityAnswer1()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion2(), cust.getSecurityAnswer2()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion3(), cust.getSecurityAnswer3()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion4(), cust.getSecurityAnswer4()));
		securityquestions.add(new SecurityQuestionModel(cust.getSecurityQuestion5(), cust.getSecurityAnswer5()));
		model.setSecurityquestions(securityquestions);
		try {
			PersonInfo personinfo = new PersonInfo();
			Customer customer = custDao.getCustById(cust.getCustomerId());
			model.setEmail(customer.getEmail());
			LoginLogoutHistory history = this.getLoginLogoutHistoryByUserName(cust.getUserName());
			if (history != null) {
				personinfo.setLastLoginTime(history.getLoginTime());
			}
			BeanUtils.copyProperties(personinfo, customer);
			personinfo.setEmail(customer.getEmail());
			personinfo.setMobile(customer.getMobile());
			model.setPersoninfo(personinfo);
		} catch (Exception e) {
			logger.error("Exception while populating PersonInfo : ", e);
		}
		return model;
	}

	
	public UserFingerprintResponseModel linkDeviceId(BigDecimal customerId) {
		CustomerOnlineRegistration customerOnlineRegistration = userValidationService
				.validateOnlineCustomerByIdentityId(customerId);
		if(metaData.getDeviceId()==null) {
			logger.error("device id null exception");
			throw new GlobalException("Device id cannot be null");
		}
		String password = Random.randomPassword(6);
		String hashPassword = userService.generateFingerPrintPassword(password);
		UserFingerprintResponseModel userFingerprintResponsemodel = new UserFingerprintResponseModel();
		userFingerprintResponsemodel.setPassword(password);
		customerOnlineRegistration.setFingerprintDeviceId(metaData.getDeviceId());
		customerOnlineRegistration.setDevicePassword(hashPassword);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
		
		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		PersonInfo personinfo = new PersonInfo();
		personinfo.setFirstName(customer.getFirstName());
		personinfo.setMiddleName(customer.getMiddleName());
		personinfo.setLastName(customer.getLastName());
		logger.info("Email to - " + customerOnlineRegistration.getEmail());
		Email email = new Email();
		logger.info("setting to");
		email.addTo(customerOnlineRegistration.getEmail());
		logger.info("setting template");
		email.setITemplate(TemplatesMX.FINGERPRINT_LINKED_SUCCESS);
		logger.info("setting html");
		email.setHtml(true);
		logger.info("setting data");
		email.getModel().put(RESP_DATA_KEY, personinfo);

		logger.info("Email to - " + customerOnlineRegistration.getEmail());
		sendEmail(email);
		return userFingerprintResponsemodel;
	}
	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			logger.info("email sent");
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
			logger.info("email exception");
			logger.error("error in link fingerprint", e);
		}
	}

	public String generateFingerPrintPassword(String password) {
		
		logger.debug("The password is " + password);
		String hashpassword = null;
		try {
			hashpassword = com.amx.utils.CryptoUtil.getSHA2Hash(password);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception thrown for incorrect algorithm ", e);
			throw new GlobalException("Unable to generate fingerprint password");
		}
		return hashpassword;

	}

	public CustomerModel loginCustomerByFingerprint(String civilId, String identityTypeStr, String password, String fingerprintDeviceId) {
		userValidationService.validateIdentityInt(civilId, identityTypeStr);
		if (metaData.getDeviceId() == null) {
			logger.error("device id null exception");
			throw new GlobalException("Device id cannot be null");
		}
		CustomerOnlineRegistration customerOnlineRegistration = null;
		if (identityTypeStr == null) {
			try {
			customerOnlineRegistration = userValidationService.validateOnlineCustomerByIdentityId(civilId,
					new BigDecimal(198));
			}catch(GlobalException e) {}
			if (customerOnlineRegistration == null) {
				customerOnlineRegistration = userValidationService.validateOnlineCustomerByIdentityId(civilId,
						new BigDecimal(2000));
			}
		} else {
			BigDecimal identityType = new BigDecimal(identityTypeStr);
			customerOnlineRegistration = userValidationService.validateOnlineCustomerByIdentityId(civilId,
					identityType);
		}
		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		logger.info("Customer id is " + metaData.getCustomerId());
		userValidationService.validateCustomerLockCount(customerOnlineRegistration);
		userValidationService.validateCustIdProofs(customerOnlineRegistration.getCustomerId());
		userValidationService.validateCustomerData(customerOnlineRegistration, customer);
		userValidationService.validateBlackListedCustomerForLogin(customer);
		userValidationService.validateFingerprintDeviceId(customerOnlineRegistration, fingerprintDeviceId);
		userValidationService.validateDevicePassword(customerOnlineRegistration, password);
		CustomerModel customerModel = convert(customerOnlineRegistration);
		return customerModel;
	}
	
	public BoolRespModel delinkFingerprint() {
		CustomerOnlineRegistration customerOnlineRegistration = custDao.getOnlineCustByCustomerId(metaData.getCustomerId());
		customerOnlineRegistration.setFingerprintDeviceId(null);
		customerOnlineRegistration.setDevicePassword(null);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		PersonInfo personinfo = new PersonInfo();
		personinfo.setFirstName(customer.getFirstName());
		personinfo.setMiddleName(customer.getMiddleName());
		personinfo.setLastName(customer.getLastName());
		logger.info("Checking wether delink has been called or not");
		Email email = new Email();
		email.addTo(customerOnlineRegistration.getEmail());
		email.setITemplate(TemplatesMX.FINGERPRINT_DELINKED_SUCCESS);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, personinfo);
		logger.info("Email to delink fingerprint- " + customerOnlineRegistration.getEmail());
		sendEmail(email);
		return boolRespModel;
	}
	public BoolRespModel resetFingerprint(String identity, String identityTypeStr) {
		userValidationService.validateIdentityInt(identity, identityTypeStr);
		BigDecimal identityType = new BigDecimal(identityTypeStr);
		CustomerOnlineRegistration customerOnlineRegistration = userValidationService
				.validateOnlineCustomerByIdentityId(identity, identityType);
		customerOnlineRegistration.setFingerprintDeviceId(null);
		customerOnlineRegistration.setDevicePassword(null);
		custDao.saveOnlineCustomer(customerOnlineRegistration);
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		Customer customer = custDao.getActiveCustomerByIndentityIntAndType(identity,identityType);
		PersonInfo personinfo = new PersonInfo();
		personinfo.setFirstName(customer.getFirstName());
		personinfo.setMiddleName(customer.getMiddleName());
		personinfo.setLastName(customer.getLastName());
		logger.info("checking wether reset has been called or not");
		Email email = new Email();
		email.addTo(customerOnlineRegistration.getEmail());
		email.setITemplate(TemplatesMX.FINGERPRINT_DELINKED_ATTEMP_SUCCESS);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, personinfo);
		logger.info("Email to reset fingerprint- " + customerOnlineRegistration.getEmail());
		sendEmail(email);
		return boolRespModel;
	
	}
}
