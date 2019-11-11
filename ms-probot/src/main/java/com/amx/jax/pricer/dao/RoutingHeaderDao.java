package com.amx.jax.pricer.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.repository.RoutingHeaderRepo;

@Component
public class RoutingHeaderDao {

	@Autowired
	RoutingHeaderRepo routingHeaderRepo;

	public List<BigDecimal> listAllRoutingBankIds() {
		List<RoutingHeader> rh = (List<RoutingHeader>) routingHeaderRepo.findAll();
		List<BigDecimal> allRoutingBanks = rh.stream().map(i -> i.getRoutingBankId()).distinct()
				.collect(Collectors.toList());
		return allRoutingBanks;
	}

	public List<RoutingHeader> getRoutHeadersByCountryIdAndCurrenyId(BigDecimal countryId, BigDecimal currencyId) {
		return routingHeaderRepo.findByCountryIdAndCurrenyIdAndIsActive(countryId, currencyId, "Y");
	}

}
