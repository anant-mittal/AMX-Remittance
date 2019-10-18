package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterModel;


public interface BankMasterRepository extends CrudRepository<BankMasterModel, BigDecimal> {

	public List<BankMasterModel> findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(BigDecimal bankCountryId, String isActive);
	
	public BankMasterModel findByBankCodeAndRecordStatus(String bankCode,String isActive);
	
	@Query(value = "select T1.* from ex_bank_master T1 inner join V_BENE_SERVICE_CURRENCY T2 on t1.bank_country_id = t2.bene_country_id"
			+ " where t1.bank_country_id=?1 and t2.currency_id = ?2", nativeQuery = true)
	public List<BankMasterModel> findBankByCountryCurrency(BigDecimal countryId, BigDecimal currencyId);

	public List<BankMasterModel> findBybankCountryIdAndLanguageInd(BigDecimal bankCountryId, String languageInd);
	

}
