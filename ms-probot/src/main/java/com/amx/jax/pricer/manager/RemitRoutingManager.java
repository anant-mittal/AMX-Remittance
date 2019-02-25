package com.amx.jax.pricer.manager;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.amx.jax.pricer.dao.ViewExRoutingMatrixDao;
import com.amx.jax.pricer.dbmodel.ViewExRoutingMatrix;
import com.amx.jax.pricer.dto.DprRequestDto;
import com.amx.jax.pricer.util.ExchangeRateRequestDataCache;
import com.amx.utils.JsonUtil;

@Component
public class RemitRoutingManager {

	@Autowired
	ViewExRoutingMatrixDao viewExRoutingMatrixDao;

	@Resource
	ExchangeRateRequestDataCache exchangeRateRequestDataCache;

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

		return routingMatrix;

	}

}
