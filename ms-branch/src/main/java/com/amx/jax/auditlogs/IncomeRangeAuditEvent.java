package com.amx.jax.auditlogs;

import com.amx.amxlib.model.request.EmploymentDetailsRequest;

public class IncomeRangeAuditEvent extends JaxAuditEvent {

EmploymentDetailsRequest employeeDetailsRequest;

public EmploymentDetailsRequest getEmployeeDetailsRequest() {
	return employeeDetailsRequest;
}

public void setEmployeeDetailsRequest(EmploymentDetailsRequest employeeDetailsRequest) {
	this.employeeDetailsRequest = employeeDetailsRequest;
}
  
public IncomeRangeAuditEvent(EmploymentDetailsRequest employeeDetailsRequest) {
	super(Type.INCOME_RANGE);
	this.employeeDetailsRequest =employeeDetailsRequest;	
}
  
}
