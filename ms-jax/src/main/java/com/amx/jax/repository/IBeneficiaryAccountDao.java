package com.amx.jax.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.amx.jax.dbmodel.bene.BeneficaryAccount;

public interface IBeneficiaryAccountDao extends JpaRepository<BeneficaryAccount, Serializable> {

	@Query("select ba from BeneficaryAccount ba where ba.beneApplicationCountryId=:applicationCountryId "
			+ "and ba.beneficaryMasterId=:beneMasterSeqId and ba.beneficaryAccountSeqId=:beneAccountSeqId and ba.isActive='Y'")
	public List<BeneficaryAccount> getBeneficiaryByBeneAccountId(@Param("beneAccountSeqId") BigDecimal beneAccountSeqId,
			@Param("beneMasterSeqId") BigDecimal beneMasterSeqId,
			@Param("applicationCountryId") BigDecimal applicationCountryId);

	public List<BeneficaryAccount> findByBeneficaryCountryIdAndBankIdAndCurrencyIdAndBankBranchIdAndBankAccountNumberAndIsActive(
			BigDecimal countryId, BigDecimal bankId, BigDecimal currencyId, BigDecimal branchId,
			String bankAccountNumber,String isActive);

	public List<BeneficaryAccount> findByServiceGroupIdAndBeneficaryCountryIdAndBankIdAndCurrencyIdAndBankBranchIdAndBankAccountNumberAndIsActive(
			BigDecimal serviceGroupId, BigDecimal countryId, BigDecimal bankId, BigDecimal currencyId,
			BigDecimal branchId, String bankAccountNumber, String isActive);
}
