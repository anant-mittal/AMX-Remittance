package com.amx.jax.constants;

import com.amx.jax.auditlog.handlers.AbstractAuditHanlder;
import com.amx.jax.auditlog.handlers.SendOtpAuditHandler;
import com.amx.jax.auditlog.handlers.ValidateOTPAuditHandler;


/**
 * @author Prashant - Represents api flow defination
 */
public enum JaxEvent {

	SEND_OTP(SendOtpAuditHandler.class),VALIDATE_OTP(ValidateOTPAuditHandler.class);
	Class<? extends AbstractAuditHanlder> auditHanlder;	

	private JaxEvent(Class<? extends AbstractAuditHanlder> auditHanlder) {
		this.auditHanlder = auditHanlder;
	}	

	public Class<? extends AbstractAuditHanlder> getAuditHanlder() {
		return auditHanlder;
	}

	public void setAuditHanlder(Class<? extends AbstractAuditHanlder> auditHanlder) {
		this.auditHanlder = auditHanlder;
	}


}
