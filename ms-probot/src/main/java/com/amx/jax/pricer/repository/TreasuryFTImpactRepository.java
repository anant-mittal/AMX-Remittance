package com.amx.jax.pricer.repository;

import java.math.BigDecimal;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;

public interface TreasuryFTImpactRepository extends CrudRepository<TreasuryFundTimeImpact, BigDecimal> {

	public TreasuryFundTimeImpact findByCountryIdAndCurrencyIdAndFundStatusAndIsActive(BigDecimal countryId, BigDecimal currencyId,
			String FundStatus, String isActive);

}
