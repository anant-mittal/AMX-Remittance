package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.ApplicationSetup;
import com.amx.jax.model.CountryMasterView;
import com.amx.jax.repository.IApplicationCountryRepository;

@Service
public class ApplicationCountryService {
	
	
	@Autowired
	IApplicationCountryRepository applicationCountryRepository;
	
	

	public List<ApplicationSetup> getApplicationCountryList(){
		return applicationCountryRepository.findAll();
	}
	
	public List<ApplicationSetup> getApplicationCountry(BigDecimal companyId,BigDecimal countryId){
		return applicationCountryRepository.findByCountryIdAndCompanyId(companyId,countryId);
	}

}
