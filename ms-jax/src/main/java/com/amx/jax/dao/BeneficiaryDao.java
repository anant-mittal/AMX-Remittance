package com.amx.jax.dao;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.SwiftMasterView;
import com.amx.jax.repository.IBankBranchView;
import com.amx.jax.repository.ISwiftMasterDao;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneficiaryDao {

	@Autowired
	ISwiftMasterDao swiftMasterRepo;

	public SwiftMasterView getSwiftMasterBySwiftBic(String swiftBic) {
		return swiftMasterRepo.getSwiftMasterDetails(swiftBic).get(0);
	}
}
