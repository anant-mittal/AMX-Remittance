package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.amx.jax.pricer.dbmodel.OnlineMarginMarkup;

@Transactional
public interface MarginMarkupRepository extends CrudRepository<OnlineMarginMarkup, BigDecimal> {

	List<OnlineMarginMarkup> findByApplicationCountryIdAndCountryIdAndCurrencyId(BigDecimal aplCountryId,
			BigDecimal countryId, BigDecimal currencyId);

	List<OnlineMarginMarkup> findByApplicationCountryIdAndCountryIdAndCurrencyIdAndBankId(BigDecimal aplCountryId,
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId);

	List<OnlineMarginMarkup> findByApplicationCountryIdAndCurrencyIdAndBankIdIn(BigDecimal aplCountryId,
			BigDecimal currencyId, List<BigDecimal> bankIds);
	
	@Query(value = "select * from EX_ONLINE_MARGIN_MARKUP where COUNTRY_ID=?1 and CURRENCY_ID=?2 and BANK_ID=?3 and "
			+ "ISACTIVE='Y'", nativeQuery = true)
	public OnlineMarginMarkup getMarkupData(BigDecimal countryId, BigDecimal currencyId,BigDecimal bankId);

}
