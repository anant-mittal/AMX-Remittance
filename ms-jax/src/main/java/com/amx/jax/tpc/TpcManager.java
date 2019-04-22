package com.amx.jax.tpc;

import java.security.NoSuchAlgorithmException;

import org.jasypt.util.text.BasicTextEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.amx.jax.config.JaxProperties;
import com.amx.jax.dbmodel.tpc.TpcClientMaster;
import com.amx.jax.repository.tpc.TpcClientMasterRepository;
import com.amx.utils.CryptoUtil;

@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public class TpcManager {

	@Autowired
	BasicTextEncryptor jaxTpBasicTextEncryptor;
	@Autowired
	TpcDao tpcDao;
	@Autowired
	TpcClientMasterRepository tpcClientMasterRepository;
	@Autowired
	JaxProperties jaxProperties;

	public String generateClientSecretHash(String clientId, String actualClientSecret) {
		TpcClientMaster tpcClientMaster = tpcDao.getTpcMasterByClientId(clientId);
		String hash = null;
		try {
			hash = CryptoUtil.getSHA2Hash(actualClientSecret);
			tpcClientMaster.setClientSecret(hash);
			tpcClientMasterRepository.save(tpcClientMaster);
		} catch (NoSuchAlgorithmException e) {
		}
		return hash;
	}

	public String encryptClientSecret(String clientSecretHash) {
		return jaxTpBasicTextEncryptor.encrypt(clientSecretHash);
	}
}
