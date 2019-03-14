package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.constant.CommunicationChannel;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.amxlib.exception.jax.InvalidOtpException;
import com.amx.amxlib.exception.jax.UserNotFoundException;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.jax.JaxAuthCache;
import com.amx.jax.JaxAuthCache.JaxAuthMeta;
import com.amx.jax.JaxAuthContext;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.constant.CustomerVerificationType;
import com.amx.jax.constant.JaxApiFlow;
import com.amx.jax.dal.ImageCheckDao;
import com.amx.jax.dao.BlackListDao;
import com.amx.jax.dbmodel.BlackListModel;
import com.amx.jax.dbmodel.ContactDetail;
import com.amx.jax.dbmodel.CusmasModel;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerIdProof;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.CustomerVerification;
import com.amx.jax.dbmodel.DmsDocumentModel;
import com.amx.jax.dbmodel.ViewOnlineCustomerCheck;
import com.amx.jax.error.JaxError;
import com.amx.jax.exception.ExceptionMessageKey;
import com.amx.jax.meta.MetaData;
import com.amx.jax.model.auth.CustomerRequestAuthMeta;
import com.amx.jax.model.auth.QuestModelDTO;
import com.amx.jax.repository.IContactDetailDao;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.userservice.constant.CustomerDataVerificationQuestion;
import com.amx.jax.userservice.dao.CusmosDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.dao.DmsDocumentDao;
import com.amx.jax.userservice.manager.SecurityQuestionsManager;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.validation.ValidationClient;
import com.amx.jax.userservice.validation.ValidationClients;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.validation.CustomerValidationService;
import com.amx.utils.Constants;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserValidationService {

	Logger logger = Logger.getLogger(UserValidationService.class);

	@Autowired
	private CustomerValidationService custValidation;

	@Autowired
	private ContactDetailService contactDetailService;

	@Autowired
	private MetaData meta;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private CustomerIdProofDao idproofDao;

	@Autowired
	private BlackListDao blistDao;

	@Autowired
	private CusmosDao cusmosDao;

	@Autowired
	private ImageCheckDao imageCheckDao;

	@Autowired
	private DmsDocumentDao dmsDocDao;

	@Autowired
	OtpSettings otpSettings;

	@Autowired
	private ValidationClients validationClients;

	@Autowired
	private JaxUtil jaxUtil;

	@Autowired
	private CustomerVerificationService customerVerificationService;

	@Autowired
	TenantContext<CustomerValidation> tenantContext;

	@Autowired
	private UserValidationService userValidationService;

	@Autowired
	UserService userService;

	@Autowired
	SecurityQuestionsManager securityQuestionsManager;

	@Autowired
	JaxAuthCache jaxAuthCache;

	@Autowired
	MetaData metaData;

	@Autowired
	IContactDetailDao contactDetailDao;

	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	protected void validateLoginId(String loginId) {
		CustomerOnlineRegistration existingCust = custDao.getOnlineCustomerByLoginIdOrUserName(loginId);
		if (existingCust != null) {
			throw new GlobalException(JaxError.USERNAME_ALREADY_EXISTS, "Username already taken");
		}
	}

	public void validateAllLoginId(String loginId) {
		List<CustomerOnlineRegistration> existingCust = custDao.getOnlineCustomerWithStatusByLoginIdOrUserName(loginId);
		if (existingCust != null && !existingCust.isEmpty()) {
			throw new GlobalException(JaxError.USERNAME_ALREADY_EXISTS, "Username already taken");
		}
	}

	public Customer validateCustomerForOnlineFlow(String civilId) {
		Customer cust = custDao.getCustomerByCivilId(civilId);
		if (cust == null) {
			throw new UserNotFoundException("Civil id is not registered at branch, civil id no,: " + civilId);
		}
		if (cust.getMobile() == null) {
			throw new GlobalException("Mobile number is empty. Contact branch to update the same.");
		}

		if (cust.getEmail() == null) {
			createEmailVerification(cust);
		}
		this.validateCustIdProofs(cust.getCustomerId());
		return cust;
	}

	private CustomerVerification createEmailVerification(Customer cust) {
		CustomerVerification customerVerification = customerVerificationService.getVerification(cust,
				CustomerVerificationType.EMAIL);
		if (customerVerification == null) {
			customerVerification = new CustomerVerification();
			customerVerification.setCustomerId(cust.getCustomerId());
			customerVerification.setVerificationType("EMAIL");
			customerVerification.setVerificationStatus("N");
			customerVerification.setCreateDate(new Date());
			customerVerificationService.saveOrUpdateVerification(customerVerification);
		}
		return customerVerification;
	}

	protected void validateCivilId(String civilId) {
		boolean isValid = custValidation.validateCivilId(civilId, meta.getCountry().getISO2Code());
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid!");
		}
	}

	// Validate IdentityInt
	protected void validateIdentityInt(String civilId, BigDecimal identityType) {
		boolean isValid = custValidation.validateIdentityInt(civilId, meta.getCountry().getISO2Code(), identityType);
		if (!isValid) {
			throw new InvalidCivilIdException("Id " + civilId + " is not valid!");
		}
	}

	protected void validatePassword(CustomerOnlineRegistration customer, String password) {
		String dbPwd = customer.getPassword();
		String passwordhashed = cryptoUtil.getHash(customer.getUserName(), password);
		if (!dbPwd.equals(passwordhashed)) {
			Integer attemptsLeft = incrementLockCount(customer);
			String errorExpression = JaxError.WRONG_PASSWORD.toString();
			if (attemptsLeft > 0) {
				errorExpression = jaxUtil.buildErrorExpression(JaxError.WRONG_PASSWORDS_ATTEMPTS.toString(),
						attemptsLeft);
			}
			throw new GlobalException(errorExpression, "Incorrect/wrong password");
		}
	}
	
	protected void validateDevicePassword(CustomerOnlineRegistration customer, String password) {
		String dbPassword = customer.getDevicePassword();
		String passwordHashed = null;
		try {
			passwordHashed = com.amx.utils.CryptoUtil.getSHA2Hash(password);
		} catch (NoSuchAlgorithmException e) {
			logger.error("Exception thrown for incorrect algorithm ", e);
			throw new GlobalException("Unable to generate hashed password");
		}
		if (!dbPassword.equals(passwordHashed)) {
			Integer attemptsLeft = incrementLockCount(customer);
			String errorExpression = JaxError.WRONG_PASSWORD.toString();
			if (attemptsLeft > 0) {
				errorExpression = jaxUtil.buildErrorExpression(JaxError.WRONG_PASSWORDS_ATTEMPTS.toString(),
						attemptsLeft);
			}
			throw new GlobalException(errorExpression, "Incorrect/wrong password");
		}
	}

	protected void validateCustIdProofs(BigDecimal custId) {
		if (tenantContext.get() != null) {
			tenantContext.get().validateCustIdProofs(custId);
			return;
		}
		List<CustomerIdProof> idProofs = idproofDao.getCustomerIdProofs(custId);
		for (CustomerIdProof idProof : idProofs) {
			validateIdProof(idProof);
		}
		if (idProofs.isEmpty()) {
			throw new GlobalException(JaxError.NO_ID_PROOFS_AVAILABLE, "ID proofs not available, contact branch");
		}
	}

	private void validateIdProof(CustomerIdProof idProof) {

		String scanSystem = idProof.getScanSystem();
		if (idProof.getIdentityExpiryDate() != null && idProof.getIdentityExpiryDate().compareTo(new Date()) < 0) {
			throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Identity proof are expired");
		}
		if ("A".equals(scanSystem)) {
			List<CustomerIdProof> validIds = idproofDao
					.getCustomerImageValidation(idProof.getFsCustomer().getCustomerId(), idProof.getIdentityTypeId());
			if (validIds == null || validIds.isEmpty()) {
				throw new GlobalException(JaxError.ID_PROOFS_NOT_VALID, "Identity proof are expired or invalid");
			}
			for (CustomerIdProof id : validIds) {
				if (id.getIdentityExpiryDate() != null && id.getIdentityExpiryDate().compareTo(new Date()) < 0) {
					throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "Identity proof are expired");
				}
			}

		} else if ("D".equals(scanSystem)) {
			Map<String, Object> imageChecks = imageCheckDao.dmsImageCheck(idProof.getIdentityTypeId(),
					idProof.getIdentityInt(), sdf.format(idProof.getIdentityExpiryDate()));
			if (imageChecks.get("docBlobId") != null && imageChecks.get("docFinYr") != null) {
				Integer docBlobIdInt = (Integer) imageChecks.get("docBlobId");
				Integer docFinYrInt = (Integer) imageChecks.get("docFinYr");
				List<DmsDocumentModel> dmsDocs = dmsDocDao.getDmsDocument(new BigDecimal(docBlobIdInt),
						new BigDecimal(docFinYrInt));
				if (dmsDocs == null || dmsDocs.isEmpty()) {
					throw new GlobalException(JaxError.ID_PROOFS_IMAGES_NOT_FOUND, "Identity proof images not found");
				}
			}
		} else {
			throw new GlobalException(JaxError.ID_PROOFS_SCAN_NOT_FOUND, "Identity proof scans not found");
		}
	}

	protected void validateCustomerData(CustomerOnlineRegistration onlineCust, Customer customer) {

		if (customer.getCustomerReference() == null) {
			throw new GlobalException(JaxError.INVALID_CUSTOMER_REFERENCE, "Invalid Customer Reference");
		}
		// validate contact details
		validateCustContact(customer);
		if (!"Y".equals(customer.getIsActive())) {
			throw new GlobalException(JaxError.CUSTOMER_INACTIVE, "Customer is not active");
		}
		if (customer.getSignatureSpecimenClob() == null) {
			throw new GlobalException(JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE, "CUSTOMER SIGNATURE NOT AVAILABLE");
		}
		boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd())
				|| "N".equals(customer.getMedicalInsuranceInd()));
		if (!insuranceCheck) {
			throw new GlobalException(JaxError.INVALID_INSURANCE_INDICATOR, "INVALID MEDICAL INSURANCE INDICATOR");
		}
		ViewOnlineCustomerCheck onlineCustView = custDao.getOnlineCustomerview(customer.getCustomerId());
		if (onlineCustView != null && onlineCustView.getIdExpirtyDate() == null) {
			throw new GlobalException(JaxError.ID_PROOF_EXPIRED, "ID is expired");
		}
		validateOldEmosData(customer);

	}

	private void validateOldEmosData(Customer customer) {
		if (customer.getCustomerReference() == null) {
			throw new GlobalException(JaxError.OLD_EMOS_USER_NOT_FOUND, "Old customer records not found in EMOS");
		}
		CusmasModel emosCustomer = cusmosDao.getOldCusMasDetails(customer.getCustomerReference());
		if (emosCustomer.getStatus() != null) {
			throw new GlobalException(JaxError.OLD_EMOS_USER_DELETED, "RECORD IS DELETED IN OLD EMOS");
		}
		if (emosCustomer.getIdExpireDate() == null || emosCustomer.getIdExpireDate().compareTo(new Date()) < 0) {
			throw new GlobalException(JaxError.OLD_EMOS_USER_DATA_EXPIRED,
					"ID EXPIRY IS NOT UPDATED OR HAS BEEN EXPIRED IN OLD EMOS");
		}
	}

	private void validateCustContact(Customer customer) {

		List<ContactDetail> contactDetails = contactDetailService.getContactDetail(customer.getCustomerId());
		if (CollectionUtils.isEmpty(contactDetails)) {
			throw new GlobalException(JaxError.MISSING_CONTACT_DETAILS, "No contact details found");
		}
		boolean ishome = false, islocal = false;
		for (ContactDetail contact : contactDetails) {
			if (contact.getFsCustomer().getCountryId().equals(meta.getCountryId())) {
				ishome = true;
			}
			if (contact.getFsBizComponentDataByContactTypeId().getComponentDataId()
					.equals(ConstantDocument.CONTACT_TYPE_FOR_LOCAL)) {
				islocal = true;
			}
			if (contact.getFsBizComponentDataByContactTypeId().getComponentDataId()
					.equals(ConstantDocument.CONTACT_TYPE_FOR_HOME)) {
				ishome = true;
			}
		}
		if (!ishome) {
			throw new GlobalException(JaxError.MISSING_HOME_CONTACT_DETAILS, "No home contact details found");
		}
		if (!islocal) {
			throw new GlobalException(JaxError.MISSING_LOCAL_CONTACT_DETAILS, "No local details found");
		}
	}

	public void validateBlackListedCustomerForLogin(Customer customer) {

		StringBuffer engNamesbuf = new StringBuffer();
		if (StringUtils.isNotBlank(customer.getFirstName())) {
			engNamesbuf.append(customer.getFirstName().trim());
		}
		if (StringUtils.isNotBlank(customer.getMiddleName())) {
			engNamesbuf.append(customer.getMiddleName().trim());
		}
		if (StringUtils.isNotBlank(customer.getLastName())) {
			engNamesbuf.append(customer.getLastName().trim());
		}
		StringBuffer localNamesbuf = new StringBuffer();
		if (StringUtils.isNotBlank(customer.getFirstNameLocal())) {
			localNamesbuf.append(customer.getFirstNameLocal().trim());
		}
		if (StringUtils.isNotBlank(customer.getMiddleNameLocal())) {
			localNamesbuf.append(customer.getMiddleNameLocal().trim());
		}
		if (StringUtils.isNotBlank(customer.getLastNameLocal())) {
			localNamesbuf.append(customer.getLastNameLocal().trim());
		}
		List<BlackListModel> blist = blistDao.getBlackByName(engNamesbuf.toString());
		if (blist != null && !blist.isEmpty()) {
			throw new GlobalException(JaxError.BLACK_LISTED_EXISTING_CIVIL_ID.getStatusKey(),
					"Your account is locked as we have found that your name has been black-listed by CBK.");
		}
		if (StringUtils.isNotBlank(localNamesbuf.toString())) {
			blist = blistDao.getBlackByName(localNamesbuf.toString());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException(JaxError.BLACK_LISTED_EXISTING_CIVIL_ID.getStatusKey(),
						"Your account is locked as we have found that your name has been black-listed by CBK.");
			}
		}
	}

	protected void validateCustomerSecurityQuestions(List<SecurityQuestionModel> answers,
			CustomerOnlineRegistration customer) {

		for (SecurityQuestionModel answer : answers) {
			String actualAnswer = getActualAnswer(answer.getQuestionSrNo(), customer);
			if (!actualAnswer.equals(cryptoUtil.getHash(customer.getUserName(), answer.getAnswer()))) {
				incrementLockCount(customer);
				throw new GlobalException(JaxError.INCORRECT_SECURITY_QUESTION_ANSWER,
						"Incorrect answer for question no. : " + answer.getQuestionSrNo());
			}
		}

	}

	private String getActualAnswer(BigDecimal questionSrNo, CustomerOnlineRegistration customer) {

		if (questionSrNo.equals(customer.getSecurityQuestion1())) {
			return customer.getSecurityAnswer1();
		}
		if (questionSrNo.equals(customer.getSecurityQuestion2())) {
			return customer.getSecurityAnswer2();
		}
		if (questionSrNo.equals(customer.getSecurityQuestion3())) {
			return customer.getSecurityAnswer3();
		}
		if (questionSrNo.equals(customer.getSecurityQuestion4())) {
			return customer.getSecurityAnswer4();
		}
		if (questionSrNo.equals(customer.getSecurityQuestion5())) {
			return customer.getSecurityAnswer5();
		}

		return null;

	}

	public void validateCustomerLockCount(CustomerOnlineRegistration onlineCustomer) {
		final Integer MAX_OTP_ATTEMPTS = otpSettings.getMaxValidateOtpAttempts();
		if (onlineCustomer.getLockCnt() != null) {
			int lockCnt = onlineCustomer.getLockCnt().intValue();
			Date midnightTomorrow = getMidnightToday();

			if (lockCnt > 0 && onlineCustomer.getLockDt() != null) {
				if (midnightTomorrow.compareTo(onlineCustomer.getLockDt()) > 0) {
					onlineCustomer.setLockCnt(new BigDecimal(0));
					onlineCustomer.setLockDt(null);
					custDao.saveOnlineCustomer(onlineCustomer);
					lockCnt = 0;
				}
				if (lockCnt >= MAX_OTP_ATTEMPTS) {
					throw new GlobalException(JaxError.USER_LOGIN_ATTEMPT_EXCEEDED,
							"Customer is locked. No of attempts:- " + lockCnt);
				}
			}
		}
	}

	/**
	 * updates lock count by one due to wrong password/otp attempt
	 */
	public int incrementLockCount(CustomerOnlineRegistration onlineCustomer) {
		Integer lockCnt = 0;
		final Integer MAX_OTP_ATTEMPTS = otpSettings.getMaxValidateOtpAttempts();
		if (onlineCustomer.getLockCnt() != null) {
			lockCnt = onlineCustomer.getLockCnt().intValue();
		}
		lockCnt++;
		if (lockCnt >= MAX_OTP_ATTEMPTS) {
			onlineCustomer.setLockDt(new Date());
		}
		onlineCustomer.setLockCnt(new BigDecimal(lockCnt));
		custDao.saveOnlineCustomer(onlineCustomer);
		if (lockCnt >= MAX_OTP_ATTEMPTS) {
			String errorExpression = JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.toString();
			errorExpression = jaxUtil.buildErrorExpression(JaxError.USER_LOGIN_ATTEMPT_EXCEEDED.toString(), lockCnt);
			throw new GlobalException(errorExpression, "Customer is locked. No of attempts:- " + lockCnt);
		}
		return MAX_OTP_ATTEMPTS - lockCnt;
	}

	public Date getMidnightToday() {
		Calendar date = new GregorianCalendar();
		date.set(Calendar.HOUR_OF_DAY, 0);
		date.set(Calendar.MINUTE, 0);
		date.set(Calendar.SECOND, 0);
		date.set(Calendar.MILLISECOND, 0);

		return date.getTime();
	}

	protected CustomerOnlineRegistration validateOnlineCustomerByIdentityId(String identityId) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustomerByLoginIdOrUserName(identityId);
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		return onlineCustomer;
	}

	protected CustomerOnlineRegistration validateOnlineCustomerByIdentityId(String identityInt,
			BigDecimal identityType) {
		Customer customer = custDao.getActiveCustomerByIndentityIntAndType(identityInt, identityType);
		
		if (customer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		return onlineCustomer;
	}
	
	protected CustomerOnlineRegistration validateOnlineCustomerByIdentityId(BigDecimal customerId) {
		Customer customer = custDao.getCustById(customerId);
		if (customer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_FOUND.getStatusKey(), "Online Customer id not found");
		}
		return onlineCustomer;
	}
	
	

	public void validateOtpFlow(CustomerModel model) {
		if (model.isRegistrationFlow()) {
			return;
		}
		BigDecimal custId = meta.getCustomerId();
		Customer customer = custDao.getCustById(custId);

		boolean isMOtpFlowRequired = isMOtpFlowRequired(model);
		boolean isEOtpFlowRequired = isEOtpFlowRequired(model, customer);

		if (isMOtpFlowRequired && model.getMotp() == null) {
			throw new GlobalException(JaxError.MISSING_OTP.getStatusKey(), "mOtp field is mandatory");
		}

		if (isEOtpFlowRequired && model.getEotp() == null) {
			throw new GlobalException(JaxError.MISSING_OTP.getStatusKey(), "eOtp field is mandatory");
		}

		// mobile otp validation
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(custId);
		this.validateCustomerLockCount(onlineCustomer);
		this.validateTokenDate(onlineCustomer);
		String hashedotp = cryptoUtil.getHash(customer.getIdentityInt(), model.getMotp());
		String dbmOtp = onlineCustomer.getSmsToken();
		if (!hashedotp.equals(dbmOtp)) {
			this.incrementLockCount(onlineCustomer);
			throw new InvalidOtpException("Mobile Otp is incorrect for identity int: " + customer.getIdentityInt());
		}
		// email otp validation
		if (isEOtpFlowRequired && onlineCustomer.getEmailToken() != null) {
			String hashedEotp = cryptoUtil.getHash(customer.getIdentityInt(), model.getEotp());
			String dbeOtp = onlineCustomer.getEmailToken();
			if (!hashedEotp.equals(dbeOtp)) {
				this.incrementLockCount(onlineCustomer);
				throw new InvalidOtpException("Email Otp is incorrect for identity int:  " + customer.getIdentityInt());
			}
		}
		this.unlockCustomer(onlineCustomer);
	}

	private boolean isMOtpFlowRequired(CustomerModel model) {

		boolean required = false;
		if (model.getSecurityquestions() != null) {
			required = true;
		}
		if (model.getPassword() != null) {
			required = true;
		}
		if (model.getImageUrl() != null) {
			required = true;
		}
		if (model.getEmail() != null) {
			required = true;
		}
		return required;
	}

	private boolean isEOtpFlowRequired(CustomerModel model, Customer customer) {

		boolean required = false;

		if (model.getEmail() != null) {
			required = true;
		}

		if (model.getMobile() != null) {
			required = true;
		}
		CustomerVerification cv = customerVerificationService.getVerification(customer, CustomerVerificationType.EMAIL);
		if (cv != null && ConstantDocument.No.equals(cv.getVerificationStatus())) {
			required = false;
		}
		return required;
	}

	private boolean isSecurityQuestionRequired(CustomerModel model) {

		boolean required = false;
		if (model.getSecurityquestions() != null) {
			required = true;
		}

		return required;
	}

	private boolean isVerificationAnswerRequired(CustomerModel model) {

		boolean required = false;
		if (model.getVerificationAnswers() != null) {
			required = true;
		}

		return required;
	}

	public void validateTokenSentCount(CustomerOnlineRegistration onlineCust) {

		Integer limit = otpSettings.getMaxSendOtpAttempts();
		if (onlineCust.getTokenSentCount() != null && onlineCust.getTokenSentCount().intValue() >= limit) {
			throw new GlobalException(JaxError.SEND_OTP_LIMIT_EXCEEDED.getStatusKey(), "Limit to send otp exceeded");
		}
	}

	public void validateTokenDate(CustomerOnlineRegistration onlineCust) {

		long otpValidTimeInMins = otpSettings.getOtpValidityTime().longValue();
		Date tokenDate = onlineCust.getTokenDate();
		if (tokenDate != null) {
			long diff = Calendar.getInstance().getTime().getTime() - tokenDate.getTime();
			long tokenTimeinMins = TimeUnit.MILLISECONDS.toMinutes(diff);
			if (tokenTimeinMins > otpValidTimeInMins) {
				throw new GlobalException(JaxError.OTP_EXPIRED.getStatusKey(), "Otp has been expired");
			}
		}
	}

	protected void validateMobileNumberLength(Customer customer, String mobile) {

		ValidationClient validationClient = validationClients.getValidationClient(customer.getCountryId().toString());
		if (!validationClient.isValidMobileNumber(mobile)) {
			throw new GlobalException(JaxError.INCORRECT_LENGTH,
					ExceptionMessageKey.build(JaxError.INCORRECT_LENGTH, validationClient.mobileLength()),
					"Length of mobile number should be of " + validationClient.mobileLength() + " digits");
		}
	}

	protected void isMobileExist(Customer customer, String mobile) {

		ValidationClient validationClient = validationClients.getValidationClient(customer.getCountryId().toString());
		if (validationClient.isMobileExist(mobile)) {
			throw new GlobalException(JaxError.ALREADY_EXIST, "Mobile Number already exist.");
		}
	}

	public void validateCustomerVerification(BigDecimal customerId) {

		if (customerId != null) {
			CustomerVerification cv = customerVerificationService.getVerification(customerId,
					CustomerVerificationType.EMAIL);
			if (cv != null && ConstantDocument.No.equals(cv.getVerificationStatus()) && cv.getFieldValue() != null) {
				throw new GlobalException(JaxError.USER_DATA_VERIFICATION_PENDING_REG,
						"Your email verificaiton is pending");
			}
		}
	}

	public void validateActiveCustomer(CustomerOnlineRegistration onlineCustReg, Boolean initRegistration) {
		if (initRegistration != null && initRegistration) {
			return;
		}
		if (onlineCustReg == null) {
			throw new GlobalException(JaxError.USER_NOT_REGISTERED, "User is not registered");
		}
		if (initRegistration == null && !"Y".equals(onlineCustReg.getStatus())) {
			throw new GlobalException(JaxError.CUSTOMER_INACTIVE, "Customer is not active");
		}
	}

	protected void unlockCustomer(CustomerOnlineRegistration onlineCustomer) {
		if (onlineCustomer.getLockCnt() != null || onlineCustomer.getLockDt() != null) {
			onlineCustomer.setLockCnt(null);
			onlineCustomer.setLockDt(null);
			custDao.saveOnlineCustomer(onlineCustomer);
		}
		onlineCustomer.setTokenSentCount(BigDecimal.ZERO);
	}

	/**
	 * validates inactive or not registered customers status
	 * 
	 * @return
	 */
	@SuppressWarnings("unused")
	public List<Customer> validateNonActiveOrNonRegisteredCustomerStatus(String identityInt, JaxApiFlow apiFlow) {
		List<Customer> customers = null;

		customers = custDao.getCustomerByIdentityInt(identityInt);
		if (CollectionUtils.isEmpty(customers) && apiFlow == JaxApiFlow.SIGNUP_DEFAULT) {
			return customers;
		}
		if (CollectionUtils.isEmpty(customers) && apiFlow != JaxApiFlow.SIGNUP_DEFAULT) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_BRANCH, "Customer not registered in branch ");
		}
		// duplicate records check
		if (customers != null && customers.size() > 1) {
			customers = custDao.findActiveCustomers(identityInt);
			boolean isSingleRecord = (customers != null && customers.size() == 1);
			if (!isSingleRecord) {
				throw new GlobalException(JaxError.DUPLICATE_CUSTOMER_NOT_ACTIVE_BRANCH,
						"Customer not active in branch, please visit branch");
			}
		}
		switch (apiFlow) {
		case SIGNUP_ONLINE:
			validateCustomerForSignUpOnline(customers.get(0));
			break;
		// online partial reg
		case SIGNUP_DEFAULT:
			validateCustomerForSignUpDefault(customers.get(0));
			break;
		default:
			validateCustomerDefault(customers.get(0));
		}
		return customers;
	}

	private void validateCustomerDefault(Customer customer) {

		if (customer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_BRANCH, "Customer not registered in branch ");
		}
		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_ACTIVE_BRANCH,
					"Customer not active in branch, go to branch ");
		}

		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_ONLINE, "Customer not registered in online");
		}

		userValidationService.validateCustomerVerification(onlineCustomer.getCustomerId());

		if (!ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_ACTIVE_ONLINE, "Customer not active in online");
		}
	}

	private void validateCustomerForSignUpDefault(Customer customer) {

		if (customer == null) {
			return;
		}

		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_ACTIVE_BRANCH,
					"Customer not active in branch, go to branch ");
		}

		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_ONLINE, "Customer not registered in online");
		}

		userValidationService.validateCustomerVerification(onlineCustomer.getCustomerId());

		if (!ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_ACTIVE_ONLINE, "Customer not active in online");
		}
		if (ConstantDocument.Yes.equals(customer.getIsActive())) {
			throw new GlobalException(JaxError.CUSTOMER_ACTIVE_BRANCH, "Customer active in branch");
		}
	}

	private void validateCustomerForSignUpOnline(Customer customer) {
		if (customer == null) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_REGISTERED_BRANCH, "Customer not registered in branch ");
		}
		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			throw new GlobalException(JaxError.CUSTOMER_NOT_ACTIVE_BRANCH,
					"Customer not active in branch, go to branch ");
		}
		// validateBlockedCustomerForOnlineReg(customer);
	}

	private void validateBlockedCustomerForOnlineReg(Customer customer) {
		// article 20
		if (customer.getFsArticleDetails() != null) {
			String articleCode = customer.getFsArticleDetails().getFsArticleMaster().getArticleCode();
			if (ConstantDocument.ARTICLE_20_CODE.equals(articleCode)) {
				throw new GlobalException(JaxError.ONLINE_REG_NOT_ALLOWED_ARTICLE_20,
						"Your online account is not activated. Please visit the branch for assistance.");
			}
		}
	}

	public void validateEmailMobileUpdateFlow(CustomerModel customerModel, List<CommunicationChannel> channels) {
		GlobalException ex = null;
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getOrDefault(metaData.getCustomerId().toString(), new JaxAuthMeta());

		if (null == JaxAuthContext.getMotp() && null == JaxAuthContext.getEotp()
				&& null == JaxAuthContext.getSecAns()) {
			ex = new GlobalException(JaxError.OTP_AND_SEC_ANSWER_REQUIRED.getStatusKey(),
					"motp, eOtp and Security Answer required");
			CivilIdOtpModel civilIdOtpModel = (CivilIdOtpModel) userService
					.sendOtpForCivilId(customerModel.getIdentityId(), channels, null, null).getResult();
			QuestModelDTO secQuestion = securityQuestionsManager.getDataVerificationRandomQuestions(1).get(0);
			ex.setMeta(new CustomerRequestAuthMeta(civilIdOtpModel.getmOtpPrefix(), civilIdOtpModel.geteOtpPrefix(),
					secQuestion));
			jaxAuthMeta.setQuestId(secQuestion.getQuestId());
			jaxAuthCache.fastPut(metaData.getCustomerId().toString(), jaxAuthMeta);
		}

		if (null == JaxAuthContext.getMotp() && null == JaxAuthContext.getEotp()
				&& JaxAuthContext.getSecAns() != null) {
			ex = new GlobalException(JaxError.BOTH_OTP_REQUIRED.getStatusKey(), "motp and eOtp required");
			CivilIdOtpModel civilIdOtpModel = (CivilIdOtpModel) userService
					.sendOtpForCivilId(customerModel.getIdentityId(), channels, null, null).getResult();
			ex.setMeta(new CustomerRequestAuthMeta(civilIdOtpModel.getmOtpPrefix(), civilIdOtpModel.geteOtpPrefix()));

			jaxAuthCache.fastPut(metaData.getCustomerId().toString(), jaxAuthMeta);
		}

		if (isSecurityAnsRequired(jaxAuthMeta)) {
			ex = new GlobalException(JaxError.SEC_ANS_REQUIRED.getStatusKey(), "Security Answer required");
			QuestModelDTO secQuestion = securityQuestionsManager.getDataVerificationRandomQuestions(1).get(0);
			ex.setMeta(new CustomerRequestAuthMeta(secQuestion));
			jaxAuthMeta.setQuestId(secQuestion.getQuestId());
			jaxAuthCache.fastPut(metaData.getCustomerId().toString(), jaxAuthMeta);
		}

		if (ex != null) {
			throw ex;
		}
		customerModel.setMotp(JaxAuthContext.getMotp());
		customerModel.setEotp(JaxAuthContext.getEotp());
		validateSecurityAnswer();

	}

	private boolean isSecurityAnsRequired(JaxAuthMeta jaxAuthMeta) {
		if (JaxAuthContext.getMotp() != null && JaxAuthContext.getEotp() != null
				&& null == JaxAuthContext.getSecAns()) {
			return true;
		}
		if (jaxAuthMeta != null && jaxAuthMeta.getQuestId() == null) {
			return true;
		}
		return false;
	}

	private void validateSecurityAnswer() {
		JaxAuthMeta jaxAuthMeta = jaxAuthCache.getOrDefault(metaData.getCustomerId().toString(), new JaxAuthMeta());

		// get question from cache
		CustomerDataVerificationQuestion question = CustomerDataVerificationQuestion
				.getCustomerDataVerificationQuestionById(jaxAuthMeta.getQuestId());

		// get answer from JaxAuthContext.getSecAns()
		String answer = JaxAuthContext.getSecAns();

		Customer customerInfo = custDao.getCustById(metaData.getCustomerId());
		ContactDetail contactDetailsLocal = contactDetailDao.getContactDetailForLocal(metaData.getCustomerId());

		switch (question) {
		case Q1: {
			String correctState = contactDetailsLocal.getFsStateMaster().getFsStateMasterDescs().get(0).getStateName();
			if (!correctState.equalsIgnoreCase(answer)) {
				throw new GlobalException(JaxError.INVALIDATE_ANSWER, "Given Answer is Invalid");
			}
			break;
		}

		case Q2: {
			Date identityExpiry = customerInfo.getIdentityExpiredDate();
			Date givenDate = com.amx.jax.util.DateUtil.convertStringToDate(answer);
			if (!DateUtils.isSameDay(identityExpiry, givenDate)) {
				throw new GlobalException(JaxError.INVALIDATE_ANSWER, "Given Answer is Invalid");
			}
			break;
		}

		default:
			break;

		}
	}
	
	
	public void validateIdentityInt(String identityInt, String identityType) {
		BigDecimal identyType = new BigDecimal(identityType);
		tenantContext.get().validateIdentityInt(identityInt, identyType);

	}
	

}
