package com.amx.jax.branch.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.client.snap.SnapConstants.SnapQueryTemplate;
import com.amx.jax.client.snap.SnapModels.SnapModelWrapper;

import io.swagger.annotations.Api;

import com.amx.jax.amxlib.model.RoutingBankMasterParam.RoutingBankMasterServiceProviderParam;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.client.serviceprovider.RoutingBankMasterDTO;
import com.amx.jax.client.snap.SnapServiceClient;

@RestController
@Api(value = "Snap Api")
public class SnapBranchController {
	@Autowired
	private SnapServiceClient snapServiceClient;

	@RequestMapping(value = "/pub/snap/view/{snapView}", method = RequestMethod.POST)
	public SnapModelWrapper snapView(@PathVariable(value = "snapView") SnapQueryTemplate snapView,
			@RequestBody Map<String, Object> params,
			@RequestParam(defaultValue = "now-1m", required = false) String gte,
			@RequestParam(defaultValue = "now", required = false) String lte,
			@RequestParam(defaultValue = "100", required = false) Integer level,
			@RequestParam(defaultValue = "0", required = false) Integer minCount) throws IOException {
		return snapServiceClient.snapView(snapView, params, gte, lte, level, minCount);
	}

}
