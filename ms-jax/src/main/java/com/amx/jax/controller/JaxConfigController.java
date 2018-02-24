package com.amx.jax.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.jax.amxlib.config.OtpSettings;
import com.amx.jax.services.JaxConfigService;

@RestController
@RequestMapping("/config")
public class JaxConfigController {

	private Logger logger = LoggerFactory.getLogger(JaxConfigController.class);

	@Autowired
	JaxConfigService jaxConfigService;

	@RequestMapping(method = RequestMethod.POST)
	public ApiResponse createorUpdateOtpSettings(@RequestBody OtpSettings otpSettings) {
		logger.debug("createorUpdateOtpSettings Request:" + otpSettings.toString());
		return jaxConfigService.saveOtpSettings(otpSettings);
	}
}
