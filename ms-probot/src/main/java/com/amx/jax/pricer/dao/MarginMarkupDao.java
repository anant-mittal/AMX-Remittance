package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;
import com.amx.jax.pricer.repository.MarginMarkupRepository;

@Component
public class MarginMarkupDao {

	@Autowired
	private MarginMarkupRepository marginMarkupRepository;

	public OnlineMarginMarkup getMarkupForCountryAndCurrencyAndBank(BigDecimal aplCountryId, BigDecimal countryId,
			BigDecimal currencyId, BigDecimal bankId) {
		List<OnlineMarginMarkup> marginList = marginMarkupRepository
				.findByApplicationCountryIdAndCountryIdAndCurrencyIdAndBankIdAndIsActive(aplCountryId, countryId,
						currencyId, bankId, "Y");

		// Only One Margin is valid at Any Point of time
		if (marginList != null && !marginList.isEmpty()) {
			return marginList.get(0);
		}

		return null;

	}

	public Map<BigDecimal, OnlineMarginMarkup> getMarkupForCurrencyAndBanksIn(BigDecimal aplCountryId,
			BigDecimal currencyId, List<BigDecimal> bankIds) {

		List<OnlineMarginMarkup> allBankMarkups = marginMarkupRepository
				.findByApplicationCountryIdAndCurrencyIdAndIsActiveAndBankIdIn(aplCountryId, currencyId, "Y", bankIds);

		Map<BigDecimal, OnlineMarginMarkup> bankMarkups = new HashMap<BigDecimal, OnlineMarginMarkup>();

		if (allBankMarkups != null && !allBankMarkups.isEmpty()) {
			for (OnlineMarginMarkup markup : allBankMarkups) {
				if (markup.getBankId() != null) {
					bankMarkups.put(markup.getBankId(), markup);
				}
			}
		}

		return bankMarkups;

	}

	public List<OnlineMarginMarkup> getMarkupForCountryAndCurrency(BigDecimal aplCountryId, BigDecimal countryId,
			BigDecimal currencyId) {

		return marginMarkupRepository.findByApplicationCountryIdAndCountryIdAndCurrencyId(aplCountryId, countryId,
				currencyId);

	}

	public OnlineMarginMarkup getMarkupData(BigDecimal aplCountryId, BigDecimal countryId, BigDecimal currencyId,
			BigDecimal bankId) {
		return marginMarkupRepository.getMarkupData(aplCountryId, countryId, currencyId, bankId);
	}

	public void saveOnlineMarginMarkup(OnlineMarginMarkup marginMarkup) {
		marginMarkupRepository.save(marginMarkup);
	}

}
