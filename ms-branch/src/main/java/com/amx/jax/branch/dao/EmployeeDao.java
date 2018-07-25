package com.amx.jax.branch.dao;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.branch.repository.EmployeeRepository;
import com.amx.jax.constant.ConstantDocument;
import com.amx.jax.dbmodel.Employee;
import com.amx.jax.dbmodel.UserVerificationCheckListModel;
import com.amx.jax.userservice.repository.UserVerificationCheckListModelRepository;



@Component
public class EmployeeDao {
	
	@Autowired
	private EmployeeRepository repo;	
	
	@Autowired
	private UserVerificationCheckListModelRepository checkListrepo;

	public Employee getEmployeeDetails(String civilId, BigDecimal ecNumber) {
		
		return repo.getEmployeeDetails(civilId,ecNumber,ConstantDocument.Deleted);
	}

	public Employee getEmployeeByCivilId(String civilId) {		
		return repo.getEmployeeByCivilId(civilId);
	}	
	
	public UserVerificationCheckListModel getCheckListForUserId(String civilId) {
		return checkListrepo.findOne(civilId);
	}

}
