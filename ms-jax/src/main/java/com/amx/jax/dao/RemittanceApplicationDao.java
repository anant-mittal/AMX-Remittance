package com.amx.jax.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.remittance.AdditionalInstructionData;
import com.amx.jax.dbmodel.remittance.RemittanceAppBenificiary;
import com.amx.jax.dbmodel.remittance.RemittanceApplication;
import com.amx.jax.repository.AdditionalInstructionDataRepository;
import com.amx.jax.repository.RemittanceApplicationBeneRepository;
import com.amx.jax.repository.RemittanceApplicationRepository;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RemittanceApplicationDao {

	@Autowired
	RemittanceApplicationRepository appRepo;

	@Autowired
	RemittanceApplicationBeneRepository appBeneRepo;

	@Autowired
	AdditionalInstructionDataRepository addlInstDataRepo;

	@Transactional
	public void saveAllApplicationData(RemittanceApplication app, RemittanceAppBenificiary appBene,
			List<AdditionalInstructionData> additionalInstrumentData) {
		appRepo.save(app);
		appBeneRepo.save(appBene);
		addlInstDataRepo.save(additionalInstrumentData);
	}

}
