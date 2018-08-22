package com.amx.jax.auditlog;

import java.math.BigDecimal;

import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	BigDecimal customerId;
	
	public static enum Type implements EventType {
		
		APPLICATION_CREATED,
		
	    //for benficiary service
	    BENE_STATUS_UPDATE_SUCCESS,
	    BENE_STATUS_UPDATE_EXEC,
	    BENE_STATUS_UPDATE_NO_BENE_RECORD,
	    BENE_FAV_UPDATE_SUCCESS,
	    BENE_FAV_UPDATE_NO_BENE_RECORD,
	    BENE_FAV_UPDATE_EXEC,
	    BENE_FAV_LIST_SUCCESS,
	    BENE_FAV_LIST_NOT_EXIST,
	    BENE_PO_SUCCESS,
	    BENE_PO_NO_PO_ID,
	    BENE_PO_NO_BENE_RECORD;

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
	}
}
