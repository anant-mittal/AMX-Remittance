package com.amx.jax.branch.api;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ICustRegService;
import com.amx.jax.JaxConstants;
import com.amx.jax.branch.service.CustRegService;
import com.amx.jax.logger.LoggerService;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustRegController implements ICustRegService {

	private static final Logger LOGGER = LoggerService.getLogger(CustRegController.class);

	@Autowired
	CustRegService custRegService;

	/**
	 * @task Sync DB perms
	 */
	@ApiOperation("Sync Permissions")
	@RequestMapping(value = JaxConstants.CustRegApiEndPoints.GET_ID_FIELDS, method = RequestMethod.POST)
	public void getIdDetailsFields() {

	}

}
