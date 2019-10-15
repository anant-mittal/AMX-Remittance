package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.repository.RoutingHeaderRepositoryAlt;

@Component
// @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode =
// ScopedProxyMode.TARGET_CLASS)
public class RoutingDaoAlt {

	@Autowired
	RoutingHeaderRepositoryAlt routingHeaderRepositoryAlt;

	public List<BigDecimal> listAllRoutingBankIds() {
		List<RoutingHeader> rh = (List<RoutingHeader>) routingHeaderRepositoryAlt.findAll();
		List<BigDecimal> allRoutingBanks = rh.stream().map(i -> i.getRoutingBankId()).distinct()
				.collect(Collectors.toList());
		return allRoutingBanks;
	}

	public List<RoutingHeader> getRoutHeadersByCountryIdAndCurrenyId(BigDecimal countryId, BigDecimal currencyId) {
		return routingHeaderRepositoryAlt.findByCountryIdAndCurrenyIdAndIsActive(countryId, currencyId, "Y");
	}

}
