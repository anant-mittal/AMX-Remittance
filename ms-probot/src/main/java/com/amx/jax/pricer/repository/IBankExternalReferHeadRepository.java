package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.BankExternalReferenceHead;

public interface IBankExternalReferHeadRepository extends CrudRepository<BankExternalReferenceHead, Serializable>{
	
	@Query(value = "SELECT * FROM EX_BANK_EXTERNAL_REF_HEAD WHERE COUNTRY_ID =?1 AND BANK_ID=?2 AND BENEFICARY_BANK_ID=?3 AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<BankExternalReferenceHead> fetchBankExternalHeaderDetails(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId);

}
