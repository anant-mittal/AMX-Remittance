package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankBranchView;

public interface VwBankBranchRepository extends CrudRepository<BankBranchView, BigDecimal> {

	List<BankBranchView> findByCountryIdAndBankIdAndIfscCode(BigDecimal countryId, BigDecimal bankId, String ifsc);

	List<BankBranchView> findByCountryIdAndBankIdAndSwift(BigDecimal countryId, BigDecimal bankId, String swift);

	List<BankBranchView> findByCountryIdAndBankIdAndBranchFullNameLike(BigDecimal countryId, BigDecimal bankId,
			String branchName);
	
	List<BankBranchView> findByCountryIdAndBankId(BigDecimal countryId, BigDecimal bankId);
}
