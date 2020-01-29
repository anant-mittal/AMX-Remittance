package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.def.CacheForSession;
import com.amx.jax.pricer.dbmodel.ViewExGLCBalProvisional;
import com.amx.jax.pricer.repository.VwExGLCBalProvRepository;

@Component
public class VwExGLCBalProvDao {

	@Autowired
	private VwExGLCBalProvRepository provRepository;

	@CacheForSession
	public Map<BigDecimal, ViewExGLCBalProvisional> getByCurrencyCodeAndBankIdIn(String currencyCode,
			List<BigDecimal> bankIds) {

		List<ViewExGLCBalProvisional> provisionalBalList = provRepository.findByCurrencyCodeAndBankIdIn(currencyCode,
				bankIds);

		Map<BigDecimal, ViewExGLCBalProvisional> bankProvisionalBal = new HashMap<BigDecimal, ViewExGLCBalProvisional>();

		if (provisionalBalList != null && !provisionalBalList.isEmpty()) {
			for (ViewExGLCBalProvisional provisional : provisionalBalList) {
				bankProvisionalBal.put(provisional.getBankId(), provisional);
			}
		}

		return bankProvisionalBal;
	}

}
