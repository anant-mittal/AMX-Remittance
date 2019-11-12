package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CurrencyMasterMdlv1;

@Transactional
public interface CurrencyRepository extends CrudRepository<CurrencyMasterMdlv1, BigDecimal> {

	List<CurrencyMasterMdlv1> findByquoteName(String quoteName);

	List<CurrencyMasterMdlv1> findAll();
	
	@Query(value = "SELECT * FROM EX_CURRENCY_MASTER WHERE CURRENCY_ID in ?1 ", nativeQuery = true)
	List<CurrencyMasterMdlv1> fetchCurrencyMaster(List<String>  currencyId);

}
