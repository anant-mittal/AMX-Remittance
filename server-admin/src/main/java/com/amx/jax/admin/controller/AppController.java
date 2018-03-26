/**  AlMulla Exchange
  *  
  */
package com.amx.jax.admin.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
	@RequestMapping(value = "/amxadmin", method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}

}
