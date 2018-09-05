package com.amx.jax.staff.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.ICustRegService;

import io.swagger.annotations.Api;

@RestController
@Api(value = "Staff App APIs")
public class StaffCustRegController {

	private Logger logger = Logger.getLogger(StaffCustRegController.class); 

	@Autowired
	private ICustRegService custRegService;

}
