package com.amx.jax.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.AdditionalBankRuleAmiec;



public interface IAdditionalBankRuleAmiecRepository extends CrudRepository<AdditionalBankRuleAmiec, Serializable>{
	
	@Query("select r from AdditionalBankRuleAmiec r where countryId=?1 and r.flexField='INDIC1' and r.isActive='Y' order by r.amiecDescription")
	List<AdditionalBankRuleAmiec> getPurposeOfTrnxByCountryId(CountryMaster countryMaster);
	
	

}
