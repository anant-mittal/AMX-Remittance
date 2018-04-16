package com.amx.jax.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.JaxApplicationSetup;
import com.amx.jax.dbmodel.ApplicationSetup;
import com.amx.jax.dbmodel.CurrencyMasterModel;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.meta.ApplicationSetupRepository;

@Service
public class ApplicationSetupService {

	@Autowired
	ApplicationSetupRepository applicationSetupRepository;

	@Autowired
	MetaData metaData;

	@Autowired
	CurrencyMasterService currencyMasterService;

	public ApplicationSetup getApplicationSetUp() {
		return applicationSetupRepository.findByApplicationCountryId(metaData.getCountryId()).get(0);
	}

	/*public void initializeApplicationSetUp() {
		ApplicationSetup applicationSetup = getApplicationSetUp();
		BigDecimal appCountryId = applicationSetup.getApplicationCountryId();
		jaxApplicationSetup.setApplicationCountryId(appCountryId);
		CurrencyMasterModel currencyMaster = currencyMasterService.getCurrencyMasterByCountryId(appCountryId).get(0);
		jaxApplicationSetup.setCurrencyId(currencyMaster.getCurrencyId());
		jaxApplicationSetup.setCurrencyQuote(currencyMaster.getQuoteName());
		jaxApplicationSetup.setOnlineFeedBackEmail(applicationSetup.getOnlineFeedbackEmail());

	}*/
}
