package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.repository.RoutingHeaderRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class RoutingDao {

	@Autowired
	RoutingHeaderRepository routingHeaderRepository;

	public List<BigDecimal> listAllRoutingBankIds() {
		List<RoutingHeader> rh = (List<RoutingHeader>) routingHeaderRepository.findAll();
		List<BigDecimal> allRoutingBanks = rh.stream().map(i -> i.getRoutingBankId()).distinct()
				.collect(Collectors.toList());
		return allRoutingBanks;
	}

	public List<RoutingHeader> getRoutHeadersByCountryIdAndCurrenyId(BigDecimal countryId, BigDecimal currencyId) {
		return routingHeaderRepository.findByCountryIdAndCurrenyIdAndIsActive(countryId, currencyId, "Y");
	}

}
