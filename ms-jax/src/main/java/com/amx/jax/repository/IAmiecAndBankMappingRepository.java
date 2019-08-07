package com.amx.jax.repository;

/**
 * @author rabil
 * 
 */
import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;
import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.remittance.AmiecAndBankMapping;

public interface IAmiecAndBankMappingRepository  extends CrudRepository<AmiecAndBankMapping, Serializable>{
	
	public AmiecAndBankMapping findByCountryIdAndBankIdAndFlexFieldAndAmiecCodeAndIsActive(CountryMaster countryId,BankMasterModel bankId,String flexField,String amiecCode,String isActive);
	
	@Query(value = "SELECT * FROM EX_AMIEC_AND_BANK_MAPPING WHERE COUNTRY_ID=?1 AND BANK_ID=?2 AND FLEX_FIELD=?3 AND AMIEC_CODE=?4 AND ISACTIVE=?5", nativeQuery = true)
	public AmiecAndBankMapping fetchAmiecBankData(BigDecimal countryId,BigDecimal bankId,String flexField,String amiecCode,String isActive);
	

}
