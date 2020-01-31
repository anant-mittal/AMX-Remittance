package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.VwExRoutingProduct;
import com.amx.jax.pricer.repository.RoutingProdStatusRepo;

@Component
public class RoutingProdStatusDao {

	@Autowired
	RoutingProdStatusRepo repo;

	public List<VwExRoutingProduct> getByCurrencyId(BigDecimal currencyId) {
		return repo.findByCurrencyId(currencyId);
	}

	public List<VwExRoutingProduct> getByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId) {
		return repo.findByCurrencyIdAndCountryId(currencyId, countryId);
	}

	public List<VwExRoutingProduct> getByCurrencyIdAndDestinationCountryId(BigDecimal currencyId,
			BigDecimal dCountryId) {
		return repo.findByCurrencyIdAndDestinationCountryId(currencyId, dCountryId);
	}

	public List<VwExRoutingProduct> getByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(countryId, currencyId, bankId,
				ServiceModeId, remitModeId);
	}

	public List<VwExRoutingProduct> getByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId, BigDecimal deliveryModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(countryId,
				currencyId, bankId, ServiceModeId, remitModeId, deliveryModeId);
	}

}
