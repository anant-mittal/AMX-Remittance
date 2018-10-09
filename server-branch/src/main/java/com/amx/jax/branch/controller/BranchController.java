package com.amx.jax.branch.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Controller
@Api(value = "App Pages")
public class BranchController {

	private Logger logger = Logger.getLogger(BranchController.class);
	
	@ApiOperation(value = "Branch Pages")
	@RequestMapping(value = { "/register/**", "/app/**", "/login/**", "/" }, method = { RequestMethod.GET })
	public String defaultPage(Model model) {
		return "index";
	}


}
