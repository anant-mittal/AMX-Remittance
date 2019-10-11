package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankMasterMdlv1;


public interface BankMasterRepository extends CrudRepository<BankMasterMdlv1, BigDecimal> {

	public List<BankMasterMdlv1> findBybankCountryIdAndRecordStatusOrderByBankShortNameAsc(BigDecimal bankCountryId, String isActive);
	
	public BankMasterMdlv1 findByBankCodeAndRecordStatus(String bankCode,String isActive);
	
	public List<BankMasterMdlv1> findBybankCountryIdAndLanguageInd(BigDecimal bankCountryId, String languageInd);
	

}
