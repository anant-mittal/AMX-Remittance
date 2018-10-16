package com.amx.jax.auditlog.handlers;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import com.amx.jax.auditlogs.JaxAuditEvent;
import com.amx.jax.logger.AuditService;
import com.amx.jax.logger.LoggerService;

public abstract class AbstractAuditHanlder implements IAuditHandler {

	@Autowired
	HttpServletResponse response;
	@Autowired
	AuditService auditService;

	private static final Logger logger = LoggerService.getLogger(AbstractAuditHanlder.class);

	boolean getSuccess() {
		if (response.getStatus() != HttpStatus.OK.value()) {
			return false;
		}
		return true;
	}

	public void log() {
		JaxAuditEvent event = createAuditEvent();
		if (event != null) {
			event.setSuccess(getSuccess());
			auditService.log(event);
		} else {
			logger.warn("No audit event defined for audit handler {}", this.getClass().getName());
		}
	}

}
