package com.amx.jax.repository;

/**
 * @author :Rabil
 * Date   :04/11/2018
 * purpose :to get the fc sale exchange rate
 */
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.FxExchangeRateView;

public interface FcSaleExchangeRateRepository extends CrudRepository<FxExchangeRateView, Serializable>{

	public List<FxExchangeRateView> findByApplicationCountryIdAndCountryBranchIdAndFxCurrencyId(BigDecimal applicationCountryId,BigDecimal countryBranchId,BigDecimal fxCurrencyId);

	
}
