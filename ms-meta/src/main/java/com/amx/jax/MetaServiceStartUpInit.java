package com.amx.jax;


import java.math.BigDecimal;

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

	private  void initializeViewCompanyMaster() {
		BigDecimal languageId = new BigDecimal(1);
		CompanyService.DEFAULT_COMPANY_DETALIS = companyDao.getCompanyDetails(languageId);
		
	}
	
}
