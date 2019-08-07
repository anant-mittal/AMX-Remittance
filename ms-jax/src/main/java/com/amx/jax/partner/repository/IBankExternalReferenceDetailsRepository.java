package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.partner.BankExternalReferenceDetail;

public interface IBankExternalReferenceDetailsRepository extends CrudRepository<BankExternalReferenceDetail, Serializable>{
	
	@Query(value = "SELECT * FROM EX_BANK_EXTERNAL_REF_HEAD WHERE COUNTRY_ID =?1 AND BANK_ID=?2 AND BENEFICARY_BANK_ID=?3 AND BENEFICARY_BRANCH_ID=?4 AND ISACTIVE = 'Y'", nativeQuery = true)
	public List<BankExternalReferenceDetail> fetchBankExternalBranchDetails(BigDecimal countryId,BigDecimal corBankId,BigDecimal beneBankId,BigDecimal beneBankBranchId);

}
