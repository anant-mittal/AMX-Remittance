package com.amx.jax.userservice.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.CustomerHomeAddress;
import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.AppConstants;
import com.amx.jax.cache.CustomerTransactionModel;
import com.amx.jax.constant.JaxTransactionModel;
import com.amx.jax.trnx.CustomerRegistrationTrnxModel;
import com.amx.jax.trnx.model.OtpData;
import com.amx.utils.ArgUtil;
import com.amx.utils.ContextUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class CustomerRegistrationManager extends CustomerTransactionModel<CustomerRegistrationTrnxModel> {

	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRegistrationManager.class);

	private String identityInt;

	@Override
	public CustomerRegistrationTrnxModel init() {
		CustomerRegistrationTrnxModel model = get();
		if (model == null) {
			model = new CustomerRegistrationTrnxModel();
			model.setOtpData(new OtpData());
			save(model);
		}
		return model;
	}

	public CustomerRegistrationTrnxModel init(CustomerPersonalDetail customerPersonalDetail) {
		CustomerRegistrationTrnxModel model = get();
		if (model == null) {
			model = new CustomerRegistrationTrnxModel();
		}
		model.setCustomerPersonalDetail(customerPersonalDetail);
		save(model);
		return model;
	}

	@Override
	public CustomerRegistrationTrnxModel commit() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getJaxTransactionId() {
		return JaxTransactionModel.CUSTOMER_REGISTRATION_MODEL.toString() + "_" + identityInt;
	}

	@Override
	protected String getTranxId() {
		String key = ArgUtil.parseAsString(ContextUtil.map().get(AppConstants.TRANX_ID_XKEY));
		if (ArgUtil.isEmptyString(key)) {
			key = getJaxTransactionId();
			ContextUtil.map().put(AppConstants.TRANX_ID_XKEY, key);
			LOGGER.info("************ Creating New Tranx Id {} *******************", key);
		}
		return super.getTranxId();
	}

	public String getIdentityInt() {
		return identityInt;
	}

	public void setIdentityInt(String identityInt) {
		this.identityInt = identityInt;
	}

	public void saveOtpData(OtpData otpData) {
		CustomerRegistrationTrnxModel model = get();
		model.setOtpData(otpData);
		save(model);
	}

	public void saveHomeAddress(CustomerHomeAddress addr) {
		CustomerRegistrationTrnxModel model = get();
		model.setCustomerHomeAddress(addr);
		save(model);
	}
}
