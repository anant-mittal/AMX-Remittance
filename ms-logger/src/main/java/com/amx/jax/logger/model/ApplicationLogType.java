package com.amx.jax.logger.model;

/**
 * Log messages types for application logging messages, used for message
 * classification.
 *
 */
public enum ApplicationLogType {
	GENERIC // Undefined
	, SYSTEM_INFO // information
	, SYSTEM_FAILURE // System Failure, Fatal Error.
	, SYSTEM_ERROR // System Error.
}
