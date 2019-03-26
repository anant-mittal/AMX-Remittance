package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CurrencyMasterModel;

@Transactional
public interface CurrencyRepository extends CrudRepository<CurrencyMasterModel, BigDecimal> {

	List<CurrencyMasterModel> findByquoteName(String quoteName);

	List<CurrencyMasterModel> findAll();
	
	@Query(value = "SELECT * FROM EX_CURRENCY_MASTER WHERE CURRENCY_ID in ?1 ", nativeQuery = true)
	List<CurrencyMasterModel> fetchCurrencyMaster(List<String>  currencyId);

}
