package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.repository.ViewExRoutingMatrixRepository;

@Component
public class ViewExRoutingMatrixDao {

	@Autowired
	private ViewExRoutingMatrixRepository viewExRoutingMatrixRepository;

	public List<ViewExRoutingMatrix> getRoutingMatrixForBankService(BigDecimal applicationCountryId,
			BigDecimal beneCountryId, BigDecimal beneBankId, BigDecimal beneBankBranchId, BigDecimal currencyId,
			String serviceGroupCode) {
		return viewExRoutingMatrixRepository.findRoutingMatrix(applicationCountryId, beneCountryId, beneBankId,
				beneBankBranchId, currencyId, serviceGroupCode);
	}

	public List<ViewExRoutingMatrix> getRoutingMatrixForCashService(BigDecimal applicationCountryId,
			BigDecimal beneCountryId, BigDecimal beneBankId, BigDecimal beneBankBranchId, BigDecimal currencyId,
			String serviceGroupCode, BigDecimal routingBankId, BigDecimal bankBranchId) {
		return viewExRoutingMatrixRepository.findRoutingMatrix(applicationCountryId, beneCountryId, beneBankId,
				beneBankBranchId, currencyId, serviceGroupCode, routingBankId, bankBranchId);
	}

}
