package com.amx.jax.rbaac.manager;

import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.cache.TransactionModel;
import com.amx.jax.model.OtpData;
import com.amx.jax.rbaac.dbmodel.FSEmployee;
import com.amx.jax.rbaac.dbmodel.RoleDefinition;
import com.amx.jax.rbaac.dbmodel.UserRoleMaster;
import com.amx.jax.rbaac.trnx.AuthLoginTrnxModel;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthLoginManager extends TransactionModel<AuthLoginTrnxModel> {

	@Override
	public AuthLoginTrnxModel init() {
		AuthLoginTrnxModel model = get();
		if (model == null) {
			model = new AuthLoginTrnxModel();
			model.setOtpData(new OtpData());
			save(model);
		}
		return model;
	}

	@Override
	public AuthLoginTrnxModel commit() {
		return this.get();
	}
	
	/**
	 * saves otp data in trnx
	 */
	public void saveOtpData(OtpData otpData) {
		AuthLoginTrnxModel model = get();
		model.setOtpData(otpData);
		save(model);
	}
	
	/**
	 * Init method
	 */
	public AuthLoginTrnxModel init(FSEmployee empDetails) {
		AuthLoginTrnxModel model = get();
		if (model == null) {
			model = new AuthLoginTrnxModel();
			model.setOtpData(new OtpData());
		}
		model.setEmpDetails(empDetails);
		save(model);
		return model;
	}
	
	/**
	 * set emp,user,role method
	 */
	public AuthLoginTrnxModel fetchEmployeeDetails(FSEmployee empDetails,UserRoleMaster userMaster,List<RoleDefinition> roleDef) {
		AuthLoginTrnxModel model = get();
		if (model == null) {
			model = new AuthLoginTrnxModel();
			model.setOtpData(new OtpData());
		}
		model.setEmpDetails(empDetails);
		model.setUserMaster(userMaster);
		model.setRoleDefinition(roleDef);
		save(model);
		return model;
	}

}
