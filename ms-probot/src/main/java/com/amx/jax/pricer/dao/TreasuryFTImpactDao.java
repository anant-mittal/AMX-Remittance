package com.amx.jax.pricer.dao;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.repository.TreasuryFTImpactRepository;

public class TreasuryFTImpactDao {

	@Autowired
	TreasuryFTImpactRepository trFtImpactRepository;

	public TreasuryFundTimeImpact findByCountryIdAndCurrencyIdAndFundStatusAndIsActive(BigDecimal countryId,
			BigDecimal currencyId, String FundStatus) {

		return trFtImpactRepository.findByCountryIdAndCurrencyIdAndFundStatusAndIsActive(countryId, currencyId,
				FundStatus, "Y");
	}

}
