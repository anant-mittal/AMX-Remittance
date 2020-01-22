package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.CurrencyOtherInfo;

@Transactional
public interface CurrencyOtherInfoRepository extends CrudRepository<CurrencyOtherInfo, Serializable> {

	CurrencyOtherInfo getByCurrencyId(BigDecimal currencyId);

	List<CurrencyOtherInfo> getByApplicationCountryIdAndCurrencyIdIn(BigDecimal countryId, List<BigDecimal> curIds);

}
