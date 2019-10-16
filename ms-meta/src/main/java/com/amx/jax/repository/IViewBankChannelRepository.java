package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ViewBankChannelModel;

public interface IViewBankChannelRepository extends CrudRepository<ViewBankChannelModel, BigDecimal> {

	public List<ViewBankChannelModel> findBybankCountryIdOrderByBankShortNameAsc(BigDecimal bankCountryId);

	public List<ViewBankChannelModel> findBybankCountryIdAndLanguageInd(BigDecimal countryId, String languageInd);
}
