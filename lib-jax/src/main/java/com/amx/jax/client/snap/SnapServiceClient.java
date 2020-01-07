package com.amx.jax.client.snap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.AppConfig;
import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.client.snap.SnapModels.SnapQueryParams;
import com.amx.jax.dict.Currency;
import com.amx.jax.logger.LoggerService;
import com.amx.jax.rest.RestService;

@Component
public class SnapServiceClient implements ISnapService {
	private static final org.slf4j.Logger logger = LoggerService.getLogger(SnapServiceClient.class);

	@Autowired
	RestService restService;

	@Autowired
	AppConfig appConfig;

	@Override
	public SnapModelWrapper getCustomerStats() {
		return new SnapModelWrapper(
				restService.ajax(appConfig.getRadarURL() + Path.SNAP_API_CUSTOMER_JOINED).get().asMap());
	}

	@Override
	public SnapModelWrapper getTranxStats() {
		return new SnapModelWrapper(restService.ajax(appConfig.getRadarURL() + Path.SNAP_API_TRANX_DONE).get().asMap());
	}

	@Override
	public SnapModelWrapper getXRateStats(StatsSpan graph, Currency forCur, Currency domCur) {
		return new SnapModelWrapper(restService.ajax(appConfig.getRadarURL() + Path.SNAP_API_XRATE_SELL_TRANSFER)
				.queryParam("graph", graph).queryParam("forCur", forCur).queryParam("domCur", domCur).get().asMap());
	}

	public SnapModelWrapper snapView(SnapQueryTemplate snapView, SnapQueryParams params) {
		
		logger.info("params gte",params.get("gte"));
		return new SnapModelWrapper(
				restService.ajax(appConfig.getRadarURL() + Path.SNAP_API_VIEW).pathParam(Params.SNAP_VIEW, snapView)
						.queryParam("gte", params.get("gte")).queryParam("lte", params.get("lte")).queryParam("level", params.getLevel())
						.queryParam("minCount", params.getMinCount()).post(params).asMap());
	}

}