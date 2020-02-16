package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.CurrencyOtherInfo;
import com.amx.jax.pricer.repository.CurrencyOtherInfoRepository;

@Component
public class CurrencyOtherInfoDao {

	@Autowired
	CurrencyOtherInfoRepository repository;

	public CurrencyOtherInfo getByCurrencyId(BigDecimal currencyId) {
		return repository.getByCurrencyId(currencyId);
	}

	public Map<BigDecimal, CurrencyOtherInfo> getByApplicationCountryIdAndCurrencyIdIn(BigDecimal applCountryId,
			List<BigDecimal> curIds) {
		List<CurrencyOtherInfo> curInfoList = repository.getByApplicationCountryIdAndCurrencyIdIn(applCountryId,
				curIds);

		if (curInfoList == null || curInfoList.isEmpty()) {
			return new HashMap<BigDecimal, CurrencyOtherInfo>();
		}

		return curInfoList.stream().collect(Collectors.toMap(i -> i.getCurrencyId(), i -> i));

	}

}
