package com.amx.jax.dal;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.routing.RoutingHeader;
import com.amx.jax.repository.routing.RoutingHeaderRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)

public class RoutingDao {

	private Logger logger = Logger.getLogger(BizcomponentDao.class);

	@Autowired
	RoutingHeaderRepository routingHeaderRepository;

	public List<BigDecimal> listAllRoutingBankIds() {
		List<RoutingHeader> rh = (List<RoutingHeader>) routingHeaderRepository.findAll();
		List<BigDecimal> allRoutingBanks = rh.stream().map(i -> i.getExRoutingBankId()).distinct()
				.collect(Collectors.toList());
		return allRoutingBanks;
	}

}
