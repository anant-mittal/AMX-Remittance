/**  AlMulla Exchange
  *  
  */
package com.amx.jax.offsite.controller;

import org.apache.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;

/**
 * 
 * @author lalittanwar
 *
 */
@RestController
public class OffsiteController {

	private Logger logger = Logger.getLogger(OffsiteController.class);

	@ApiOperation(value = "Index page")
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index(Model model) {
		return "index";
	}

}
