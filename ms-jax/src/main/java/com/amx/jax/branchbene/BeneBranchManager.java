package com.amx.jax.branchbene;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.bene.BeneficaryRelationship;
import com.amx.jax.error.JaxError;
import com.amx.jax.model.request.benebranch.UpdateBeneStatusRequest;
import com.amx.jax.repository.IBeneficiaryRelationshipDao;
import com.amx.jax.services.BeneficiaryService;
import com.jax.amxlib.exception.jax.GlobaLException;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class BeneBranchManager {

	@Autowired
	IBeneficiaryRelationshipDao iBeneficiaryRelationshipDao;
	@Autowired
	BeneficiaryService beneficiaryService;

	public void updateBeneStatus(UpdateBeneStatusRequest request) {
		BeneficaryRelationship beneRel = iBeneficiaryRelationshipDao.findOne(BigDecimal.valueOf(request.getBeneRelationshipSeqId()));
		if(beneRel == null) {
			throw new GlobaLException(JaxError.JAX_FIELD_VALIDATION_FAILURE, "Bene not found");
		}
		beneRel.setIsActive(request.getStatusCode().getDbFlag());
		iBeneficiaryRelationshipDao.save(beneRel);
	}

}
