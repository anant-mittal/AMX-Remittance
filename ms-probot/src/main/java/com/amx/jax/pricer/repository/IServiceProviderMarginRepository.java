package com.amx.jax.pricer.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ServiceProviderRateView;

public interface IServiceProviderMarginRepository extends CrudRepository<ServiceProviderRateView, Serializable> {

	@Query(value = "SELECT * FROM JAX_VW_HOME_SRV_RATE WHERE COUNTRY_ID=?1 AND BANK_ID=?2 AND CURRENCY_ID=?3 AND REMITTANCE_ID=?4 AND DELIVERY_ID=?5 ", nativeQuery = true)
	public ServiceProviderRateView fetchMerginByProduct(BigDecimal countryId,BigDecimal bankId,BigDecimal currencyId,BigDecimal remittanceId,BigDecimal deliveryId);
	
}
