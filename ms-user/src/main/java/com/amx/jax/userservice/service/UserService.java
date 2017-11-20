package com.amx.jax.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.AbstractModel;
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
//		model.setFirstName(cust.getFirstName());
//		model.setLastName(cust.getLastName());
//		model.setMiddleName(cust.getMiddleName());
//		model.setTitle(cust.getTitle());
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
		return null;
	}
	
}
