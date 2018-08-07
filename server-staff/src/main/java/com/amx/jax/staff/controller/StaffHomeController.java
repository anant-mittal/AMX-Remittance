/**  AlMulla Exchange
  *  
  */
package com.amx.jax.staff.controller;

import static com.amx.jax.staff.StaffConstant.ADMIN_API_ENDPOINT;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.staff.service.AdminService;

import io.swagger.annotations.Api;

@RestController
@RequestMapping(ADMIN_API_ENDPOINT)
@Api(value = "Staff App APIs")
@SuppressWarnings("rawtypes")
public class StaffHomeController {

	private Logger logger = Logger.getLogger(StaffHomeController.class);

	@Autowired
	private AdminService adminService;

}
