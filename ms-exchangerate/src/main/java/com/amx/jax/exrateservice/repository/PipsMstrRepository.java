package com.amx.jax.exrateservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.amx.jax.dbmodel.PipsMstr;

@Transactional
public interface PipsMstrRepository extends CrudRepository<PipsMstr,BigDecimal> {

	@Query("select rate from PipsMstr rate where rate.curCod=?1 and rate.corsBnk=?2 ")
	List<PipsMstr> getExchangeRatesPlaceorder(BigDecimal currency, String bankId);
}
