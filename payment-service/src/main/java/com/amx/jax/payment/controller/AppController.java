/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Viki Sangani 13-Dec-2017 Appcontroller.java
 */
@Controller
@Api(value = "App APIs")
public class AppController {

	private Logger log = Logger.getLogger(AppController.class);

	@ApiOperation(value = "Index page")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "thymeleaf/index";
	}

	@RequestMapping(value = "callback/{page}", method = RequestMethod.GET)
	public String success(Model model, @PathVariable("page") String page) {

		return "thymeleaf/pg_" + page;
	}

}
