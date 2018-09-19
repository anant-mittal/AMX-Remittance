package com.amx.jax.auditlogs;

import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.jax.logger.AuditEvent;
import com.amx.jax.model.request.CustomerInfoRequest;
import com.amx.jax.model.request.DynamicFieldRequest;
import com.amx.jax.model.request.EmploymentDetailsRequest;
import com.amx.jax.model.request.OffsiteCustomerRegistrationRequest;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;	
	
	OffsiteCustomerRegistrationRequest model;	

	Boolean success;
	
	CustomerPersonalDetail customerPersonalDetails;

	EmploymentDetailsRequest employeeDetailsRequest;
	
	DynamicFieldRequest dynamicFieldRequest;
	
	CustomerInfoRequest customerInfoRequest;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public static enum Type implements EventType {
		SEND_OTP,VALIDATE_OTP,
		ID_TYPE,
		COUNTRY_LIST,
		STATE_LIST,
		FIELD_LIST,
		MOBILE_EMAIL_OTP,
		DISTRICT_LIST,
		CITY_LIST,
		ARTICLE_LIST,
		DESIGNATION_LIST,
		INCOME_RANGE,
		CUST_INFO;

		@Override
		public EventMarker marker() {
			return EventMarker.AUDIT;
		}
	}


	public JaxAuditEvent(EventType type) {
		super(type);
	}
	
	public JaxAuditEvent(Type type, OffsiteCustomerRegistrationRequest offsiteRequest)
	{
		super(type);
		this.model = offsiteRequest;
	}
	
	public JaxAuditEvent(Type type, EmploymentDetailsRequest employeeDetailsRequest)
	{		
		super(type);
		this.employeeDetailsRequest = employeeDetailsRequest;
	}
	
	public JaxAuditEvent(Type type, DynamicFieldRequest dynamicFieldRequest)
	{		
		super(type);
		this.dynamicFieldRequest = dynamicFieldRequest;
	}
	
	public JaxAuditEvent(Type type, CustomerInfoRequest customerInfoRequest)
	{		
		super(type);
		this.customerInfoRequest = customerInfoRequest;
	}
	

	

	
	
	
	
}
