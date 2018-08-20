package com.amx.jax.branch.api;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ICustRegService;
import com.amx.jax.api.ARespModel;
import com.amx.jax.api.AmxApiResponse;
import com.amx.jax.branch.service.CustRegService;

import io.swagger.annotations.ApiOperation;

@RestController
public class CustRegController implements ICustRegService {

	@Autowired
	CustRegService custRegService;

	@Override
	@RequestMapping(value = CustRegApiEndPoints.GET_MODES, method = RequestMethod.GET)
	public AmxApiResponse<BigDecimal, Object> getModes() {
		return custRegService.getModes();
	}

	@Override
	@ApiOperation("Get ID Fields List for Identity")
	@RequestMapping(value = CustRegApiEndPoints.GET_ID_FIELDS, method = RequestMethod.POST)
	public AmxApiResponse<ARespModel, Object> getIdDetailsFields(@RequestBody RegModeModel regModeModel) {
		return custRegService.getIdDetailsFields(regModeModel);
	}

}
