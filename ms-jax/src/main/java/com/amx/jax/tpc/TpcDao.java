package com.amx.jax.tpc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.dbmodel.tpc.TpcClientMaster;
import com.amx.jax.repository.tpc.TpcClientMasterRepository;

@Component
public class TpcDao {

	@Autowired
	TpcClientMasterRepository tpcClientMasterRepository;

	public TpcClientMaster getTpcMasterByClientId(String clientId) {
		return tpcClientMasterRepository.findByClientId(clientId);
	}

}
