package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForSession;
import com.amx.jax.pricer.dbmodel.TreasuryFundTimeImpact;
import com.amx.jax.pricer.repository.TreasuryFTImpactRepository;
import com.amx.jax.pricer.var.PricerServiceConstants.TREASURY_FUND_STATUS;

@Component
public class TreasuryFTImpactDao {

	@Autowired
	TreasuryFTImpactRepository trFtImpactRepository;

	public TreasuryFundTimeImpact findByCountryIdAndCurrencyIdAndFundStatus(BigDecimal countryId, BigDecimal currencyId,
			String FundStatus) {

		return trFtImpactRepository.findByCountryIdAndCurrencyIdAndFundStatusAndIsActive(countryId, currencyId,
				FundStatus, "Y");
	}

	@CacheForSession
	public Map<TREASURY_FUND_STATUS, TreasuryFundTimeImpact> findByCountryIdAndCurrencyId(BigDecimal countryId,
			BigDecimal currencyId) {

		List<TreasuryFundTimeImpact> ftImpacts = trFtImpactRepository.findByCountryIdAndCurrencyIdAndIsActive(countryId,
				currencyId, "Y");

		Map<TREASURY_FUND_STATUS, TreasuryFundTimeImpact> impactMap = new HashMap<TREASURY_FUND_STATUS, TreasuryFundTimeImpact>();

		if (ftImpacts != null && !ftImpacts.isEmpty()) {
			for (TreasuryFundTimeImpact impact : ftImpacts) {
				if (TREASURY_FUND_STATUS.FUNDED.toString().equalsIgnoreCase(impact.getFundStatus())) {
					impactMap.put(TREASURY_FUND_STATUS.FUNDED, impact);
				} else if (TREASURY_FUND_STATUS.OUT_OF_FUND.toString().equalsIgnoreCase(impact.getFundStatus())) {
					impactMap.put(TREASURY_FUND_STATUS.OUT_OF_FUND, impact);
				}
			}
		}

		return impactMap;
	}

}
