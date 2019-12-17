package com.amx.jax.branch.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;
import com.amx.jax.client.snap.SnapModels.SnapQueryParams;
import com.amx.jax.client.snap.SnapServiceClient;
import com.amx.jax.sso.SSOUser;
import org.slf4j.Logger;
import com.amx.jax.logger.LoggerService;
import io.swagger.annotations.Api;

@RestController
@Api(value = "Snap Api")
public class BDashboardAnalyticsController {

	@Autowired
	private SnapServiceClient snapServiceClient;

	@Autowired
	private SSOUser ssoUser;

	private static final Logger LOGGER = LoggerService.getLogger(BDashboardAnalyticsController.class);

	@RequestMapping(value = "/api/reports/RPT", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@RequestBody Map<String, Object> params) throws IOException {

		Map<String, Map<String, String>> permissionMap = ssoUser.getUserDetails().getUserRole().getPermissionMap();
		SnapQueryParams snapQueryParams = new SnapQueryParams();


		for (String outerMapKey : permissionMap.keySet()) {
			if (outerMapKey == "REPORTS.RPT_REPORT") {
				Map<String, String> innerMap = permissionMap.get(outerMapKey);
				innerMap.forEach((key, value) -> {
					if (key.contains("VIEW") && value.contains("AREA")) {
						BigDecimal areacode = ssoUser.getUserDetails().getAreaCode();
						snapQueryParams.addFilter("branch.areaId", areacode.toString());
					}
					if (key.contains("VIEW") && value.contains("BRANCH")) {
						BigDecimal branchId = ssoUser.getUserDetails().getBranchId();
						snapQueryParams.addFilter("branch.id", branchId.toString());

					}
					if (key.contains("VIEW") && value.contains("COUNTRY")) {
						BigDecimal countryId = ssoUser.getUserDetails().getCountryId();
						snapQueryParams.addFilter("branch.countryId", countryId.toString());

					}

				});

			}
		}
		snapQueryParams.addValue("PrevMonth", "PrevMonth");
		snapQueryParams.addValue("ThisMonth", "ThisMonth");
		snapQueryParams.addValue("lte", "now");
		snapQueryParams.addValue("gte", "now-1m");
		snapQueryParams.addValue("level", "100");
		snapQueryParams.addValue("minCount", "0");

		return snapServiceClient.snapView(SnapQueryTemplate.RPTPG2, new SnapQueryParams(params));

	}

}
