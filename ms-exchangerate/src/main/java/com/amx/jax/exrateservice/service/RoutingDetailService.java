package com.amx.jax.exrateservice.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.CountryMaster;
import com.amx.jax.dbmodel.CurrencyMasterMdlv1;
import com.amx.jax.repository.routing.RoutingDetailRepository;

@Service
public class RoutingDetailService {

	@Autowired
	RoutingDetailRepository routingDetailRepository;

	/**
	 * @param currencyId
	 *            - currency id
	 * @return list of routing banks for cash channel
	 */
	public List<BigDecimal> getCashRoutingBanks(BigDecimal currencyId, BigDecimal beneCountryId) {
		return routingDetailRepository.getCashRoutingBanks(new CurrencyMasterMdlv1(currencyId),
				new CountryMaster(beneCountryId));
	}
}
