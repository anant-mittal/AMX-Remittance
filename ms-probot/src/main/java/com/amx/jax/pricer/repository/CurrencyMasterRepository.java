package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.CurrencyMasterModel;

@Transactional
public interface CurrencyMasterRepository extends CrudRepository<CurrencyMasterModel, BigDecimal> {

	CurrencyMasterModel findByCurrencyCode(String currencyCode);

	@Query("select c from CurrencyMasterModel c where currGroupId=?1 and isactive='Y'")
	List<CurrencyMasterModel> getCurrencyByGroupId(BigDecimal currGroupId);

}
