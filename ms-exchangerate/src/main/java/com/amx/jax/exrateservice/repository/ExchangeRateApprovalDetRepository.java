package com.amx.jax.exrateservice.repository;

import java.math.BigDecimal;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.dbmodel.ExchangeRateApprovalDetModel;

@Transactional
public interface ExchangeRateApprovalDetRepository extends CrudRepository<ExchangeRateApprovalDetModel, BigDecimal> {

	@Query("select rate from ExchangeRateApprovalDetModel rate where rate.currencyId=?1 and rate.countryBranchId=?2 "
			+ "and rate.countryId=?3")
	List<ExchangeRateApprovalDetModel> getExchangeRates(BigDecimal currencyId, BigDecimal countryBranchId,BigDecimal countryId);

}
