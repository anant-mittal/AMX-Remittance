package com.amx.jax.pricer.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;

public interface ViewExRoutingMatrixRepository extends CrudRepository<ViewExRoutingMatrix, BigDecimal> {

	@Query("select route from ViewExRoutingMatrix route where route.applicationCountryId=?1 and route.beneCountryId=?2"
			+ " and route.beneBankId=?3 and route.beneBankBranchId=?4 and route.currencyId=?5")
	List<ViewExRoutingMatrix> getRoutingMatrixList(Long applicationCountryId, Long beneCountryId, Long beneBankId,
			Long beneBankBranchId, Long currencyId);

	@Query("select route from ViewExRoutingMatrix route where route.applicationCountryId=?1 and route.beneCountryId=?2"
			+ " and route.currencyId=?3")
	List<ViewExRoutingMatrix> getRoutingMatrixForCountry(Long applicationCountryId, Long beneCountryId,
			Long currencyId);

}
