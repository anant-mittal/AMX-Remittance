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

@Controller
@Api(value = "App Pages")
public class AppController {

	private Logger log = Logger.getLogger(AppController.class);

	@ApiOperation(value = "Index page")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}

	@ApiOperation(value = "Admin page")
	@RequestMapping(value = "/app/admin", method = RequestMethod.GET)
	public String indexAdmin(Model model) {
		return "admin-index";
	}

	@RequestMapping(value = "/app/push", method = RequestMethod.GET)
	public String pushIndex(Model model) {
		return "push-index";
	}

	@RequestMapping(value = "/app/service2", method = RequestMethod.GET)
	public String pushService(Model model) {
		return "service2-index";
	}

}
