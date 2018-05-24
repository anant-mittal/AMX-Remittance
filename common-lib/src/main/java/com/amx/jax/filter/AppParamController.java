package com.amx.jax.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.AppParam;

@RestController
public class AppParamController {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppParamController.class);

	@RequestMapping(value = "/pub/amx/params", method = RequestMethod.GET)
	public AppParam geoLocation(@RequestParam AppParam param) {
		param.setEnabled(!param.isEnabled());
		LOGGER.info("App Param {} changed to {}", param, param.isEnabled());
		return param;
	}

}
