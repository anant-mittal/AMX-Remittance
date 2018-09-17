package com.amx.jax.auditlogs;

import com.amx.jax.model.request.EmploymentDetailsRequest;

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
