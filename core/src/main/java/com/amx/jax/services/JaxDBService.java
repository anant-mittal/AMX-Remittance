package com.amx.jax.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.amx.jax.dbmodel.Employee;
import com.amx.jax.meta.MetaData;
import com.amx.jax.repository.EmployeeRespository;

import org.springframework.web.context.WebApplicationContext;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class JaxDBService {

	@Autowired
	MetaData metaData;
	@Autowired
	EmployeeRespository employeeRespository;

	public String getCreatedOrUpdatedBy() {
		BigDecimal employeeId = metaData.getEmployeeId();
		String createdBy = null;
		if (employeeId != null) {
			Employee employeeDetails = employeeRespository.findEmployeeById(employeeId);
			createdBy = employeeDetails.getUserName();
		}
		return createdBy;
	}
}
