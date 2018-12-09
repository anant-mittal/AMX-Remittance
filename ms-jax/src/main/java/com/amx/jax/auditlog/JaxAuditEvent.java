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
		
		APPLICATION_CREATED,
		PLACE_ORDER_TRIGGER,
		
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
	    BENE_PO_NO_BENE_RECORD,

		/** FOR FC Sale Order in Online added by Rabil **/
	    FC_SALE_PUR_TRNX,
	    FC_SALE_PUR_TRNX_NOT_EXIST,
		FC_SALE_CURR_LIST_SUCESS,
		FC_SALE_CURR_LIST_NOT_SET,
		FC_SALE_UPDATE_ORDER_STATUS;
		/** End Here**/

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
