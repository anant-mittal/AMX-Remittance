package com.amx.jax.userservice.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.AbstractModel;
import com.amx.amxlib.model.CivilIdOtpModel;
import com.amx.amxlib.model.CustomerModel;
import com.amx.amxlib.model.SecurityQuestionModel;
import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.ResponseStatus;
import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.meta.MetaData;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.KwUserDao;
import com.amx.jax.userservice.exception.InvalidCivilIdException;
import com.amx.jax.userservice.exception.UserNotFoundException;
import com.amx.jax.userservice.model.AbstractUserModel;
import com.amx.jax.userservice.model.UserModel;
import com.amx.jax.util.CryptoUtil;
import com.amx.jax.util.Util;
import com.amx.jax.util.validation.CustomerValidation;

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
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustByUserId(model.getIdentityId());
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

	public CustomerOnlineRegistration verifyCivilId(String civilId) {

		Customer cust = custDao.getCustomerByCivilId(civilId);
		if (cust == null) {
			throw new UserNotFoundException("Civil id is not registered at branch, civil id no,: " + civilId);
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustById(cust.getCustomerId());
		if (onlineCust == null) {
			onlineCust = new CustomerOnlineRegistration(cust);
		}
		return onlineCust;
	}

	public ApiResponse sendOtpForCivilId(String civilId) {
		validateCivilId(civilId);
		CustomerOnlineRegistration onlineCust = verifyCivilId(civilId);
		CivilIdOtpModel model = new CivilIdOtpModel();
		generateToken(civilId, model);
		onlineCust.setEmailToken(model.getHashedOtp());
		onlineCust.setMobileNumber(model.getHashedOtp());
		custDao.saveOnlineCustomer(onlineCust);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
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
	
	public void validagteOtp() {}

}
