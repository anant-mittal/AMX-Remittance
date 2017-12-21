package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dao.BankDao;
import com.amx.jax.dbmodel.BankBranchView;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BankService {

	@Autowired
	BankDao bankDao;

	public String getBranchSwiftCode(BigDecimal bankId, BigDecimal bankBranchId) {
		BankBranchView branch = bankDao.getBankBranchById(bankId, bankBranchId);
		String swift = null;
		if (branch != null) {
			swift = branch.getSwift();
		}
		return swift;
	}
}
