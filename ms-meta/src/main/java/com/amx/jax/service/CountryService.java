package com.amx.jax.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.model.CountryMasterView;
import com.amx.jax.repository.CountryRepository;
import com.amx.jax.services.AbstractService;
/**
 * 
 * @author Rabil
 *
 */

@Service
public class CountryService extends AbstractService {
	@Autowired
	CountryRepository countryRepository;
	
	
	public List<CountryMasterView> getCountryList(){
		return countryRepository.findAll();
		
	}
	
	/*public CountryMasterView getBusinessCountry(Long languageId){
		return countryRepository.findOne(languageId);
		
	}*/
	
	public List<CountryMasterView> getCountryByLanguageId(BigDecimal languageId){
		return countryRepository.findByLanguageId(languageId);
		
	}
	
	public List<CountryMasterView> getCountryByLanguageIdAndCountryId(BigDecimal languageId,BigDecimal countryId){
		return countryRepository.findByLanguageIdAndCountryId(languageId,countryId);
		
	}
	
	public List<CountryMasterView> getBusinessCountry(BigDecimal languageId){
		return countryRepository.getBusinessCountry(languageId);
		
	}

	@Override
	public String getModelType() {
		return "country";
	}

	@Override
	public Class<?> getModelClass() {
		return null;
	}
	
	
	
	
	
	
	

	
	
	
	
	
	

}
