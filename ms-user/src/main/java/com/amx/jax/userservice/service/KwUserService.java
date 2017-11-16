package com.amx.jax.userservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.Customer;
import com.amx.jax.model.AbstractModel;
import com.amx.jax.model.response.ApiResponse;
import com.amx.jax.model.response.ResponseStatus;
import com.amx.jax.userservice.dal.AbstractUserDao;
import com.amx.jax.userservice.dal.KwUserDao;
import com.amx.jax.userservice.model.AbstractUserModel;
import com.amx.jax.userservice.model.KwUserModel;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class KwUserService extends AbstractUserService {

	@Autowired
	private KwUserDao dao;

	@Override
	public ApiResponse registerUser(AbstractUserModel userModel) {
		KwUserModel kwUserModel = (KwUserModel) userModel;
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
		KwUserModel model = new KwUserModel();
		model.setFirstName(cust.getFirstName());
		model.setLastName(cust.getLastName());
		model.setMiddleName(cust.getMiddleName());
		model.setTitle(cust.getTitle());
		return model;
	}

	@Override
	public Class<KwUserModel> getModelClass() {
		return KwUserModel.class;
	}

	@Override
	public boolean validateCivilId(String civilId) {
		boolean isValid = true;
		if (StringUtils.isEmpty(civilId)) {
			isValid = false;
		}
		if (civilId.length() != 12) {
			isValid = false;
		}
		int idcheckdigit = Character.getNumericValue(civilId.charAt(11));
		int[] multiFactor = { 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2 };
		int cal = 0;
		for (int i = 0; i < 11; i++) {
			cal += multiFactor[i] * Character.getNumericValue(civilId.charAt(i));
		}
		int rem = cal % 11;
		int calcheckdigit = 11 - rem;
		if (calcheckdigit > 0 && calcheckdigit < 10) {
			isValid = false;
		} else {
			if (calcheckdigit != idcheckdigit) {
				isValid = false;
			}
		}

		return isValid;
	}

}
