package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ViewExGLCBAL;
import com.amx.jax.pricer.repository.VwExGLCBALRepository;

@Component
public class ViewExGLCBALDao {

	@Autowired
	VwExGLCBALRepository vwExGLCBALRepository;

	public List<ViewExGLCBAL> getGLCBALforCurrencyAndBank(String currencyCode, List<BigDecimal> bankIds) {
		return vwExGLCBALRepository.findByCurrencyCodeAndBankIdIn(currencyCode, bankIds);
	}

}
