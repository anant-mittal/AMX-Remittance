package com.amx.jax;

import java.math.BigDecimal;

import javax.annotation.PostConstruct;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.dbmodel.JaxConfig;
import com.amx.jax.repository.ICompanyDAO;
import com.amx.jax.service.CompanyService;
import com.amx.jax.util.ConverterUtil;

@Component
public class MetaServiceStartUpInit {

	@Autowired
	ICompanyDAO companyDao;

	@Autowired
	ConverterUtil converterutil;

	@Autowired
	OtpSettings otpSettings;

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostConstruct
	public void initializeMetaData() {
		initializeViewCompanyMaster();
	}

	private void initializeViewCompanyMaster() {
		BigDecimal languageId = new BigDecimal(1);
		CompanyService.DEFAULT_COMPANY_DETALIS = companyDao.getCompanyDetails(languageId);
	}

}
