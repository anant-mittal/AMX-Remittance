package com.amx.jax.pricer.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.repository.ViewExRoutingMatrixRepository;

@Component
public class ViewExRoutingMatrixDao {

	@Autowired
	ViewExRoutingMatrixRepository routingMatrixRepo;

	List<ViewExRoutingMatrix> getRoutingMatrixList(Long applicationCountryId, Long beneCountryId, Long beneBankId,
			Long beneBankBranchId, Long currencyId) {
		return routingMatrixRepo.getRoutingMatrixList(applicationCountryId, beneCountryId, beneBankId, beneBankBranchId,
				currencyId);
	}

	List<ViewExRoutingMatrix> getRoutingMatrixForCountry(Long applicationCountryId, Long beneCountryId,
			Long currencyId) {
		return routingMatrixRepo.getRoutingMatrixForCountry(applicationCountryId, beneCountryId, currencyId);
	}

}
