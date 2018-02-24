package com.amx.jax.logger.rest;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.model.ApplicationLog;
import com.amx.jax.logger.model.CustomerLog;
import com.amx.jax.logger.service.ApplicationLogService;
import com.amx.jax.logger.service.CustomerLogService;
import com.amx.jax.logger.util.CustomerLogStatistics;
import com.amx.jax.logger.util.LogMessageFactory;

/**
 * JAX-RS Statistics service, prototype for data mining and data analysis
 * operations.
 *
 * Implemented the following services: 1- all log messages for a specific
 * customer. 2- list of most visited products 3- all logs for a specific
 * application. 4- Most visited product for a specific customer. 5- full log
 * search for both Application and customer logs.
 *
 */
@RequestMapping("/statistics")
@RestController
public class StatisticsService {

	Logger logger = Logger.getLogger(StatisticsService.class);

	@Autowired
	private CustomerLogService customerLogService;

	@Autowired
	private ApplicationLogService applicationLogService;

	/**
	 * Retrieves products visited by this customer ordered by most visited one.
	 *
	 * @param customerId
	 *            The customer ID.
	 * @return List of products and the corresponding number of visits
	 */
	@RequestMapping("mostVisitedProducts")
	public List<CustomerLogStatistics> getMostVisitedProducts(@RequestParam("customerId") String customerId) {
		logger.debug("getMostVisitedProducts(" + customerId + ")");
		return customerLogService.mostVisitedProducts(customerId);
	}

	/**
	 * Retrieves most visited product.
	 *
	 * @param customerId
	 *            The customer ID.
	 * @return Most visited product.
	 */
	@RequestMapping("recommendedProduct")
	public CustomerLogStatistics getMostVisitedProduct(@RequestParam("customerId") String customerId) {
		logger.debug("getMostVisitedProduct(" + customerId + ")");
		return customerLogService.mostVisitedProduct(customerId);
	}

	/**
	 * All log messages for a specific customer.
	 *
	 * @param customerId
	 *            The customer ID.
	 * @return List of CustomerLog for this customer.
	 */
	@RequestMapping("allCustomerLogs")
	public List<CustomerLog> getCustomerLogs(@RequestParam("customerId") String customerId) {
		logger.debug("getCustomerLogs(" + customerId + ")");
		return customerLogService.findByCustomerId(customerId);
	}

	/**
	 * All log messages for a specific application.
	 *
	 */
	@RequestMapping("allAppLogs")
	public List<ApplicationLog> getAppLogs(@RequestParam("applicationName") String appName) {
		logger.debug("getAppLogs(" + appName + ")");
		return applicationLogService.findAll(appName);
	}

	/**
	 * Search all application logs matching all passed strings in query parameters:
	 * /searchAppLogs?param1=XXX&param2=YYY....
	 *
	 * @return list of matched records sorted by relevance
	 */
	@RequestMapping("searchAppLogs")
	public List<ApplicationLog> searchAppLogs(HttpServletRequest request) {
		logger.debug("searchAppLogs()");
		String[] texts = LogMessageFactory.getAllValues(request.getParameterMap());
		return applicationLogService.searchForText(texts);
	}

	/**
	 * Search all customer logs matching all passed strings in query parameters:
	 * /searchCustomerLogs?param1=XXX&param2=YYY....
	 *
	 * @return list of matched records sorted by relevance
	 */
	@RequestMapping("searchCustomerLogs")
	public List<CustomerLog> searchCustomerLogs(HttpServletRequest request) {
		logger.debug("searchCustomerLogs()");
		String[] texts = LogMessageFactory.getAllValues(request.getParameterMap());
		return customerLogService.searchForText(texts);
	}

}
