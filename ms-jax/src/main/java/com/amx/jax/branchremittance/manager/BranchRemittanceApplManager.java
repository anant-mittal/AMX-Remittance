package com.amx.jax.branchremittance.manager;

/**
 * @author rabil 
 * @date 21/01/2019
 */
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
		/**checkingStaffIdNumberWithCustomer **/
		 branchRemitManager.checkingStaffIdNumberWithCustomer();
		 /**checking bene account exception  **/
		 branchRemitManager.beneAccountException(requestApplModel.getRelationshipId());
		 /**checking bene account type **/
		 branchRemitManager.checkBeneAccountType(requestApplModel.getRelationshipId());
		 /**checking bene additional details **/
		 branchRemitManager.beneAddCheck(requestApplModel.getRelationshipId());
		 /**checking banned bank details **/
		 branchRemitManager.bannedBankCheck(requestApplModel.getRelationshipId());
		
	}

}
