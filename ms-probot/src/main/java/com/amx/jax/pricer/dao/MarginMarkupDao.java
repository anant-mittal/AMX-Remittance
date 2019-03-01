package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.repository.MarginMarkupRepository;

@Component
public class MarginMarkupDao {

	@Autowired
	MarginMarkupRepository marginMarkupRepository;

	public OnlineMarginMarkup getMarkupForCountryAndCurrency(BigDecimal aplCountryId, BigDecimal countryId,
			BigDecimal currencyId) {
		List<OnlineMarginMarkup> marginList = marginMarkupRepository
				.findByApplicationCountryIdAndCountryIdAndCurrencyId(aplCountryId, countryId, currencyId);

		// Only One Margin is valid at Any Point of time
		if (marginList != null && !marginList.isEmpty()) {
			return marginList.get(0);
		}

		return null;

	}

}
