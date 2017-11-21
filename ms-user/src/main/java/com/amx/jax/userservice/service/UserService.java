package com.amx.jax.userservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.dbmodel.CustomerOnlineRegistration;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.CustomerModel;
import com.amx.jax.model.SecurityQuestionModel;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.model.response.ResponseStatus;
import com.amx.jax.userservice.dao.AbstractUserDao;
import com.amx.jax.userservice.dao.CustomerDao;
import com.amx.jax.userservice.dao.KwUserDao;
import com.amx.jax.userservice.exception.UserNotFoundException;
import com.amx.jax.userservice.model.AbstractUserModel;
import com.amx.jax.userservice.model.UserModel;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserService extends AbstractUserService {

	@Autowired
	private KwUserDao dao;

	@Autowired
	private CustomerDao custDao;

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
		// model.setFirstName(cust.getFirstName());
		// model.setLastName(cust.getLastName());
		// model.setMiddleName(cust.getMiddleName());
		// model.setTitle(cust.getTitle());
		return model;
	}

	public AbstractModel convert(CustomerOnlineRegistration cust) {
		CustomerModel model = new CustomerModel();
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

	@Override
	public Class<UserModel> getModelClass() {
		return UserModel.class;
	}

	public ApiResponse verifyCivilId(String civilId) {

		Customer cust = custDao.getCustomerByCivilId(civilId);
		if (cust == null) {
			throw new UserNotFoundException("Civil id is not registered at branch, civil id no,: " + civilId);
		}
		CustomerOnlineRegistration onlineCust = custDao.getOnlineCustById(cust.getCustomerId());
		if (onlineCust == null) {
			// TODO create new online cust
		}
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(convert(onlineCust));
		response.setResponseStatus(ResponseStatus.OK);
		return null;
	}

}
