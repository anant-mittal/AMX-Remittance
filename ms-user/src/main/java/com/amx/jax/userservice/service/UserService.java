package com.amx.jax.userservice.service;

import static com.amx.amxlib.constant.NotificationConstants.RESP_DATA_KEY;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.amxlib.exception.jax.InvalidJsonInputException;
import com.amx.amxlib.exception.jax.InvalidOtpException;
import com.amx.amxlib.exception.jax.UserNotFoundException;
import com.amx.amxlib.meta.model.BeneficiaryListDTO;
import com.amx.amxlib.meta.model.CustomerDto;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserFingerprintResponseModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.JaxAuthCache;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.api.BoolRespModel;
import com.amx.jax.async.ExecutorConfig;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dbmodel.BenificiaryListView;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CountryMasterView;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerRemittanceTransactionView;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dbmodel.DistrictMaster;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.dbmodel.ViewCity;
import com.amx.jax.dbmodel.ViewDistrict;
import com.amx.jax.dbmodel.ViewState;
import com.amx.jax.error.JaxError;
import com.amx.jax.logger.AuditEvent.Result;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.events.CActivityEvent;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.model.response.customer.CustomerFlags;
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
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.manager.CustomerFlagManager;
import com.amx.jax.userservice.manager.SecurityQuestionsManager;
import com.amx.jax.userservice.repository.LoginLogoutHistoryRepository;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.StringUtil;
import com.amx.utils.Random;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class UserService extends AbstractUserService {

	private static final String FIELD_LOCK = "LOCK";

	private static final String FIELD_STATUS = "STATUS";

	private static final String FIELD_VALUE_HIDDEN = "*";

	private static final String FIELD_IMAGE = "IMAGE";

	private static final String FIELD_EMAIL = "EMAIL";

	private static final String FIELD_SECQ = "SECQ";

	private static final String FIELD_CAPTION = "CAPTION";

	private static final String FIELD_LOGINID = "LOGINID";

	private static final String FIELD_PSSWRD = "PSSWRD";

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private JaxUtil util;

	@Autowired
	private CheckListManager checkListManager;

	@Autowired
	private UserValidationService userValidationService;

	@Autowired
	private SecurityQuestionsManager secQmanager;

	@Autowired
	private StringUtil stringUtil;

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
	JaxAuthCache jaxAuthCache;
	
	@Autowired
	UserService userService;
	
	@Autowired
	PostManService postManService;
	@Autowired
	CustomerFlagManager customerFlagManager;
	

	@Override
	public ApiResponse registerUser(AbstractUserModel userModel) {
		UserModel kwUserModel = (UserModel) userModel;
		Customer cust = getDao().saveOrUpdateUser(kwUserModel);
		AbstractModel model = convert(cust);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	@Override
	public AbstractUserDao getDao() {
		return null;
	}

	@Override
	public AbstractModel convert(Customer cust) {
		UserModel model = new UserModel();
		return model;
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
			model = populateFlags(model, customer);
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

	public CustomerModel populateFlags(CustomerModel customerModel, Customer customer) {
		Date annualIncomeUpdateDate = customer.getAnnualIncomeUpdatedDate();

		CustomerFlags flags = new CustomerFlags();
		customerModel.setFlags(flags);

		if (annualIncomeUpdateDate == null) {
			customerModel.getFlags().setAnnualIncomeExpired(Boolean.TRUE);
			logger.info("Flag value is " + customerModel.getFlags().getAnnualIncomeExpired());
			return customerModel;
		}
		Date currentDate = new Date();
		long millisec = currentDate.getTime() - annualIncomeUpdateDate.getTime();
		long milliSecInYear = 31540000000L;

		if (millisec >= milliSecInYear) {
			customerModel.getFlags().setAnnualIncomeExpired(Boolean.TRUE);
			logger.info("Flag value isss " + customerModel.getFlags().getAnnualIncomeExpired());
		} else {
			customerModel.getFlags().setAnnualIncomeExpired(Boolean.FALSE);
			logger.info("Flag value isssss " + customerModel.getFlags().getAnnualIncomeExpired());
		}

		return customerModel;
	}

	public ApiResponse saveCustomer(CustomerModel model) {
		BigDecimal customerId = (model.getCustomerId() == null) ? metaData.getCustomerId() : model.getCustomerId();
		if (customerId == null) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID.getCode(), "Null customer id passed ");
		}
		Customer cust = custDao.getCustById(customerId);
		String oldEmail = cust.getEmail();
		String oldMobile = cust.getMobile();

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustomerByCustomerId(customerId);
		if (onlineCust == null) {
			throw new UserNotFoundException("Customer is not registered for online flow");
		}
		if (model.getLoginId() != null) {
			userValidationService.validateLoginId(model.getLoginId());
		}
		userValidationService.validateOtpFlow(model);

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE);
		// Audit
		addMyProfileAuditLog(auditEvent, model, cust, onlineCust);

		simplifyAnswers(model.getSecurityquestions());
		onlineCust = custDao.saveOrUpdateOnlineCustomer(onlineCust, model);
		updateCustomerVerification(onlineCust, model, cust);
		setCustomerStatus(onlineCust, model, cust);
		checkListManager.updateCustomerChecks(onlineCust, model);
		ApiResponse response = getBlackApiResponse();

		if (model.getLoginId() != null || model.getPassword() != null) { // after this step flow is going to login
			afterLoginSteps(onlineCust);
		}

		CustomerModel outputModel = convert(onlineCust);

		response.getData().getValues().add(outputModel);
		response.getData().setType(outputModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		// this is to send email on OLD email id
		if (model.getEmail() != null) {
			model.setEmail(oldEmail);
		}

		if (isNewUserRegistrationSuccess(model, onlineCust)) {
			jaxNotificationService.sendNewRegistrationSuccessEmailNotification(outputModel.getPersoninfo(),
					onlineCust.getEmail());
		} else {
			jaxNotificationService.sendProfileChangeNotificationEmail(model, outputModel.getPersoninfo());
			jaxNotificationService.sendProfileChangeNotificationMobile(model, outputModel.getPersoninfo(), oldMobile);
		}
		auditService.log(auditEvent.result(Result.DONE));
		return response;
	}

	private void updateCustomerVerification(CustomerOnlineRegistration onlineCust, CustomerModel model, Customer cust) {
		CustomerVerification cv = customerVerificationService.getVerification(cust, CustomerVerificationType.EMAIL);
		if (cv != null) {
			logger.info("customer verification found with status = " + cv.getVerificationStatus());
			customerVerificationService.updateVerification(cust, CustomerVerificationType.EMAIL, model.getEmail());
			if (model.getEmail() != null && ConstantDocument.Yes.equals(cv.getVerificationStatus())) {
				updateEmail(cust.getCustomerId(), model.getEmail());
			}
		}

	}

	// password not null flag indicates it is final step in registration
	private void setCustomerStatus(CustomerOnlineRegistration onlineCust, CustomerModel model, Customer cust) {
		if (model.getPassword() != null) {
			CustomerVerification cv = customerVerificationService.getVerification(cust, CustomerVerificationType.EMAIL);

			if (cv != null && cv.getFieldValue() != null && !ConstantDocument.Yes.equals(cv.getVerificationStatus())) {
				throw new GlobalException(
						JaxError.USER_DATA_VERIFICATION_PENDING,
						"Thank you for registration, Our helpdesk will get in touch with you in 48 hours");
			}

			onlineCust.setStatus(ConstantDocument.Yes);
			custDao.saveOnlineCustomer(onlineCust);
		}

	}

	private boolean isNewUserRegistrationSuccess(CustomerModel model, CustomerOnlineRegistration onlineCust) {

		if (model.getPassword() != null && ConstantDocument.Yes.equals(onlineCust.getStatus())) {
			return true;
		}
		return false;
	}

	public void simplifyAnswers(List<SecurityQuestionModel> securityquestions) {
		if (securityquestions != null && !securityquestions.isEmpty()) {
			securityquestions.forEach(qa -> qa.setAnswer(stringUtil.simplifyString(qa.getAnswer())));
		}

	}

	@Override
	public Class<UserModel> getModelClass() {
		return UserModel.class;
	}

	public CustomerOnlineRegistration verifyCivilId(String civilId, CivilIdOtpModel model) {
		Customer cust = userValidationService.validateCustomerForOnlineFlow(civilId);

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(cust.getCustomerId());
		if (onlineCust == null) {
			logger.info("registering new customer for online device IP: " + metaData.getDeviceIp());
			logger.info("device id: " + metaData.getDeviceId());
			onlineCust = new CustomerOnlineRegistration(cust);
			onlineCust.setHresetBy(cust.getIdentityInt());
			onlineCust.setHresetIp(metaData.getDeviceIp());
			onlineCust.setHresetkDt(new Date());
			onlineCust.setResetIp(metaData.getDeviceIp());
		}
		model.setEmail(cust.getEmail());
		model.setMobile(cust.getMobile());
		model.setIsActiveCustomer(ConstantDocument.Yes.equals(onlineCust.getStatus()) ? true : false);
		return onlineCust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		return sendOtpForCivilId(civilId, null, null, null);
	}

	public ApiResponse sendOtpForCivilId(String civilId, List<CommunicationChannel> channels,
			CustomerModel customerModel, Boolean initRegistration) {
		if (StringUtils.isNotBlank(civilId)) {
			if(tenantContext.getKey().equals("OMN")) {
				tenantContext.get().validateCivilId(civilId);
			}
		}
		BigDecimal customerId = metaData.getCustomerId();
		if (customerId != null) {
			civilId = custDao.getCustById(customerId).getIdentityInt();
		}
		if (customerId == null && civilId != null) {
			userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(civilId, JaxApiFlow.SIGNUP_ONLINE);
			Customer customer = custDao.getCustomerByCivilId(civilId);
			if (customer == null && !Boolean.TRUE.equals(initRegistration)) {
				throw new GlobalException(JaxError.INVALID_CIVIL_ID, "Invalid civil Id passed");
			}
			if (customer != null) {
				customerId = customer.getCustomerId();
			}

			// ---- check for blacklisted customer ----
			userValidationService.validateBlackListedCustomerForLogin(customer);
		}
		logger.info("customerId is --> " + customerId);
		userValidationService.validateCustomerVerification(customerId);
		//userValidationService.validateCivilId(civilId);
		
		// --- Validate IdentityInt 
		Customer customerType = custDao.getCustomerByCivilId(civilId);
		if (null != customerType) {
			BigDecimal indentityType = customerType.getIdentityTypeId();
		userValidationService.validateIdentityInt(civilId, indentityType);
		}

		CivilIdOtpModel model = new CivilIdOtpModel();

		CustomerOnlineRegistration onlineCustReg = custDao.getOnlineCustByCustomerId(customerId);

		if (onlineCustReg != null) {
			logger.info("validating customer lock count.");
			userValidationService.validateCustomerLockCount(onlineCustReg);
		} else {
			logger.info("onlineCustReg is null");
		}

		CustomerOnlineRegistration onlineCust = verifyCivilId(civilId, model);
		userValidationService.validateActiveCustomer(onlineCustReg, initRegistration);

		try {
			userValidationService.validateTokenDate(onlineCust);
		} catch (GlobalException e) {
			// reset sent token count
			onlineCust.setTokenSentCount(BigDecimal.ZERO);
		}
		// userValidationService.validateCustomerLockCount(onlineCust);
		userValidationService.validateTokenSentCount(onlineCust);
		generateToken(civilId, model, channels);
		onlineCust.setEmailToken(model.getHashedeOtp());
		onlineCust.setSmsToken(model.getHashedmOtp());
		onlineCust.setTokenDate(new Date());
		BigDecimal tokenSentCount = (onlineCust.getTokenSentCount() == null) ? BigDecimal.ZERO
				: onlineCust.getTokenSentCount().add(new BigDecimal(1));
		onlineCust.setTokenSentCount(tokenSentCount);
		custDao.saveOnlineCustomer(onlineCust);
		Customer customer = custDao.getCustById(onlineCust.getCustomerId());
		model.setFirstName(customer.getFirstName());
		model.setLastName(customer.getLastName());
		model.setCustomerId(onlineCust.getCustomerId());
		model.setMiddleName(customer.getMiddleName());
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		// if user is already registered do not send OTP
		if (initRegistration != null && initRegistration && onlineCust != null
				&& ConstantDocument.Yes.equals(onlineCust.getStatus())) {
			logger.info(String.format("Customer %s -- %s is already registred.", model.getCustomerId(),
					model.getFirstName()));
			return response;
		}

		PersonInfo personinfo = new PersonInfo();
		try {
			BeanUtils.copyProperties(personinfo, customer);
		} catch (Exception e) {
		}

		if (customerModel != null && customerModel.getEmail() != null) {
			personinfo.setEmail(customerModel.getEmail());
		}
		if (customerModel != null && customerModel.getMobile() != null) {
			personinfo.setMobile(customerModel.getMobile());
		}

		jaxNotificationService.sendOtpSms(personinfo, model);

		if (channels != null && (channels.contains(CommunicationChannel.EMAIL)
				|| channels.contains(CommunicationChannel.EMAIL_AS_MOBILE))) {
			jaxNotificationService.sendOtpEmail(personinfo, model);
		}
		return response;
	}

	public void generateToken(String userId, CivilIdOtpModel model, List<CommunicationChannel> channels) {
		String randmOtp = util.createRandomPassword(6);
		String hashedmOtp = cryptoUtil.getHash(userId, randmOtp);
		String randeOtp = util.createRandomPassword(6);
		String hashedeOtp = cryptoUtil.getHash(userId, randeOtp);
		model.setHashedmOtp(hashedmOtp);
		model.setmOtp(randmOtp);
		model.setmOtpPrefix(Random.randomAlpha(3));
		if (channels != null && channels.contains(CommunicationChannel.EMAIL)) {
			model.setHashedeOtp(hashedeOtp);
			model.seteOtp(randeOtp);
			model.seteOtpPrefix(Random.randomAlpha(3));
			logger.info("Generated otp for civilid email- " + userId + " is " + randeOtp);
		}

		// set e-otp same as m-otp
		if (channels != null && channels.contains(CommunicationChannel.EMAIL_AS_MOBILE)) {
			model.setHashedeOtp(hashedmOtp);
			model.seteOtp(randmOtp);
			model.seteOtpPrefix(model.getmOtpPrefix());
			logger.info("Generated otp for civilid email- " + userId + " is " + randmOtp);
		}

		/*JaxAuthMeta jaxAuthMeta = jaxAuthCache.getOrDefault(metaData.getCustomerId().toString(), new JaxAuthMeta());
		jaxAuthMeta.seteOtp(randeOtp);
		jaxAuthMeta.setmOtp(randmOtp);
		jaxAuthCache.fastPut(metaData.getCustomerId().toString(), jaxAuthMeta);*/

		logger.info("Generated otp for civilid mobile- " + userId + " is " + randmOtp);
	}

	public ApiResponse validateOtp(String civilId, String mOtp) {
		return validateOtp(civilId, mOtp, null);
	}

	/**
	 * validate otp
	 * 
	 * @param civilId
	 * @param mOtp
	 * @param eOtp
	 * @return apiresponse
	 */
	public ApiResponse validateOtp(String civilId, String mOtp, String eOtp) {
		logger.info("in validateopt of civilid: " + civilId);
		Customer customer = null;
		if (civilId != null) {
			customer = custDao.getCustomerByCivilId(civilId);
		}
		if (metaData.getCustomerId() != null) {
			customer = custDao.getCustById(metaData.getCustomerId());
			civilId = customer.getIdentityInt();
		}
		if (customer == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCust == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		if (StringUtils.isEmpty(mOtp)) {
			throw new InvalidJsonInputException("Otp is empty for civil-id: " + civilId);
		}
		userValidationService.validateCustomerLockCount(onlineCust);
		userValidationService.validateTokenDate(onlineCust);
		String etokenHash = onlineCust.getEmailToken();
		String mtokenHash = onlineCust.getSmsToken();
		String mOtpHash = cryptoUtil.getHash(civilId, mOtp);
		String eOtpHash = null;
		if (StringUtils.isNotBlank(eOtp)) {
			eOtpHash = cryptoUtil.getHash(civilId, eOtp);
		}
		if (!mOtpHash.equals(mtokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new InvalidOtpException("Sms Otp is incorrect for civil-id: " + civilId);
		}
		if (eOtpHash != null && !eOtpHash.equals(etokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new InvalidOtpException("Email Otp is incorrect for civil-id: " + civilId);
		}
		checkListManager.updateMobileAndEmailCheck(onlineCust, custDao.getCheckListForUserId(civilId));
		this.unlockCustomer(onlineCust);
		custDao.saveOnlineCustomer(onlineCust);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCust);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.info("end of validateopt for civilid: " + civilId);
		return response;
	}

	public ApiResponse loginUser(String userId, String password) {
		if(tenantContext.getKey().equals("OMN")) {
			tenantContext.get().validateCivilId(userId);
		}	
		List<Customer> validCustomer = userValidationService.validateNonActiveOrNonRegisteredCustomerStatus(userId,
				JaxApiFlow.LOGIN);
		CustomerOnlineRegistration onlineCustomer = custDao
				.getOnlineCustByCustomerId(validCustomer.get(0).getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.USER_NOT_REGISTERED,
					"User with userId: " + userId + " is not registered");
		}
		Customer customer = custDao.getCustById(onlineCustomer.getCustomerId());
		// userValidationService.validateCustomerVerification(onlineCustomer.getCustomerId());
		if (!ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException(JaxError.USER_NOT_REGISTERED,
					"User with userId: " + userId + " is not registered or not active");
		}

		userValidationService.validateCustomerLockCount(onlineCustomer);
		userValidationService.validatePassword(onlineCustomer, password);
		userValidationService.validateCustIdProofs(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerData(onlineCustomer, customer);
		userValidationService.validateBlackListedCustomerForLogin(customer);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCustomer);
		// afterLoginSteps(onlineCustomer);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * call this method to perform tasks after login
	 */
	private void afterLoginSteps(CustomerOnlineRegistration onlineCustomer) {
		custDao.updatetLoyaltyPoint(onlineCustomer.getCustomerId());
		this.unlockCustomer(onlineCustomer);
		this.saveLoginLogoutHistoryByUserName(onlineCustomer.getUserName());
	}

	public ApiResponse getUserCheckList(String loginId) {
		UserVerificationCheckListDTO model = checkListManager.getUserCheckList(loginId);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.info("end of getUserCheckList for loginId: " + loginId);
		return response;

	}

	public ApiResponse generateRandomQuestions(Integer size, Integer customerId) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(new BigDecimal(customerId));
		ApiResponse response = getBlackApiResponse();
		List<QuestModelDTO> result = secQmanager.generateRandomQuestions(onlineCustomer, size, customerId);
		response.getData().getValues().addAll(result);
		response.getData().setType("quest");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	@SuppressWarnings("unchecked")
	public ApiResponse<QuestModelDTO> getDataVerificationRandomQuestions(Integer size) {
		BigDecimal customerId = metaData.getCustomerId();
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);
		ApiResponse<QuestModelDTO> response = getBlackApiResponse();
		List<QuestModelDTO> result = secQmanager.getDataVerificationRandomQuestions(onlineCustomer, size, customerId);
		response.getData().getValues().addAll(result);
		response.getData().setType("quest");
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	public ApiResponse validateCustomerData(CustomerModel model) {
		if (model.getCustomerId() == null) {
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID.getCode(), "Null customer id passed ");
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(model.getCustomerId());
		ApiResponse response = getBlackApiResponse();

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.VALIDATION);

		try {
			userValidationService.validateCustomerLockCount(onlineCustomer);
		} catch (GlobalException e) {
			auditService.log(
					auditEvent.result(Result.FAIL).message(e.getError()));
			throw e;
		}
		// commented trailing s and special characters removal
		simplifyAnswers(model.getSecurityquestions());
		try {
			userValidationService.validateCustomerSecurityQuestions(model.getSecurityquestions(), onlineCustomer);
		} catch (GlobalException e) {
			auditService.log(
					auditEvent.result(Result.FAIL).message(e.getError()));
			throw e;
		}
		this.unlockCustomer(onlineCustomer);
		afterLoginSteps(onlineCustomer);
		CustomerModel responseModel = convert(onlineCustomer);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		auditService.log(auditEvent.result(Result.DONE));
		return response;
	}

	public AmxApiResponse<BoolRespModel, Object> updatePassword(CustomerModel model) {
		BigDecimal custId = (model.getCustomerId() == null) ? metaData.getCustomerId() : null;

		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).field(FIELD_PSSWRD);
		if (custId == null) {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.NULL_CUSTOMER_ID));
			throw new GlobalException(JaxError.NULL_CUSTOMER_ID.getCode(), "Null customer id passed ");
		}
		try {
			userValidationService.validateOtpFlow(model);
		} catch (InvalidOtpException e) {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.INVALID_OTP));
			throw e;
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(custId);
		onlineCustomer.setPassword(cryptoUtil.getHash(onlineCustomer.getUserName(), model.getPassword()));
		custDao.saveOnlineCustomer(onlineCustomer);
		
		CustomerModel outputModel = convert(onlineCustomer);
		if (outputModel.getEmail() != null) {
			jaxNotificationService.sendProfileChangeNotificationEmail(model, outputModel.getPersoninfo());
			logger.info("The update password mail notification success");
		}
		
		BoolRespModel responseModel = new BoolRespModel(true);
		auditService.log(auditEvent.result(Result.DONE));
		return AmxApiResponse.build(responseModel);
	}

	public void updateEmail(BigDecimal customerId, String email) {
		logger.info("Updating customer's email address " + email);
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);
		onlineCustomer.setEmail(email);
		Customer customer = custDao.getCustById(customerId);
		customer.setEmail(email);
		custDao.saveOnlineCustomer(onlineCustomer);
		custDao.saveCustomer(customer);
	}

	/**
	 * reset lock
	 */
	protected void unlockCustomer(CustomerOnlineRegistration onlineCustomer) {
		if (onlineCustomer.getLockCnt() != null || onlineCustomer.getLockDt() != null) {
			onlineCustomer.setLockCnt(null);
			onlineCustomer.setLockDt(null);
			custDao.saveOnlineCustomer(onlineCustomer);
		}
		onlineCustomer.setTokenSentCount(BigDecimal.ZERO);
	}

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
	
	public LoginLogoutHistory getLastLogoutHistoryByUserName(String userName) {

		Sort sort = new Sort(Direction.DESC, "logoutTime");
		LoginLogoutHistory loginLogoutHistory = loginLogoutHistoryRepositoryRepo
				.findFirstByuserNameAndLogoutTimeIsNotNull(userName, sort);

		return loginLogoutHistory;
	}

	protected void saveLoginLogoutHistoryByUserName(String userName) {
		LoginLogoutHistory output = new LoginLogoutHistory();
		output = new LoginLogoutHistory();
		output.setLoginType("C");
		output.setUserName(userName);
		output.setLoginTime(new Timestamp(new Date().getTime()));
		loginLogoutHistoryRepositoryRepo.save(output);
	}

	/**
	 * My Profile Info
	 */

	public ApiResponse getCustomerInfo(BigDecimal countryId, BigDecimal companyId, BigDecimal customerId) {
		CustomerDto customerInfo = new CustomerDto();
		ApiResponse response = getBlackApiResponse();
		BeneficiaryListDTO beneDto = null;
		List<Customer> customerList = customerDao.getCustomerByCustomerId(countryId, companyId, customerId);
		BenificiaryListView defaultBene = beneficiaryOnlineDao.getDefaultBeneficiary(customerId, countryId);
		List<CustomerRemittanceTransactionView> noOfTrnxList = tranxHistDao.getCustomerTotalTrnx(customerId);
		List<ContactDetail> contactList = contactDao.getContactDetailForLocal(new Customer(customerId));

		if (customerList.isEmpty()) {
			throw new GlobalException("Customer is not avaliable");
		} else {
			customerInfo = convertCustomerDto(customerList);
			if (defaultBene != null) {
				beneDto = convertBeneModelToDto(defaultBene);
			}
			if (!noOfTrnxList.isEmpty()) {
				customerInfo.setTotalTrnxCount(new BigDecimal(noOfTrnxList.size()));
			}
			customerInfo.setDefaultBeneDto(beneDto);

			if (!contactList.isEmpty()) {
				customerInfo.setLocalContactBuilding(contactList.get(0).getBuildingNo());
				customerInfo.setStreet(contactList.get(0).getStreet());
				customerInfo.setBlockNo(contactList.get(0).getBlock());
				customerInfo.setHouse(contactList.get(0).getFlat());
				List<CountryMasterView> countryMasterView = countryDao.findByLanguageIdAndCountryId(new BigDecimal(1),
						contactList.get(0).getFsCountryMaster().getCountryId());
				if (!countryMasterView.isEmpty()) {
					customerInfo.setLocalContactCountry(countryMasterView.get(0).getCountryName());
					if (contactList.get(0).getFsStateMaster() != null) {
						List<ViewState> stateMasterView = stateDao.getState(countryMasterView.get(0).getCountryId(),
								contactList.get(0).getFsStateMaster().getStateId(), new BigDecimal(1));
						if (!stateMasterView.isEmpty()) {
							customerInfo.setLocalContactState(stateMasterView.get(0).getStateName());
							DistrictMaster distictMaster = contactList.get(0).getFsDistrictMaster();
							if (distictMaster != null) {
								List<ViewDistrict> districtMas = districtDao.getDistrict(
										stateMasterView.get(0).getStateId(), distictMaster.getDistrictId(),
										new BigDecimal(1));
								if (!districtMas.isEmpty()) {
									customerInfo.setLocalContactDistrict(districtMas.get(0).getDistrictDesc());
									List<ViewCity> cityDetails = cityDao.getCityDescription(
											districtMas.get(0).getDistrictId(),
											contactList.get(0).getFsCityMaster().getCityId(), new BigDecimal(1));
									if (!cityDetails.isEmpty()) {
										customerInfo.setLocalContactCity(cityDetails.get(0).getCityName());
									}
								}
							}
						}
					}

				}
				response.getData().getValues().add(customerInfo);
				response.setResponseStatus(ResponseStatus.OK);
			}
		}
		response.getData().setType("customer-dto");
		return response;
	}

	public CustomerDto convertCustomerDto(List<Customer> customerList) {
		CustomerDto dto = null;
		for (Customer model : customerList) {
			dto = new CustomerDto();
			dto.setTitle(model.getTitle());
			dto.setFirstName(model.getFirstName());
			dto.setMiddleName(model.getMiddleName());
			dto.setLastName(model.getLastName());
			dto.setIdentityInt(model.getIdentityInt());
			dto.setIdentityExpiredDate(model.getIdentityExpiredDate());
			dto.setEmail(model.getEmail() == null ? "" : model.getEmail());
			dto.setMobile(model.getMobile() == null ? "" : model.getMobile());
			if (model.getLoyaltyPoints() != null) {
				dto.setLoyaltyPoints(model.getLoyaltyPoints().compareTo(BigDecimal.ZERO) <= 0 ? BigDecimal.ZERO
						: model.getLoyaltyPoints());
			} else {
				dto.setLoyaltyPoints(BigDecimal.ZERO);
			}
			dto.setDateOfBirth(model.getDateOfBirth());
			dto.setCustomerId(model.getCustomerId());
			dto.setCustomerReference(model.getCustomerReference());
			dto.setIsActive(model.getIsActive());
			dto.setFirstNameLocal(model.getFirstNameLocal());
			dto.setMiddleNameLocal(model.getMiddleNameLocal());
			dto.setLastNameLocal(model.getLastNameLocal());
			dto.setShortName(model.getShortName());
			dto.setShortNameLocal(model.getShortNameLocal());
		}

		return dto;
	}

	private BeneficiaryListDTO convertBeneModelToDto(BenificiaryListView beneModel) {
		BeneficiaryListDTO dto = new BeneficiaryListDTO();
		try {
			BeanUtils.copyProperties(dto, beneModel);
		} catch (IllegalAccessException | InvocationTargetException e) {
			logger.error("bene list display", e);
		}
		return dto;
	}

	/**
	 * Unlocks the customer account
	 * 
	 * @param customerId
	 * @return
	 */
	public ApiResponse unlockCustomer(BigDecimal customerId) {
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse();
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);

		// Audit
		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).customerId(customerId)
				.field(FIELD_LOCK);

		if (onlineCustomer == null) {
			auditService.log(
					auditEvent.result(Result.REJECTED).message(JaxError.USER_NOT_REGISTERED));
			throw new GlobalException(JaxError.USER_NOT_REGISTERED,
					"User with userId: " + customerId + " is not registered or not active");
		}
		auditEvent.from(onlineCustomer.getLockCnt()); // Audit
		this.unlockCustomer(onlineCustomer);
		auditEvent.to(onlineCustomer.getLockCnt()); // Audit
		responseModel.setSuccess(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		auditService.log(auditEvent.result(Result.DONE)); // Audit
		return response;

	}

	/**
	 * Unlocks the customer account
	 * 
	 * @param civilid
	 * @return
	 */
	public ApiResponse unlockCustomer(String civilid) {
		Customer cust = custDao.getCustomerByCivilId(civilid);
		BigDecimal customerId = null;
		if (cust != null)
			customerId = cust.getCustomerId();
		return unlockCustomer(customerId);
	}

	/**
	 * Deactivates the customer
	 * 
	 * @param bigDecimal
	 */
	public ApiResponse deactivateCustomer(BigDecimal customerId) {
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse();
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerId);

		// Audit
		CActivityEvent auditEvent = new CActivityEvent(CActivityEvent.Type.PROFILE_UPDATE).customerId(customerId)
				.field(FIELD_STATUS);

		if (onlineCustomer != null) {
			auditEvent.from(onlineCustomer.getStatus());
			onlineCustomer.setStatus(ConstantDocument.No);
			custDao.saveOnlineCustomer(onlineCustomer);
			auditEvent.to(onlineCustomer.getStatus());
		} else {
			auditService.log(auditEvent.result(Result.REJECTED).message(JaxError.USER_NOT_REGISTERED));
			throw new GlobalException(JaxError.USER_NOT_REGISTERED,
					"User with userId: " + customerId + " is not registered or not active");
		}
		responseModel.setSuccess(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);

		auditService.log(auditEvent.result(Result.DONE)); // Audit
		return response;

	}

	/**
	 * Deactivates the customer
	 */
	public ApiResponse deactivateCustomer(String civilid) {
		Customer cust = custDao.getCustomerByCivilId(civilid);
		BigDecimal customerId = null;
		if (cust != null)
			customerId = cust.getCustomerId();

		return deactivateCustomer(customerId);

	}

	public void validateMobile(CustomerModel custModel) {
		Customer cust = custDao.getCustById(custModel.getCustomerId());
		userValidationService.validateMobileNumberLength(cust, custModel.getMobile());
		userValidationService.isMobileExist(cust, custModel.getMobile());

	} // end of validateMobile

	public PersonInfo getPersonInfo(BigDecimal customerId) {
		PersonInfo personInfo = null;
		try {
			personInfo = new PersonInfo();
			Customer customer = custDao.getCustById(customerId);

			BeanUtils.copyProperties(personInfo, customer);
			personInfo.setEmail(customer.getEmail());
			personInfo.setMobile(customer.getMobile());
		} catch (Exception e) {
		}
		return personInfo;
	}

	public ApiResponse customerLoggedIn(CustomerModel customerModel) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customerModel.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		CustomerModel cusModel = convert(onlineCustomer);
		// afterLoginSteps(onlineCustomer);
		response.getData().getValues().add(cusModel);
		response.getData().setType(cusModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		auditService.log(new CActivityEvent(CActivityEvent.Type.LOGIN));
		return response;
	}

	private void addMyProfileAuditLog(CActivityEvent auditEvent, CustomerModel custNew, Customer custOld,
			CustomerOnlineRegistration onlineCustOld) {
		List<SecurityQuestionModel> secQuestions = custNew.getSecurityquestions();
		if (!CollectionUtils.isEmpty(secQuestions)) {
			auditEvent.field(FIELD_SECQ).from(FIELD_VALUE_HIDDEN).to(FIELD_VALUE_HIDDEN);
		}
		if (custNew.getCaption() != null) {
			auditEvent.field(FIELD_CAPTION).from(onlineCustOld.getCaption()).to(custNew.getCaption());
		}
		if (custNew.getImageUrl() != null) {
			auditEvent.field(FIELD_IMAGE).from(onlineCustOld.getImageUrl()).to(custNew.getImageUrl());
		}

		if (custNew.getLoginId() != null) {
			auditEvent.field(FIELD_LOGINID).from(onlineCustOld.getLoginId()).to(custNew.getLoginId());
		}

		if (custNew.getPassword() != null) {
			auditEvent.field(FIELD_PSSWRD).from(FIELD_VALUE_HIDDEN).to(FIELD_VALUE_HIDDEN);
		}

		// update new email id
		if (custNew.getEmail() != null) {
			auditEvent.field(FIELD_EMAIL).from(onlineCustOld.getEmail()).to(custNew.getEmail());
		}
	}

	public Customer getCustById(BigDecimal id) {
		return repo.findOne(id);
	}
	
	public Customer getCustomerDetails(String loginId) {
		return repo.getCustomerDetails(loginId);
	}
	
	public Customer getCustomerDetailsByCustomerId(BigDecimal custId) {
		return repo.getCustomerDetailsByCustomerId(custId);
	}
	
	@Transactional
	public void deActivateFsCustomer(BigDecimal customerId) {
		Customer customer = repo.findOne(customerId);
		customer.setIsActive(ConstantDocument.Deleted);
		List<CustomerIdProof> activeIdProofs = customerIdProofDao.getActiveCustomeridProofForIdType(customerId,
				customer.getIdentityTypeId());
		for (CustomerIdProof customerIdProof : activeIdProofs) {
			customerIdProof.setIdentityStatus(ConstantDocument.Deleted);
		}
		if (!CollectionUtils.isEmpty(activeIdProofs)) {
			customerIdProofDao.save(activeIdProofs);
		}
		repo.save(customer);
	}
	
	
	
	public UserFingerprintResponseModel linkDeviceId(BigDecimal customerId) {

		CustomerOnlineRegistration customerOnlineRegistration = userValidationService
				.validateOnlineCustomerByIdentityId(customerId);
		
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
		Email email = new Email();
		
		email.addTo(customerOnlineRegistration.getEmail());
		email.setITemplate(TemplatesMX.FINGERPRINT_LINKED_SUCCESS);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, personinfo);

		logger.debug("Email to - " + customerOnlineRegistration.getEmail());
		sendEmail(email);
		return userFingerprintResponsemodel;
	}
	@Async(ExecutorConfig.DEFAULT)
	public void sendEmail(Email email) {
		try {
			postManService.sendEmailAsync(email);
		} catch (PostManException e) {
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

	public CustomerModel loginCustomerByFingerprint(String civilId, String identityTypeStr, String password) {
		userValidationService.validateIdentityInt(civilId, identityTypeStr);
		BigDecimal identityType = new BigDecimal(identityTypeStr);

		CustomerOnlineRegistration customerOnlineRegistration = userValidationService
				.validateOnlineCustomerByIdentityId(civilId, identityType);
		userValidationService.validateDevicePassword(customerOnlineRegistration, password);
		CustomerModel customerModel = convert(customerOnlineRegistration);
		return customerModel;
	}

	public BoolRespModel delinkFingerprint() {
		CustomerOnlineRegistration customerOnlineRegistration = custDao
				.getOnlineCustByCustomerId(metaData.getCustomerId());
		customerOnlineRegistration.setFingerprintDeviceId("");
		customerOnlineRegistration.setDevicePassword("");
		custDao.saveOnlineCustomer(customerOnlineRegistration);
		BoolRespModel boolRespModel = new BoolRespModel();
		boolRespModel.setSuccess(Boolean.TRUE);
		Customer customer = custDao.getCustById(customerOnlineRegistration.getCustomerId());
		PersonInfo personinfo = new PersonInfo();
		personinfo.setFirstName(customer.getFirstName());
		personinfo.setMiddleName(customer.getMiddleName());
		personinfo.setLastName(customer.getLastName());
		Email email = new Email();
		email.addTo(customerOnlineRegistration.getEmail());
		email.setITemplate(TemplatesMX.FINGERPRINT_DELINKED_SUCCESS);
		email.setHtml(true);
		email.getModel().put(RESP_DATA_KEY, personinfo);
		logger.debug("Email to - " + customerOnlineRegistration.getEmail());
		sendEmail(email);
		return boolRespModel;
	}
	
	public CustomerFlags getCustomerFlags(BigDecimal customerId) {
		CustomerFlags customerFlags = null;
		if (customerId != null) {
			customerFlags = customerFlagManager.getCustomerFlags(customerId);
		}
		return customerFlags;
	}

	/**
	 * deactivates id proof of customer
	 * @param customerId
	 */
	public void deActivateCustomerIdProof(BigDecimal customerId) {
		Customer customer = repo.findOne(customerId);
		List<CustomerIdProof> activeIdProofs = customerIdProofDao.getActiveCustomerIdProof(customerId,
				customer.getIdentityTypeId());
		for (CustomerIdProof customerIdProof : activeIdProofs) {
			customerIdProof.setIdentityStatus(ConstantDocument.Deleted);
		}
		if (!CollectionUtils.isEmpty(activeIdProofs)) {
			customerIdProofDao.save(activeIdProofs);
		}
	}
	
	public void deactiveteCustomerIdProofPendingCompliance(BigDecimal customerId) {
		Customer customer = repo.findOne(customerId);
		List<CustomerIdProof> deActiveIdProofs = customerIdProofDao.getCompliancePendingCustomerIdProof(customerId,
				customer.getIdentityTypeId());
		for (CustomerIdProof customerIdProof : deActiveIdProofs) {
			customerIdProof.setIdentityStatus(ConstantDocument.Deleted);
		}
		if (!CollectionUtils.isEmpty(deActiveIdProofs)) {
			customerIdProofDao.save(deActiveIdProofs);
		}

	public List<Customer> getCustomerByIdentityInt(String identityInt) {
		String[] isActiveFlags = new String[] { ConstantDocument.Yes, ConstantDocument.No };
		List<Customer> customers = repo.getCustomerByIdentityIntAndIsActive(identityInt, Arrays.asList(isActiveFlags));
		return customers;
	}
	 
}
