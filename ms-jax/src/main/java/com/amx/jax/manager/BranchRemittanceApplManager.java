package com.amx.jax.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.model.request.remittance.BranchRemittanceApplRequestModel;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BranchRemittanceApplManager {
	
	
	@Autowired
	BranchRemittanceManager branchRemitManager;
	

	
	public void saveBranchApplication(BranchRemittanceApplRequestModel requestApplModel) {
		//Customer Validation
		//Beneficiary validation
		//checkingStaffIdNumberWithCustomer
		 
		 branchRemitManager.checkingStaffIdNumberWithCustomer();
		 branchRemitManager.beneAccountException(requestApplModel.getRelationshipId());
		 branchRemitManager.checkBeneAccountType(requestApplModel.getRelationshipId());
		 branchRemitManager.beneAddCheck(requestApplModel.getRelationshipId());
		
	}

}
