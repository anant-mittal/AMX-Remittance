package com.amx.jax.userservice.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.error.JaxError;
import com.amx.amxlib.meta.model.QuestModelDTO;
import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.UserVerificationCheckListDTO;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.dbmodel.OnlineQuestModel;
import com.amx.jax.exception.GlobalException;
import com.amx.jax.exception.InvalidCivilIdException;
import com.amx.jax.exception.InvalidJsonInputException;
import com.amx.jax.exception.InvalidOtpException;
import com.amx.jax.exception.UserNotFoundException;
import com.amx.jax.service.QuestionAnswerService;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.manager.SecurityQuestionsManager;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.Util;
import com.amx.jax.util.WebUtils;
import com.amx.jax.util.validation.CustomerValidation;
import com.amx.jax.util.validation.PatternValidator;

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
		model.setCaption(cust.getCaption());
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
		onlineCust = custDao.saveOrUpdateOnlineCustomer(onlineCust, model);
		checkListManager.updateCustomerChecks(onlineCust, model);
		ApiResponse response = getBlackApiResponse();
		CustomerModel outputModel = convert(onlineCust);
		response.getData().getValues().add(outputModel);
		response.getData().setType(outputModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	@Override
	public Class<UserModel> getModelClass() {
		return UserModel.class;
	}

	public CustomerOnlineRegistration verifyCivilId(String civilId, CivilIdOtpModel model) {
		Customer cust = userValidationService.validateCustomerForOnlineFlow(civilId);

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(cust.getCustomerId());
		if (onlineCust == null) {
			onlineCust = new CustomerOnlineRegistration(cust);
			onlineCust.setHresetBy(cust.getIdentityInt());
			onlineCust.setHresetIp(webutil.getClientIp());
			onlineCust.setHresetkDt(new Date());
			onlineCust.setResetIp(webutil.getClientIp());
		}
		model.setEmail(cust.getEmail());
		model.setMobile(cust.getMobile());
		model.setIsActiveCustomer("Y".equals(cust.getActivatedInd()) ? true : false);
		return onlineCust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		userValidationService.validateCivilId(civilId);
		CivilIdOtpModel model = new CivilIdOtpModel();
		CustomerOnlineRegistration onlineCust = verifyCivilId(civilId, model);

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
		String emailTokenHash = onlineCust.getEmailToken();
		String otpHash = cryptoUtil.getHash(civilId, otp);
		if (!otpHash.equals(emailTokenHash)) {
			throw new InvalidOtpException("Otp is incorrect for civil-id: " + civilId);
		}
		checkListManager.updateMobileAndEmailCheck(onlineCust, custDao.getCheckListForUserId(civilId));
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
			throw new UserNotFoundException("User with userId: " + userId + " not found");
		}
		Customer customer = custDao.getCustById(onlineCustomer.getCustomerId());
		userValidationService.validatePassword(onlineCustomer, password);
		userValidationService.validateCustIdProofs(onlineCustomer.getCustomerId());
		userValidationService.validateCustomerData(onlineCustomer, customer);
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCustomer);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
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
		CustomerOnlineRegistration onlineCustomer = custDao.getOnlineCustByCustomerId(model.getCustomerId());
		ApiResponse response = getBlackApiResponse();
		userValidationService.validateCustomerSecurityQuestions(model.getSecurityquestions(), onlineCustomer);
		CustomerModel responseModel = convert(onlineCustomer);
		response.getData().getValues().add(responseModel);
		response.getData().setType(responseModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}
}
