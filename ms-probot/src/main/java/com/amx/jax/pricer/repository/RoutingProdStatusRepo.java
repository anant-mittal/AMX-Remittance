package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.VwExRoutingProduct;

public interface RoutingProdStatusRepo extends CrudRepository<VwExRoutingProduct, String> {

	List<VwExRoutingProduct> findByCurrencyId(BigDecimal currencyId);
	
	@Query(value = "SELECT * FROM VW_EX_ROUTING_PRODUCT WHERE CURRENCY_ID=?1 AND ROUTING='ACTIVE' ", nativeQuery = true)
	List<VwExRoutingProduct> findByCurrencyIdAndrouting(BigDecimal currencyId);
	
	List<VwExRoutingProduct> findByCurrencyIdAndCountryId(BigDecimal currencyId, BigDecimal countryId);

	List<VwExRoutingProduct> findByCurrencyIdAndDestinationCountryId(BigDecimal currencyId,
			BigDecimal dCountryId);

	List<VwExRoutingProduct> findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeId(BigDecimal countryId,
			BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId, BigDecimal remitModeId);

	List<VwExRoutingProduct> findByCountryIdAndCurrencyIdAndBankIdAndServiceIdAndRemitModeIdAndDeliveryModeId(
			BigDecimal countryId, BigDecimal currencyId, BigDecimal bankId, BigDecimal ServiceModeId,
			BigDecimal remitModeId, BigDecimal deliveryModeId);

}
