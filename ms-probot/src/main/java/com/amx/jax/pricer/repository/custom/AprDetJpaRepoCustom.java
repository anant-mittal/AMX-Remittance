package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;

public interface AprDetJpaRepoCustom {

	List<ExchangeRateMasterApprovalDet> findByPredicateIn(List<BigDecimal> predicateIn);

	List<ExchangeRateMasterApprovalDet> getExchangeRatesForPredicates(BigDecimal countryId, BigDecimal currencyId,
			BigDecimal bankId, BigDecimal serviceIndId, BigDecimal countryBranchId, Pageable pageable);

}
