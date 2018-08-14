package com.amx.jax.auditlogs;

import com.amx.amxlib.model.CustomerPersonalDetail;
import com.amx.amxlib.model.request.CommonRequest;
import com.amx.amxlib.model.request.EmploymentDetailsRequest;
import com.amx.amxlib.model.request.GetJaxFieldRequest;
import com.amx.amxlib.model.request.OffsiteCustomerRegistrationRequest;
import com.amx.jax.logger.AuditEvent;

public class JaxAuditEvent extends AuditEvent {

	private static final long serialVersionUID = 7451732272992078549L;
	//BigDecimal customerId;
	
	OffsiteCustomerRegistrationRequest model;
	
	CommonRequest dataModel;
	
	GetJaxFieldRequest detJaxFieldModel;

	Boolean success;
	
	CustomerPersonalDetail customerPersonalDetails;
	
	EmploymentDetailsRequest employeeDetailsRequest;

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public static enum Type implements EventType {
		SEND_OTP,VALIDATE_OTP,ID_TYPE,COUNTRY_LIST,STATE_LIST,FIELD_LIST,MOBILE_EMAIL_OTP,DISTRICT_LIST,CITY_LIST,ARTICLE_LIST,DESIGNATION_LIST,INCOME_RANGE;

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

	public GetJaxFieldRequest getDetJaxFieldModel() {
		return detJaxFieldModel;
	}

	public void setDetJaxFieldModel(GetJaxFieldRequest detJaxFieldModel) {
		this.detJaxFieldModel = detJaxFieldModel;
	}

	public CustomerPersonalDetail getCustomerPersonalDetails() {
		return customerPersonalDetails;
	}

	public void setCustomerPersonalDetails(CustomerPersonalDetail customerPersonalDetails) {
		this.customerPersonalDetails = customerPersonalDetails;
	}

	public EmploymentDetailsRequest getEmployeeDetailsRequest() {
		return employeeDetailsRequest;
	}

	public void setEmployeeDetailsRequest(EmploymentDetailsRequest employeeDetailsRequest) {
		this.employeeDetailsRequest = employeeDetailsRequest;
	}
	
	
	
}
