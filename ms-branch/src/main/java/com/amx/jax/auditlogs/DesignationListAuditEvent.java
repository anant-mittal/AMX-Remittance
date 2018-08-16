package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.EmploymentDetailsRequest;
import com.amx.jax.auditlogs.JaxAuditEvent.Type;

public class DesignationListAuditEvent extends JaxAuditEvent {

EmploymentDetailsRequest employeeDetailsRequest;

public EmploymentDetailsRequest getEmployeeDetailsRequest() {
	return employeeDetailsRequest;
}

public void setEmployeeDetailsRequest(EmploymentDetailsRequest employeeDetailsRequest) {
	this.employeeDetailsRequest = employeeDetailsRequest;
}
  
public DesignationListAuditEvent(EmploymentDetailsRequest employeeDetailsRequest) {
	super(Type.DESIGNATION_LIST);
	this.employeeDetailsRequest =employeeDetailsRequest;	
}
  
}
