package com.amx.jax;


import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.service.CompanyService;

@Component
public class MetaServiceStartUpInit {

	@Autowired
	ICompanyDAO companyDao;

	@PostConstruct
	public void initializeMetaData() {
		initializeViewCompanyMaster();
	}

	private void initializeViewCompanyMaster() {
		CompanyService.ALL_COMPANY_DETALIS = companyDao.findAll();
	}
}
