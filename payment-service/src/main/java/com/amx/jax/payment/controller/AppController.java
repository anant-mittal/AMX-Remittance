/**  AlMulla Exchange
  *  
  */
package com.amx.jax.payment.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Viki Sangani
 * 13-Dec-2017
 * Appcontroller.java
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
	   
	   @RequestMapping(value = "/pg_success", method = RequestMethod.GET)
	   public String success(Model model) {
	      
		   return "thymeleaf/pg_success";
	      
	   }
	   
	   @RequestMapping("/jsp")
	    String jspPage(Model model,@RequestParam String name) {
	        model.addAttribute("name", name);
	        return "jsp/sample";
	    }

	   @RequestMapping("/thymeleaf")
	    String thymeleafPage(Model model,@RequestParam String name) {
	        model.addAttribute("name", name);
	        return "thymeleaf/sample";
	    }
	    

}
