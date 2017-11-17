package com.amx.jax.userservice.service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.model.response.ResponseStatus;
import com.amx.jax.services.AbstractService;
import com.amx.jax.userservice.dal.AbstractUserDao;
import com.amx.jax.userservice.model.AbstractUserModel;

public abstract class AbstractUserService extends AbstractService {

	public abstract ApiResponse registerUser(AbstractUserModel userModel);

	public ApiResponse getUser(Long userId) {
		Customer user = getDao().getUser(userId);
		AbstractModel model = convert(user);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(model);
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}
	
	public ApiResponse editUser(AbstractUserModel userModel) {
		Customer cust = getDao().saveOrUpdateUser(userModel);
		ApiResponse response = getBlackApiResponse();
		response.getData().getValues().add(convert(cust));
		response.setResponseStatus(ResponseStatus.OK);
		return response;
	}


	public abstract AbstractUserDao getDao();

	public abstract AbstractModel convert(Customer cust);

	@Override
	public String getModelType() {
		return "user";
	}

}
