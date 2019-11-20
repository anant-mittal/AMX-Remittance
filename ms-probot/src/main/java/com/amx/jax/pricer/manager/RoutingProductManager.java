package com.amx.jax.pricer.manager;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.RoutingDaoAlt;
import com.amx.jax.pricer.dao.ViewExGLCBALDao;
import com.amx.jax.pricer.dao.VwExGLCBalProvDao;
import com.amx.jax.pricer.dao.VwGlcbalProvProductDao;
import com.amx.jax.pricer.dbmodel.RoutingHeader;
import com.amx.jax.pricer.dto.RoutingProductStatusDetails;

@Component
public class RoutingProductManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RoutingProductManager.class);

	@Autowired
	private RoutingDaoAlt routingDao;

	@Autowired
	private ViewExGLCBALDao glcbalDao;

	@Autowired
	private VwExGLCBalProvDao vwGLCBalProvDao;

	@Autowired
	private VwGlcbalProvProductDao vwGlcbalProvProductDao;

	public RoutingProductStatusDetails getRoutingProductStatus(BigDecimal countryId, BigDecimal currencyId) {

		RoutingProductStatusDetails resp = new RoutingProductStatusDetails();

		List<RoutingHeader> routingHeaders = routingDao.getRoutHeadersByCountryIdAndCurrenyId(countryId, currencyId);

		if (routingHeaders == null || routingHeaders.isEmpty()) {
			// throw Exception
		}

		List<BigDecimal> correspondentIds = routingHeaders.stream().map(h -> h.getRoutingBankId()).distinct()
				.collect(Collectors.toList());

		return null;

	}

}
