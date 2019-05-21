package com.amx.jax.partner.repository;

import java.io.Serializable;
import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.partner.dbmodel.USDExchangeRateView;

public interface IUsdExchangeRateRepository extends CrudRepository<USDExchangeRateView, Serializable> {
	
	@Query(value = "SELECT TRUNC(USD_LOCAL_RATE,6) as USD_LOCAL_RATE FROM EX_CASH_USD_RATE WHERE CUR_DATE = (SELECT MAX(CUR_DATE) FROM EX_CASH_USD_RATE)", nativeQuery = true)
	public BigDecimal fetchUsdExchangeRate();
	
	@Query(value = "SELECT WU_TOKEN_SEQ.NEXTVAL FROM DUAL", nativeQuery = true)
	public BigDecimal fetchServiceProviderRefernceNum();

}
