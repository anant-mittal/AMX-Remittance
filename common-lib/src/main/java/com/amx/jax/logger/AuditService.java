package com.amx.jax.logger;

import org.slf4j.Logger;

public interface AuditService {

	public static final String AUDIT_EVENT_TOPIC = "AUDIT_EVENT_TOPIC";

	/**
	 * Log Audit Events
	 * 
	 * @param event
	 * @return
	 */
	public AuditLoggerResponse log(AuditEvent event);

	/**
	 * Audit from request tracking purpose
	 * 
	 * @param event
	 * @return
	 */
	public AuditLoggerResponse track(AuditEvent event);

	/**
	 * Audit from performance perspective
	 * 
	 * @param event
	 * @return
	 */
	public AuditLoggerResponse gauge(AuditEvent event);

	/**
	 * 
	 * Log exception
	 * 
	 * @param event
	 * @param e
	 * @return
	 */
	public AuditLoggerResponse excep(AuditEvent event, Exception e);

	/**
	 * Log exception plus print stack trace
	 * 
	 * @param event
	 * @param logger
	 * @param e
	 * @return
	 */
	public AuditLoggerResponse excep(AuditEvent event, Logger logger, Exception e);

}
