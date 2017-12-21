package com.amx.jax.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceApplicationDao {

	@Autowired
	RemittanceApplicationRepository repo;

	public void saveApplication(RemittanceApplication app) {
		repo.save(app);
	}

}
