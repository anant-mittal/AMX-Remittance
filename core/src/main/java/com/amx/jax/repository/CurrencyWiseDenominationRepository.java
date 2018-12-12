package com.amx.jax.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.CurrencyWiseDenomination;

@Transactional
public interface CurrencyWiseDenominationRepository extends CrudRepository<CurrencyWiseDenomination, String>{
	
	@Query(value = "SELECT * FROM EX_CURRENCY_DENOMINATION WHERE CURRENCY_ID=?1 AND ISACTIVE=?2", nativeQuery = true)
	public List<CurrencyWiseDenomination> fetchCurrencyDenomination(BigDecimal currencyId,String isActive);

}
