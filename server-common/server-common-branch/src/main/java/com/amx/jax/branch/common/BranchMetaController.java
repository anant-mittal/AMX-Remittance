package com.amx.jax.branch.common;

import java.util.concurrent.ExecutionException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.http.ApiRequest;
import com.amx.jax.postman.PostManException;
import com.amx.jax.rbaac.dto.response.EmployeeDetailsDTO;
import com.amx.jax.sso.SSOUser;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Branch User/Employee Meta APIs")
public class BranchMetaController {

	private Logger logger = Logger.getLogger(BranchMetaController.class);

	@Autowired
	private SSOUser ssoUser;

	@Deprecated
	@ApiRequest(deprecated = "use /pub/meta/user")
	@RequestMapping(value = { "/pub/meta" }, method = RequestMethod.GET)
	public AmxApiResponse<EmployeeDetailsDTO, Object> metaCommon()
			throws PostManException, InterruptedException, ExecutionException {
		return AmxApiResponse.build(ssoUser.getUserDetails());
	}

	@RequestMapping(value = { "/pub/meta/user" }, method = RequestMethod.GET)
	public AmxApiResponse<EmployeeDetailsDTO, Object> metaUser()
			throws PostManException, InterruptedException, ExecutionException {
		return AmxApiResponse.build(ssoUser.getUserDetails());
	}

}
