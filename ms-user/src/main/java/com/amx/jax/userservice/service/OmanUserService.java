package com.amx.jax.userservice.service;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.userservice.dal.AbstractUserDao;
import com.amx.jax.userservice.model.AbstractUserModel;

public class OmanUserService extends AbstractUserService {

	@Override
	public ApiResponse registerUser(AbstractUserModel userModel) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractUserDao getDao() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AbstractModel convert(Customer cust) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<?> getModelClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean validateCivilId(String civilId) {
		return true;
	}

}
