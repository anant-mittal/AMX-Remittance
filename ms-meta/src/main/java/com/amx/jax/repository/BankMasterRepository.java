package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterMdlv1;


public interface BankMasterRepository extends CrudRepository<BankMasterMdlv1, BigDecimal> {

	public List<BankMasterMdlv1> findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(BigDecimal bankCountryId, String isActive);
	
	public BankMasterMdlv1 findByBankCodeAndRecordStatus(String bankCode,String isActive);
	
	public List<BankMasterMdlv1> findBybankCountryIdAndLanguageInd(BigDecimal bankCountryId, String languageInd);

	@Query(value = "select T1.* from ex_bank_master T1 inner join V_BENE_SERVICE_CURRENCY T2 on t1.bank_country_id = t2.bene_country_id"
			+ " where t1.bank_country_id=?1 and t2.currency_id = ?2", nativeQuery = true)
	public List<BankMasterMdlv1> findBankByCountryCurrency(BigDecimal countryId, BigDecimal currencyId);
	

}
