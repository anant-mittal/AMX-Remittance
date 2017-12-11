package com.amx.jax.manager;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.model.request.RemittanceTransactionRequestModel;
import com.amx.jax.repository.IBeneficiaryOnlineDao;

@Component
public class RemittanceTransactionManager {

	@Autowired
	IBeneficiaryOnlineDao beneficiaryOnlineDao;
	
	public Object validateTransactionData(RemittanceTransactionRequestModel model) {
		
		BigDecimal beneId = model.getBeneId();
		beneficiaryOnlineDao.findOne(beneId);
		return model;

		
	}

	
}
