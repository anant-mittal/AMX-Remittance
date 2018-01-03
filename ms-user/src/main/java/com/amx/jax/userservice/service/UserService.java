package com.amx.jax.userservice.service;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.PersonInfo;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.LoginLogoutHistory;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exception.InvalidCivilIdException;
import com.amx.jax.exception.InvalidJsonInputException;
import com.amx.jax.exception.InvalidOtpException;
import com.amx.jax.exception.UserNotFoundException;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.SecurityQuestionsManager;
import com.amx.jax.userservice.repository.LoginLogoutHistoryRepository;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.StringUtil;
import com.amx.jax.util.Util;
import com.amx.jax.util.WebUtils;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@SuppressWarnings("rawtypes")
public class UserService extends AbstractUserService {

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private Util util;

	@Autowired
	private WebUtils webutil;

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
		model.setIsActive("Y".equals(cust.getStatus()));
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
			LoginLogoutHistory history = this.getLoginLogoutHistoryByUserName(cust.getUserName());
			if (history != null) {
				personinfo.setLastLoginTime(history.getLoginTime());
			}
			BeanUtils.copyProperties(personinfo, customer);
			model.setPersoninfo(personinfo);
		} catch (Exception e) {
		}
		return model;
	}

	public ApiResponse saveCustomer(CustomerModel model) {
		// userValidationService.validateCustomerForOnlineFlow(model.getCustomerId());
		if (model.getCustomerId() == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustomerByCustomerId(model.getCustomerId());
		if (onlineCust == null) {
			throw new UserNotFoundException("Customer is not registered for online flow");
		}
		if (model.getLoginId() != null) {
			userValidationService.validateLoginId(model.getLoginId());
		}

		simplifyAnswers(model.getSecurityquestions());
		onlineCust = custDao.saveOrUpdateOnlineCustomer(onlineCust, model);
		checkListManager.updateCustomerChecks(onlineCust, model);
		ApiResponse response = getBlackApiResponse();
		CustomerModel outputModel = convert(onlineCust);
		if (model.getLoginId() != null || model.getPassword() != null) { // after this step flow is going to login
			Map<String, Object> output = afterLoginSteps(onlineCust);
			if (output.get("PERSON_INFO") != null) {
				outputModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
			}
		}
		response.getData().getValues().add(outputModel);
		response.getData().setType(outputModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void simplifyAnswers(List<SecurityQuestionModel> securityquestions) {
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
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
/*		if (onlineCust == null) {
			onlineCust = new CustomerOnlineRegistration(cust);
			onlineCust.setHresetBy(cust.getIdentityInt());
			onlineCust.setHresetIp(webutil.getClientIp());
			onlineCust.setHresetkDt(new Date());
			onlineCust.setResetIp(webutil.getClientIp());
		}*/
		model.setEmail(cust.getEmail());
		model.setMobile(cust.getMobile());
		model.setIsActiveCustomer("Y".equals(onlineCust.getStatus()) ? true : false);
		return onlineCust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		userValidationService.validateCivilId(civilId);
		CivilIdOtpModel model = new CivilIdOtpModel();
		CustomerOnlineRegistration onlineCust = verifyCivilId(civilId, model);
		userValidationService.validateCustomerLockCount(onlineCust);
		generateToken(civilId, model);
		onlineCust.setEmailToken(model.getHashedOtp());
		onlineCust.setSmsToken(model.getHashedOtp());
		onlineCust.setTokenDate(new Date());
		custDao.saveOnlineCustomer(onlineCust);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	private void generateToken(String userId, CivilIdOtpModel model) {
		String randOtp = util.createRandomPassword(6);
		String hashedOtp = cryptoUtil.getHash(userId, randOtp);
		model.setHashedOtp(hashedOtp);
		model.setOtp(randOtp);
		logger.info("Generated otp for civilid- " + userId + " is " + randOtp);
	}

	public ApiResponse validateOtp(String civilId, String otp) {
		logger.debug("in validateopt of civilid: " + civilId);
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByUserId(civilId);
		if (onlineCust == null) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " not registered.");
		}
		if (StringUtils.isEmpty(otp)) {
			throw new InvalidJsonInputException("Otp is empty for civil-id: " + civilId);
		}
		userValidationService.validateCustomerLockCount(onlineCust);
		String emailTokenHash = onlineCust.getEmailToken();
		String otpHash = cryptoUtil.getHash(civilId, otp);
		if (!otpHash.equals(emailTokenHash)) {
			userValidationService.incrementLockCount(onlineCust);
			throw new InvalidOtpException("Otp is incorrect for civil-id: " + civilId);
		}
		checkListManager.updateMobileAndEmailCheck(onlineCust, custDao.getCheckListForUserId(civilId));
		this.unlockCustomer(onlineCust);
		onlineCust.setEmailToken(null);
		onlineCust.setMobileNumber(null);
		custDao.saveOnlineCustomer(onlineCust);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCust);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.debug("end of validateopt for civilid: " + civilId);
		return response;
	}

	public ApiResponse loginUser(String userId, String password) {
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustomerByLoginIdOrUserName(userId);
		if (onlineCustomer == null) {
			throw new UserNotFoundException("User with userId: " + userId + " not found or not active");
		}
		Customer customer = custDao.getCustById(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerLockCount(onlineCustomer);
		userValidationService.validatePassword(onlineCustomer, password);
		userValidationService.validateCustIdProofs(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerData(onlineCustomer, customer);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCustomer);
		Map<String, Object> output = afterLoginSteps(onlineCustomer);
		if (output.get("PERSON_INFO") != null) {
			customerModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
		}
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	/**
	 * call this method to perform tasks after login
	 */
	private Map<String, Object> afterLoginSteps(CustomerOnlineRegistration onlineCustomer) {
		custDao.updatetLoyaltyPoint(onlineCustomer.getCustomerId());
		this.unlockCustomer(onlineCustomer);
		this.saveLoginLogoutHistoryByUserName(onlineCustomer.getUserName());
		Map<String, Object> output = new HashMap<>();
		return output;
	}

	public ApiResponse getUserCheckList(String loginId) {
		UserVerificationCheckListDTO model = checkListManager.getUserCheckList(loginId);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.getData().setType(model.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.debug("end of getUserCheckList for loginId: " + loginId);
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

	public ApiResponse validateCustomerData(CustomerModel model) {
		if (model.getCustomerId() == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(model.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		userValidationService.validateCustomerLockCount(onlineCustomer);
		simplifyAnswers(model.getSecurityquestions());
		userValidationService.validateCustomerSecurityQuestions(model.getSecurityquestions(), onlineCustomer);
		this.unlockCustomer(onlineCustomer);
		CustomerModel responseModel = convert(onlineCustomer);
		Map<String, Object> output = afterLoginSteps(onlineCustomer);
		if (output.get("PERSON_INFO") != null) {
			responseModel.setPersoninfo((PersonInfo) output.get("PERSON_INFO"));
		}

		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	public ApiResponse updatePassword(Integer custId, String password) {
		if (custId == null) {
			throw new GlobalException("Null customer id passed ", JaxError.NULL_CUSTOMER_ID.getCode());
		}
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(new BigDecimal(custId));
		onlineCustomer.setPassword(cryptoUtil.getHash(onlineCustomer.getUserName(), password));
		custDao.saveOnlineCustomer(onlineCustomer);
		ApiResponse response = getBlackApiResponse();
		BooleanResponse responseModel = new BooleanResponse(true);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
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
	}

	protected LoginLogoutHistory getLoginLogoutHistoryByUserName(String userName) {

		Sort sort = new Sort(Direction.DESC, "loginLogoutId");
		LoginLogoutHistory output = loginLogoutHistoryRepositoryRepo.findFirst1ByuserName(userName, sort);

		return output;
	}

	protected void saveLoginLogoutHistoryByUserName(String userName) {
		LoginLogoutHistory output = getLoginLogoutHistoryByUserName(userName);
		if (output == null) {
			output = new LoginLogoutHistory();
			output.setLoginType("C");
			output.setUserName(userName);
		}
		output.setLoginTime(new Timestamp(new Date().getTime()));
		loginLogoutHistoryRepositoryRepo.save(output);
	}
}
