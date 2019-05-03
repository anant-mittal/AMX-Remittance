package com.amx.jax.repository;

/**
 * @author rabil
 * 
 */
import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.AmiecAndBankMapping;

public interface IAmiecAndBankMappingRepository  extends CrudRepository<AmiecAndBankMapping, Serializable>{
	
	public AmiecAndBankMapping findByCountryIdAndBankIdAndFlexFieldAndAmiecCodeAndIsActive(CountryMaster countryId,BankMasterModel bankId,String flexField,String amiecCode,String isActive);
	

}
