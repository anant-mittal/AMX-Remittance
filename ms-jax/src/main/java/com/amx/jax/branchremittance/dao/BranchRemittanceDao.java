package com.amx.jax.branchremittance.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemitApplAmlModel;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.IRemitApplAmlRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class BranchRemittanceDao {
	
	@Autowired
	RemittanceApplicationRepository appRepo;

	@Autowired
	RemittanceApplicationBeneRepository appBeneRepo;

	@Autowired
	AdditionalInstructionDataRepository addlInstDataRepo;
	
	@Autowired
	IRemitApplAmlRepository applAmlRepository;

	
	@Transactional
	@SuppressWarnings("unchecked")
	public void saveAllApplications(HashMap<String, Object> mapAllDetailApplSave) {
		RemittanceApplication saveApplTrnx= (RemittanceApplication)  mapAllDetailApplSave.get("EX_APPL_TRNX");
		RemittanceAppBenificiary saveApplBene = (RemittanceAppBenificiary)mapAllDetailApplSave.get("EX_APPL_BENE") ;
		List<AdditionalInstructionData> saveApplAddlData = (List<AdditionalInstructionData>) mapAllDetailApplSave.get("EX_APPL_BENE");
		List<RemitApplAmlModel> saveApplAmlList = (List<RemitApplAmlModel>) mapAllDetailApplSave.get("EX_APPL_AML");
		
		if(saveApplTrnx!=null) {
			appRepo.save(saveApplTrnx);
		}
		if(saveApplBene!=null) {
			appBeneRepo.save(saveApplBene);
		}
		
		if(saveApplAddlData!=null) {
			addlInstDataRepo.save(saveApplAddlData);
		}
		if(saveApplAmlList!=null) {
			applAmlRepository.save(saveApplAmlList);
		}
		
	}
}



