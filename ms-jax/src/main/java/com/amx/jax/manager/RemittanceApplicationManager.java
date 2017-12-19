package com.amx.jax.manager;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceApplicationManager {

	public void createRemittanceApplication(RemittanceTransactionRequestModel model) {
		
		RemittanceApplication application = new RemittanceApplication();
		application.setDocumentCode(null);
	}
}
