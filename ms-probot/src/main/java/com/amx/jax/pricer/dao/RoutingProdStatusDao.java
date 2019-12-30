package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.VwExRoutingProductStatus;
import com.amx.jax.pricer.repository.RoutingProdStatusRepo;

@Component
public class RoutingProdStatusDao {

	@Autowired
	RoutingProdStatusRepo repo;

	public List<VwExRoutingProductStatus> getByCurrencyId(BigDecimal currencyId) {
		return repo.findByCurrencyId(currencyId);
	}

	public List<VwExRoutingProductStatus> getByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId) {
		return repo.findByCurrencyIdAndCountryId(currencyId, countryId);
	}

	public List<VwExRoutingProductStatus> getByCurrencyIdAndDestinationCountryId(BigDecimal currencyId,
			BigDecimal dCountryId) {
		return repo.findByCurrencyIdAndDestinationCountryId(currencyId, dCountryId);
	}

	public List<VwExRoutingProductStatus> getByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(countryId, currencyId, bankId,
				ServiceModeId, remitModeId);
	}

	public List<VwExRoutingProductStatus> getByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId, BigDecimal deliveryModeId) {
		return repo.findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(countryId,
				currencyId, bankId, ServiceModeId, remitModeId, deliveryModeId);
	}

}
