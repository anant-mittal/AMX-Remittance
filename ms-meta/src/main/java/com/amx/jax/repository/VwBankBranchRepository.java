package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.BankBranchView;


public interface VwBankBranchRepository extends CrudRepository<BankBranchView, BigDecimal> {

	List<BankBranchView> findByCountryIdAndBankIdAndIfscCodeIgnoreCaseLike(BigDecimal countryId, BigDecimal bankId, String ifsc, Sort sort);

	List<BankBranchView> findByCountryIdAndBankIdAndSwiftIgnoreCaseLike(BigDecimal countryId, BigDecimal bankId, String swift, Sort sort);

	List<BankBranchView> findByCountryIdAndBankIdAndBranchFullNameIgnoreCaseLike(BigDecimal countryId, BigDecimal bankId,
			String branchName, Sort sort);
	
	List<BankBranchView> findByCountryIdAndBankId(BigDecimal countryId, BigDecimal bankId,  Sort sort);
	
	List<BankBranchView> findByCountryIdAndBankIdAndIfscCode(BigDecimal countryId, BigDecimal bankId, String ifsc, Sort sort);

	List<BankBranchView> findByCountryIdAndBankIdAndSwift(BigDecimal countryId, BigDecimal bankId, String swift, Sort sort);
}
