package com.amx.jax.pricer.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.pricer.dao.ViewExRoutingMatrixDao;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.exception.PricerServiceError;
import com.amx.jax.pricer.exception.PricerServiceException;
import com.amx.jax.pricer.util.ExchangeRequestTransientDataCache;
import com.amx.jax.pricer.util.RoutingTransientDataComputationObject;
import com.amx.utils.JsonUtil;

@Component
public class RemitRoutingManager {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(RemitRoutingManager.class);

	@Autowired
	ViewExRoutingMatrixDao viewExRoutingMatrixDao;

	@Resource
	ExchangeRequestTransientDataCache exchangeRequestTransientDataCache;

	public List<ViewExRoutingMatrix> getRoutingMatrixForRemittance(DprRequestDto dprRequestDto) {

		// StopWatch watch = new StopWatch();
		// watch.start();

		List<ViewExRoutingMatrix> routingMatrix = viewExRoutingMatrixDao.getRoutingMatrix(
				dprRequestDto.getLocalCountryId(), dprRequestDto.getForeignCountryId(),
				dprRequestDto.getBeneficiaryBankId(), dprRequestDto.getBeneficiaryBranchId(),
				dprRequestDto.getForeignCurrencyId(), dprRequestDto.getServiceGroup().getGroupCode());

		// watch.stop();
		// System.out.println(" Time taken ==> " + (watch.getLastTaskTimeMillis() ));

		System.out.println(" Routing matrix ==>  " + JsonUtil.toJson(routingMatrix));

		if (null == routingMatrix || routingMatrix.isEmpty()) {

			LOGGER.info("Routing Matrix is Data is Empty or Null for the Pricing/Routing Request");

			throw new PricerServiceException(PricerServiceError.INVALID_ROUTING_BANK_IDS,
					"Invalid Routing Bank Ids : None Found matching with the Requested Ids: "
							+ dprRequestDto.getRoutingBankIds());
		}

		List<RoutingTransientDataComputationObject> routingComputationObjects = new ArrayList<RoutingTransientDataComputationObject>();

		for (ViewExRoutingMatrix viewExRoutingMatrix : routingMatrix) {
			RoutingTransientDataComputationObject obj = new RoutingTransientDataComputationObject();
			obj.setViewExRoutingMatrix(viewExRoutingMatrix);
			routingComputationObjects.add(obj);
		}

		exchangeRequestTransientDataCache.setRoutingMatrix(routingComputationObjects);

		return routingMatrix;

	}

}
