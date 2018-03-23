package com.amx.jax.logger.rest;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amx.jax.logger.AuditLoggerUrls;
import com.amx.jax.logger.events.SessionEvent;
import com.amx.jax.logger.log.MongoAppenderInitializer;
import com.amx.jax.logger.model.ApplicationLog;
import com.amx.jax.logger.model.CustomerLog;
import com.amx.jax.logger.model.SessionLog;
import com.amx.jax.logger.service.SessionLogService;
import com.amx.jax.logger.util.LogMessageFactory;

/**
 * JAX-RS Logging service, endpoint for client apps to invoke the logging
 * service.
 *
 */
@RestController
public class LoggerService {

	/**
	 * MongoDB logger, logger for remote apps.
	 */
	Logger loggerService = Logger.getLogger(MongoAppenderInitializer.LOGGER_SERVICE_NAME);
	/**
	 * stdout logger, utility logger for the service logs.
	 */
	Logger logger = Logger.getLogger(LoggerService.class);

	@Autowired
	SessionLogService sessionLogService;

	/**
	 * Log an application log message, message parameters are passed as request
	 * query parameters.
	 */
	@RequestMapping(AuditLoggerUrls.APP_LOG)
	public void applicationLog(@RequestParam("level") String level, HttpServletRequest request) {
		logger.info(request.getParameterMap());

		ApplicationLog applicationLog = LogMessageFactory.createApplicationLogMessage(request.getParameterMap());
		logger.info("Logging application log message [" + applicationLog + "]");
		loggerService.log(Level.toLevel(level), applicationLog);
	}

	/**
	 * Log a customer related log message, message parameters are passed as request
	 * query parameters.
	 */
	@RequestMapping(AuditLoggerUrls.CUSTOMER_LOG)
	public String customerLog(@RequestParam("level") String level, HttpServletRequest request) {
		CustomerLog customerLog = LogMessageFactory.createCustomerLogMessage(request.getParameterMap());
		logger.info("Logging customer log message [" + customerLog + "]");
		loggerService.log(Level.toLevel(level), customerLog);
		return "ok";
	}

	@RequestMapping(AuditLoggerUrls.SESSION_LOG)
	public SessionLog sessionLog(@RequestParam("level") String level, @RequestBody SessionEvent sessionEvent) {
		SessionLog sessionLog = new SessionLog(sessionEvent);
		logger.info("Logging customer log message [" + sessionEvent + "]");
		sessionLogService.saveSessionLog(sessionLog);
		return sessionLog;
	}

}
