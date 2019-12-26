package com.amx.jax.auth;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.exception.AbstractJaxException;
import com.amx.amxlib.model.CustomerModel;
import com.amx.jax.util.JaxContextUtil;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class AuthFailureLogManager {

	public void logAuthFailureEvent(AbstractJaxException ex) {
		String identityInt = null;
		Object requestModel = JaxContextUtil.getRequestModel();
		if (requestModel instanceof CustomerModel) {
			CustomerModel customerModel = (CustomerModel) requestModel;
			String civilId = customerModel.getLoginId();
			identityInt = civilId;
		}
		// TODO: create jaxauthfailure record in db

	}

}
