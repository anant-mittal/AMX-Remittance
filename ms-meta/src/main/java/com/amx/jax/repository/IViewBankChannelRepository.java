package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ViewBankChannelModel;

public interface IViewBankChannelRepository extends CrudRepository<ViewBankChannelModel, BigDecimal> {

	public List<ViewBankChannelModel> findBybankCountryIdOrderByBankShortNameAsc(BigDecimal bankCountryId);

	public List<ViewBankChannelModel> findBybankCountryIdAndLanguageInd(BigDecimal countryId, String languageInd);

	@Query(value = "select distinct T1.* from VW_EX_CHANNEL_BANKS T1 inner join V_BENE_SERVICE_CURRENCY T2 on t1.bank_country_id = t2.bene_country_id"
			+ " where t1.bank_country_id=?1 and t2.currency_id = ?2", nativeQuery = true)
	public List<ViewBankChannelModel> findByCountryAndCurrency(BigDecimal countryId, BigDecimal currencyId);
}
