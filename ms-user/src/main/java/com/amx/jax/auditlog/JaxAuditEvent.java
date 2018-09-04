package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	BigDecimal customerId;

	Boolean success;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public static enum Type implements EventType {
	    
		  //for customer service
		    CUSTOMER_LOCK,
		    CUSTOMER_UNLOCK_SUCCESS,
		    CUSTOMER_UNLOCK_USER_NOT_REGISTERED,
		    CUSTOMER_LOGIN_SUCCESS,
		    CUSTOMER_LOGOUT,
		    CUSTOMER_ACTIVATION,
		    CUSTOMER_DEACTIVATE_SUCCESS,
		    CUSTOMER_REGISTORED,
		    
		    SEC_QUE_GENERATE_SUCCESS,
		    SEC_QUE_GENERATE_EXCEPTION,
		    SEC_QUE_VALIDATE_SUCCESS,
		    SEC_QUE_VALIDATE_INCORRECT_ANS,
		    SEC_QUE_VALIDATE_USER_LOGIN_ATTEMPT_EXCEEDED,
		    
		    CUSTOMER_PASSWORD_UPDATE_SUCCESS,
		    CUSTOMER_PASSWORD_UPDATE_CUSTOMER_ID_NULL,
		    CUSTOMER_PASSWORD_UPDATE_INVALID_OTP,
    
		    MY_PROFILE_EMAIL_UPDATE,
		    MY_PROFILE_PASSWORD_UPDATE,
		    MY_PROFILE_LOG_IN_ID_UPDATE,
		    MY_PROFILE_IMAGE_URL_UPDATE,
		    MY_PROFILE_CAPTION_UPDATE,
		    MY_PROFILE_SEC_QUE_UPDATE;

		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}


	public JaxAuditEvent(EventType type) {
		super(type);
	}

	public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
		if (customerId != null) {
			this.actorId = customerId.toString();
		}
	}
}
