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
import com.amx.jax.logger.LoggerService;
import com.amx.jax.sso.SSOUser;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Snap Api")
public class DashboardAnalyticsController {

	@Autowired
	private SnapServiceClient snapServiceClient;

	@Autowired
	private SSOUser ssoUser;

	private static final org.slf4j.Logger logger = LoggerService.getLogger(DashboardAnalyticsController.class);

	@RequestMapping(value = "/api/reports/RPT", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@RequestBody SnapQueryParams params) throws IOException {

		Map<String, Map<String, String>> permissionMap = ssoUser.getUserDetails().getUserRole().getPermissionMap();
		SnapQueryParams snapQueryParams = new SnapQueryParams();
		String userName = ssoUser.getUserDetails().getUserName();
		BigDecimal areacode = ssoUser.getUserDetails().getAreaCode();
		BigDecimal branchId = ssoUser.getUserDetails().getBranchId();
		Map<String, String> testMap = new HashMap<String, String>();

		for (String outerMapKey : permissionMap.keySet()) {
			if (outerMapKey == "REPORTS.RPT_REPORT") {
				Map<String, String> innerMap = permissionMap.get(outerMapKey);
				innerMap.forEach((key, value) -> {
					if (key.contains("VIEW") && value.contains("AREA")) {
						logger.debug("Key = " + key + ", Value = " + value);
						snapQueryParams.addFilter("branch.areaId", areacode.toString());
						testMap.put("branch.areaId", areacode.toString());
					}
					if (key.contains("VIEW") && value.contains("BRANCH")) {
						logger.debug("Key = " + key + ", Value = " + value);
						snapQueryParams.addFilter("branch.id", branchId.toString());
						testMap.put("branch.id", branchId.toString());

					}

				});

			}
			testMap.forEach((key, value) -> {
				logger.debug("Key = " + key + ", Value = " + value);

			});

		}
		return snapServiceClient.snapView(SnapQueryTemplate.RPTPG2, snapQueryParams);

	}

}
