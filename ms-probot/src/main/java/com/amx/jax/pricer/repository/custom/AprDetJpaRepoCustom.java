package com.amx.jax.pricer.repository.custom;

import java.math.BigDecimal;
import java.util.List;

import com.amx.jax.pricer.dbmodel.ExchangeRateMasterApprovalDet;

public interface AprDetJpaRepoCustom {

	List<ExchangeRateMasterApprovalDet> findByPredicateIn(List<BigDecimal> predicateIn);

}
