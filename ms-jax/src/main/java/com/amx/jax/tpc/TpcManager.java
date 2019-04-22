package com.amx.jax.tpc;

import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.dbmodel.tpc.TpcClientMaster;
import com.amx.jax.repository.tpc.TpcClientMasterRepository;
import com.amx.utils.CryptoUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class TpcManager {

	@Autowired
	TpcDao tpcDao;
	@Autowired
	TpcClientMasterRepository tpcClientMasterRepository;

	public void generateSecret(String clientId, String actualSecret) {
		TpcClientMaster tpcClientMaster = tpcDao.getTpcMasterByClientId(clientId);
		try {
			String hash = CryptoUtil.getSHA1Hash(actualSecret);
			tpcClientMaster.setClientSecret(hash);
			tpcClientMasterRepository.save(tpcClientMaster);
		} catch (NoSuchAlgorithmException e) {
		}
	}
}
