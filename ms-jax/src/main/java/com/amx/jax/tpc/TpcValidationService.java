package com.amx.jax.tpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.amxlib.exception.jax.GlobalException;
import com.amx.amxlib.util.JaxValidationUtil;
import com.amx.jax.dbmodel.tpc.TpcClientMaster;

@Component
public class TpcValidationService {

	@Autowired
	TpcDao tpcDao;

	public void validateGenerateSecretRequeset(String clientId, String actualSecret) {
		JaxValidationUtil.validateNonEmpty(clientId, "clientId");
		JaxValidationUtil.validateNonEmpty(actualSecret, "actualSecret");
		TpcClientMaster tpcClientMaster = tpcDao.getTpcMasterByClientId(clientId);
		if (tpcClientMaster == null) {
			throw new GlobalException("tpc clinet withgiven id not found");
		}
	}

}
