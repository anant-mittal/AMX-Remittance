package com.amx.jax.scheduler.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amx.amxlib.model.response.ApiResponse;
import com.amx.amxlib.model.response.BooleanResponse;
import com.amx.jax.scheduler.service.RateAlertService;

@RestController
@RequestMapping("/task")
public class TaskController {

	@Autowired
	RateAlertService rateAlertService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	@RequestMapping(value = "/rate-alert/restart/", method = RequestMethod.POST)
	public ApiResponse<BooleanResponse> restartRateAlertTask() {
		logger.info("restartRateAlertTask Request:");
		return rateAlertService.restartRateAlertTask();
	}
}
