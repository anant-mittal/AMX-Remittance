package com.amx.jax.userservice.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.AbstractUserModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.UserModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.exception.InvalidCivilIdException;
import com.amx.jax.exception.InvalidJsonInputException;
import com.amx.jax.exception.InvalidOtpException;
import com.amx.jax.exception.UserNotFoundException;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.KwUserDao;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.Util;
import com.amx.jax.util.WebUtils;
import com.amx.jax.util.validation.CustomerValidation;
import com.amx.jax.util.validation.PatternValidator;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService extends AbstractUserService {

	Logger logger = Logger.getLogger(UserService.class);

	@Autowired
	private KwUserDao dao;

	@Autowired
	private CustomerDao custDao;

	@Autowired
	private CustomerValidation custValidation;

	@Autowired
	private MetaData meta;

	@Autowired
	private CryptoUtil cryptoUtil;

	@Autowired
	private Util util;

	@Autowired
	private WebUtils webutil;

	@Autowired
	private PatternValidator patternValidator;

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
		return dao;
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
		validateCustomerForOnlineFlow(model.getIdentityId());
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByUserId(model.getIdentityId());
		if (onlineCust == null) {
			throw new UserNotFoundException("Customer is not registered for online flow");
		}
		onlineCust = custDao.saveOrUpdateOnlineCustomer(onlineCust, model);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(convert(onlineCust));
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}

	@Override
	public Class<UserModel> getModelClass() {
		return UserModel.class;
	}

	public CustomerOnlineRegistration verifyCivilId(String civilId, CivilIdOtpModel model) {
		Customer cust = validateCustomerForOnlineFlow(civilId);

		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByCustomerId(cust.getCustomerId());
		if (onlineCust == null) {
			onlineCust = new CustomerOnlineRegistration(cust);
			onlineCust.setHresetBy(cust.getIdentityInt());
			onlineCust.setHresetIp(webutil.getClientIp());
			onlineCust.setHresetkDt(new Date());
		}
		model.setEmail(cust.getEmail());
		model.setMobile(cust.getMobile());
		model.setIsActiveCustomer("Y".equals(cust.getActivatedInd()) ? true : false);
		return onlineCust;
	}

	private Customer validateCustomerForOnlineFlow(String civilId) {
		Customer cust = custDao.getCustomerByCivilId(civilId);
		if (cust == null) {
			throw new UserNotFoundException("Civil id is not registered at branch, civil id no,: " + civilId);
		}
		if (cust.getMobile() == null) {
			throw new InvalidCivilIdException("Mobile number is empty. Contact branch to update the same.");
		}
		if (cust.getEmail() == null) {
			throw new InvalidCivilIdException("Email is empty. Contact branch to update the same.");
		}
		return cust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		validateCivilId(civilId);
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

	public void validateCivilId(String civilId) {
		boolean isValid = custValidation.validateCivilId(civilId, meta.getCountry().getCountryCode());
		if (!isValid) {
			throw new InvalidCivilIdException("Civil Id " + civilId + " is not valid.");
		}
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
		ApiResponse response = getBlackApiResponse();
		CustomerModel customerModel = convert(onlineCust);
		response.getData().getValues().add(customerModel);
		response.getData().setType(customerModel.getModelType());
		response.setResponseStatus(ResponseStatus.OK);
		logger.debug("end of validateopt for civilid: " + civilId);
		return response;
	}
}
