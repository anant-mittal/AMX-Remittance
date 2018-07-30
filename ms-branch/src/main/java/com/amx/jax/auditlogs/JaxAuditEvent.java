package com.amx.jax.auditlogs;

import java.math.BigDecimal;

import com.amx.amxlib.model.request.CommonRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	//BigDecimal customerId;
	
	OffsiteCustomerRegistrationRequest model;
	
	CommonRequest dataModel;

	Boolean success;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public static enum Type implements EventType {
		SEND_OTP,VALIDATE_OTP,ID_TYPE,COUNTRY_LIST,STATE_LIST;

		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}


	public JaxAuditEvent(EventType type) {
		super(type);
	}

	/*public BigDecimal getCustomerId() {
		return customerId;
	}

	public void setCustomerId(BigDecimal customerId) {
		this.customerId = customerId;
		if (customerId != null) {
			this.actorId = customerId.toString();
		}
	}*/

	public OffsiteCustomerRegistrationRequest getModel() {
		return model;
	}

	public void setModel(OffsiteCustomerRegistrationRequest model) {
		this.model = model;		
			
	}

	public CommonRequest getDataModel() {
		return dataModel;
	}

	public void setDataModel(CommonRequest dataModel) {
		this.dataModel = dataModel;
	}
	
	
	
}
