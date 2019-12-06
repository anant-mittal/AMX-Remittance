
package com.amx.jax.ui.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.BoolRespModel;
import com.amx.jax.client.insurance.IGigInsuranceService;
import com.amx.jax.model.request.insurance.SaveInsuranceDetailRequest;
import com.amx.jax.model.response.insurance.GigInsuranceDetail;
import com.amx.jax.swagger.IStatusCodeListPlugin.ApiStatusService;
import com.amx.jax.ui.response.ResponseWrapper;
import com.amx.jax.ui.service.UserService;

import io.swagger.annotations.Api;

/**
 * The Class InsuranceController.
 */
@RestController
@Api(value = "Insurance Alerts Apis")
@ApiStatusService(IGigInsuranceService.class)
public class InsuranceController {

	@Autowired
	private UserService userService;

	/** The jax service. */
	@Autowired
	private IGigInsuranceService gigInsuranceService;

	@RequestMapping(value = "/api/insurance/get", method = { RequestMethod.GET })
	public ResponseWrapper<GigInsuranceDetail> fetchInsuranceDetail() {
		return ResponseWrapper.build(gigInsuranceService.fetchInsuranceDetail());
	}

	@RequestMapping(value = "/api/insurance/save", method = { RequestMethod.POST })
	public ResponseWrapper<BoolRespModel> saveInsuranceDetail(@RequestBody SaveInsuranceDetailRequest request) {
		try {
			return ResponseWrapper.build(gigInsuranceService.saveInsuranceDetail(request));
		} finally {
			userService.updateCustoemrModel();
		}

	}

}
