package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.exception.jax.InvalidCivilIdException;
import com.amx.amxlib.exception.jax.InvalidOtpException;
import com.amx.amxlib.exception.jax.UserNotFoundException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
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
import com.amx.jax.meta.MetaData;
import com.amx.jax.scope.TenantContext;
import com.amx.jax.userservice.dao.CusmosDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.CustomerIdProofDao;
import com.amx.jax.userservice.dao.DmsDocumentDao;
import com.amx.jax.userservice.service.CustomerValidationContext.CustomerValidation;
import com.amx.jax.userservice.validation.ValidationClient;
import com.amx.jax.userservice.validation.ValidationClients;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.JaxUtil;
import com.amx.jax.util.validation.CustomerValidationService;

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

	private DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	protected void validateLoginId(String loginId) {
		CustomerOnlineRegistration existingCust = custDao.getOnlineCustomerByLoginIdOrUserName(loginId);
		if (existingCust != null) {
			throw new GlobalException("Username already taken", JaxError.USERNAME_ALREADY_EXISTS);
		}
	}

	public void validateAllLoginId(String loginId) {
		List<CustomerOnlineRegistration> existingCust = custDao.getOnlineCustomerWithStatusByLoginIdOrUserName(loginId);
		if (existingCust != null && !existingCust.isEmpty()) {
			throw new GlobalException("Username already taken", JaxError.USERNAME_ALREADY_EXISTS);
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
		boolean isValid = custValidation.validateCivilId(civilId, meta.getCountry().getCountryCode());
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid.");
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
			throw new GlobalException("Incorrect/wrong password", errorExpression);
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
			throw new GlobalException("ID proofs not available, contact branch", JaxError.NO_ID_PROOFS_AVAILABLE);
		}
	}

	private void validateIdProof(CustomerIdProof idProof) {

		String scanSystem = idProof.getScanSystem();
		if (idProof.getIdentityExpiryDate() != null && idProof.getIdentityExpiryDate().compareTo(new Date()) < 0) {
			throw new GlobalException("Identity proof are expired", JaxError.ID_PROOF_EXPIRED);
		}
		if ("A".equals(scanSystem)) {
			List<CustomerIdProof> validIds = idproofDao
					.getCustomerImageValidation(idProof.getFsCustomer().getCustomerId(), idProof.getIdentityTypeId());
			if (validIds == null || validIds.isEmpty()) {
				throw new GlobalException("Identity proof are expired or invalid", JaxError.ID_PROOFS_NOT_VALID);
			}
			for (CustomerIdProof id : validIds) {
				if (id.getIdentityExpiryDate() != null && id.getIdentityExpiryDate().compareTo(new Date()) < 0) {
					throw new GlobalException("Identity proof are expired", JaxError.ID_PROOF_EXPIRED);
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
					throw new GlobalException("Identity proof images not found", JaxError.ID_PROOFS_IMAGES_NOT_FOUND);
				}
			}
		} else {
			throw new GlobalException("Identity proof scans not found", JaxError.ID_PROOFS_SCAN_NOT_FOUND);
		}
	}

	protected void validateCustomerData(CustomerOnlineRegistration onlineCust, Customer customer) {

		if (customer.getCustomerReference() == null) {
			throw new GlobalException("Invalid Customer Reference", JaxError.INVALID_CUSTOMER_REFERENCE);
		}
		// validate contact details
		validateCustContact(customer);
		if (!"Y".equals(customer.getIsActive())) {
			throw new GlobalException("Customer is not active", JaxError.CUSTOMER_INACTIVE);
		}
		if (customer.getSignatureSpecimenClob() == null) {
			throw new GlobalException("CUSTOMER SIGNATURE NOT AVAILABLE", JaxError.CUSTOMER__SIGNATURE_UNAVAILABLE);
		}
		boolean insuranceCheck = ("Y".equals(customer.getMedicalInsuranceInd())
				|| "N".equals(customer.getMedicalInsuranceInd()));
		if (!insuranceCheck) {
			throw new GlobalException("INVALID MEDICAL INSURANCE INDICATOR", JaxError.INVALID_INSURANCE_INDICATOR);
		}
		ViewOnlineCustomerCheck onlineCustView = custDao.getOnlineCustomerview(customer.getCustomerId());
		if (onlineCustView != null && onlineCustView.getIdExpirtyDate() == null) {
			throw new GlobalException("ID is expired", JaxError.ID_PROOF_EXPIRED);
		}		
		validateOldEmosData(customer);

	}

	private void validateOldEmosData(Customer customer) {
		if (customer.getCustomerReference() == null) {
			throw new GlobalException("Old customer records not found in EMOS", JaxError.OLD_EMOS_USER_NOT_FOUND);
		}
		CusmasModel emosCustomer = cusmosDao.getOldCusMasDetails(customer.getCustomerReference());
		if (emosCustomer.getStatus() != null) {
			throw new GlobalException("RECORD IS DELETED IN OLD EMOS", JaxError.OLD_EMOS_USER_DELETED);
		}
		if (emosCustomer.getIdExpireDate() == null || emosCustomer.getIdExpireDate().compareTo(new Date()) < 0) {
			throw new GlobalException("ID EXPIRY IS NOT UPDATED OR HAS BEEN EXPIRED IN OLD EMOS",
					JaxError.OLD_EMOS_USER_DATA_EXPIRED);
		}
	}

	private void validateCustContact(Customer customer) {

		List<ContactDetail> contactDetails = contactDetailService.getContactDetail(customer.getCustomerId());
		if (CollectionUtils.isEmpty(contactDetails)) {
			throw new GlobalException("No contact details found", JaxError.MISSING_CONTACT_DETAILS);
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
			throw new GlobalException("No home contact details found", JaxError.MISSING_HOME_CONTACT_DETAILS);
		}
		if (!islocal) {
			throw new GlobalException("No local details found", JaxError.MISSING_LOCAL_CONTACT_DETAILS);
		}
	}
	
	void validateBlackListedCustomerForLogin(Customer customer) {

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
			throw new GlobalException("Your account is locked as we have found that your name has been black-listed by CBK.",
					JaxError.BLACK_LISTED_EXISTING_CIVIL_ID.getCode());
		}		
		if (StringUtils.isNotBlank(localNamesbuf.toString())) {
			blist = blistDao.getBlackByName(localNamesbuf.toString());
			if (blist != null && !blist.isEmpty()) {
				throw new GlobalException("Your account is locked as we have found that your name has been black-listed by CBK.",
						JaxError.BLACK_LISTED_EXISTING_CIVIL_ID.getCode());
			}
		}
	}

	protected void validateCustomerSecurityQuestions(List<SecurityQuestionModel> answers,
			CustomerOnlineRegistration customer) {

		for (SecurityQuestionModel answer : answers) {
			String actualAnswer = getActualAnswer(answer.getQuestionSrNo(), customer);
			if (!actualAnswer.equals(cryptoUtil.getHash(customer.getUserName(), answer.getAnswer()))) {
				incrementLockCount(customer);
				throw new GlobalException("Incorrect answer for question no. : " + answer.getQuestionSrNo(),
						JaxError.INCORRECT_SECURITY_QUESTION_ANSWER);
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
					custDao.saveOnlineCustomer(onlineCustomer);
					lockCnt = 0;
				}
				if (lockCnt >= MAX_OTP_ATTEMPTS) {
					throw new GlobalException("Customer is locked. No of attempts:- " + lockCnt,
							JaxError.USER_LOGIN_ATTEMPT_EXCEEDED);
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
			throw new GlobalException("Customer is locked. No of attempts:- " + lockCnt, errorExpression);
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
			throw new GlobalException("Online Customer id not found", JaxError.CUSTOMER_NOT_FOUND.getCode());
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
			throw new GlobalException("mOtp field is mandatory", JaxError.MISSING_OTP.getCode());
		}

		if (isEOtpFlowRequired && model.getEotp() == null) {
			throw new GlobalException("eOtp field is mandatory", JaxError.MISSING_OTP.getCode());
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

	public void validateTokenSentCount(CustomerOnlineRegistration onlineCust) {

		Integer limit = otpSettings.getMaxSendOtpAttempts();
		if (onlineCust.getTokenSentCount() != null && onlineCust.getTokenSentCount().intValue() >= limit) {
			throw new GlobalException("Limit to send otp exceeded", JaxError.SEND_OTP_LIMIT_EXCEEDED.getCode());
		}
	}

	public void validateTokenDate(CustomerOnlineRegistration onlineCust) {

		long otpValidTimeInMins = otpSettings.getOtpValidityTime().longValue();
		Date tokenDate = onlineCust.getTokenDate();
		if (tokenDate != null) {
			long diff = Calendar.getInstance().getTime().getTime() - tokenDate.getTime();
			long tokenTimeinMins = TimeUnit.MILLISECONDS.toMinutes(diff);
			if (tokenTimeinMins > otpValidTimeInMins) {
				throw new GlobalException("Otp has been expired", JaxError.OTP_EXPIRED.getCode());
			}
		}
	}

	protected void validateMobileNumberLength(Customer customer, String mobile) {

		ValidationClient validationClient = validationClients.getValidationClient(customer.getCountryId().toString());
		if (!validationClient.isValidMobileNumber(mobile)) {
			throw new GlobalException("Mobile Number length is not correct.", JaxError.INCORRECT_LENGTH);
		}
	}

	protected void isMobileExist(Customer customer, String mobile) {

		ValidationClient validationClient = validationClients.getValidationClient(customer.getCountryId().toString());
		if (validationClient.isMobileExist(mobile)) {
			throw new GlobalException("Mobile Number already exist.", JaxError.ALREADY_EXIST);
		}
	}

	public void validateCustomerVerification(BigDecimal customerId) {

		if (customerId != null) {
			CustomerVerification cv = customerVerificationService.getVerification(customerId,
					CustomerVerificationType.EMAIL);
			if (cv != null && ConstantDocument.No.equals(cv.getVerificationStatus()) && cv.getFieldValue() != null) {
				throw new GlobalException("Your email verificaiton is pending",
						JaxError.USER_DATA_VERIFICATION_PENDING_REG);
			}
		}
	}

	public void validateActiveCustomer(CustomerOnlineRegistration onlineCustReg, Boolean initRegistration) {
		if (initRegistration != null && initRegistration) {
			return;
		}
		if (onlineCustReg == null) {
			throw new GlobalException("User is not registered", JaxError.USER_NOT_REGISTERED);
		}
		if (initRegistration == null && !"Y".equals(onlineCustReg.getStatus())) {
			throw new GlobalException("Customer is not active", JaxError.CUSTOMER_INACTIVE);
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
	 */
	@SuppressWarnings("unused")
	public void validateNonActiveOrNonRegisteredCustomerStatus(String identityInt, JaxApiFlow apiFlow) {
		Customer customer = custDao.getCustomerByIdentityInt(identityInt);
		
		if(customer == null && apiFlow == JaxApiFlow.SIGNUP_DEFAULT) {
			return;
		}
		if (customer == null && apiFlow != JaxApiFlow.SIGNUP_DEFAULT) {
			throw new GlobalException("Customer not registered in branch", JaxError.CUSTOMER_NOT_REGISTERED_BRANCH);
		}
		if (!ConstantDocument.Yes.equals(customer.getIsActive())) {
			throw new GlobalException("Customer not active in branch, go to branch", JaxError.CUSTOMER_NOT_ACTIVE_BRANCH);
		}
		if (apiFlow == JaxApiFlow.SIGNUP_ONLINE) {
			return;
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(customer.getCustomerId());
		if (onlineCustomer == null) {
			throw new GlobalException("Customer not registered in online", JaxError.CUSTOMER_NOT_REGISTERED_ONLINE);
		}
		if (!ConstantDocument.Yes.equals(onlineCustomer.getStatus())) {
			throw new GlobalException("Customer not active in online", JaxError.CUSTOMER_NOT_ACTIVE_ONLINE);
		}
		if (ConstantDocument.Yes.equals(customer.getIsActive()) && apiFlow == JaxApiFlow.SIGNUP_DEFAULT) {
			throw new GlobalException("Customer active in branch", JaxError.CUSTOMER_ACTIVE_BRANCH);
		}
	}

}
