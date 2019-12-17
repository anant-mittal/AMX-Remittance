package com.amx.jax.offsite.controller;

import java.util.Map;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.client.snap.SnapModels.SnapQueryParams;
import com.amx.jax.client.snap.SnapServiceClient;
import com.amx.jax.logger.LoggerService;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

/**
 * The Class MetaController.
 */
@RestController
@Api(value = "DashBoard  APIs")
public class DashboardAnalyticsController {

	private static final Logger LOGGER = LoggerService.getLogger(DashboardAnalyticsController.class);

	@Autowired
	private SnapServiceClient snapServiceClient;

	@RequestMapping(value = "/pub/reports/trnx_status", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@RequestBody Map<String, Object> params) {
		return snapServiceClient.snapView(SnapQueryTemplate.TRNX_LIFECYCLE, new SnapQueryParams(params));
	}


	
}
