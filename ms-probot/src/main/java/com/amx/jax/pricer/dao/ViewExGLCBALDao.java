package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForSession;
import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.repository.VwExGLCBALRepository;

@Component
public class ViewExGLCBALDao {

	@Autowired
	private VwExGLCBALRepository vwExGLCBALRepository;

	@CacheForSession
	public List<ViewExGLCBAL> getGLCBALforCurrencyAndBanks(String currencyCode, List<BigDecimal> bankIds) {
		return vwExGLCBALRepository.findByCurrencyCodeAndBankIdIn(currencyCode, bankIds);
	}

	@CacheForSession
	public List<ViewExGLCBAL> getGLCBALforCurrencyAndBank(String currencyCode, BigDecimal bankId) {
		return vwExGLCBALRepository.findByCurrencyCodeAndBankId(currencyCode, bankId);
	}

	public List<ViewExGLCBAL> getGLCBALforCurrenciesAndBanks(List<String> currencyCodes, List<BigDecimal> bankIds) {
		return vwExGLCBALRepository.findByCurrencyCodeInAndBankIdIn(currencyCodes, bankIds);
	}

}
