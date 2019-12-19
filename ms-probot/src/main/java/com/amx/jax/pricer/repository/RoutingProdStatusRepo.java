package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExRoutingProductStatus;

public interface RoutingProdStatusRepo extends CrudRepository<VwExRoutingProductStatus, String> {

	List<VwExRoutingProductStatus> findByCurrencyId(BigDecimal currencyId);

	List<VwExRoutingProductStatus> findByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId);

	List<VwExRoutingProductStatus> findByCurrencyIdAndDestinationCountryId(BigDecimal currencyId,
			BigDecimal dCountryId);

	List<VwExRoutingProductStatus> findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(BigDecimal countryId,
			BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId, BigDecimal remitModeId);

	List<VwExRoutingProductStatus> findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId, BigDecimal deliveryModeId);

}
