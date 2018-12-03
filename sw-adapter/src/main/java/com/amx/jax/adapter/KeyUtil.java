package com.amx.jax.adapter;

import java.io.File;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Base64;

import org.slf4j.Logger;

import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.JsonUtil;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.Keyring;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

public class KeyUtil {

	private static final Logger LOGGER = LoggerService.getLogger(KeyUtil.class);

	public static String PREFIX = "mxbranchadapter";
	public static String SUFFIX = ".keystore";
	public static String SERVICE_NAME = "amx-adapter";
	public static Keyring KEYRING = null;

	public static Keyring getKeyRing() throws BackendNotSupportedException, IOException, KeyStoreException,
			CertificateException, NoSuchAlgorithmException {
		if (KEYRING == null) {
			Keyring keyRing = Keyring.create();
			if (keyRing.isKeyStorePathRequired()) {
				File keyStoreFile = new File(
						System.getenv("APPDATA") 
						//System.getProperty("user.home") 
						+ File.separator + "BranchAdapter" + File.separator + SUFFIX);
				if (!keyStoreFile.exists()) {
					keyStoreFile.getParentFile().mkdirs();
				}
				keyRing.setKeyStorePath(keyStoreFile.getPath());
			}
			KEYRING = keyRing;
		}
		return KEYRING;
	}

	public static DevicePairingCreds getDevicePairingCreds() throws LockException, PasswordRetrievalException {
		String passwordEncd = KEYRING.getPassword(SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString());
		byte[] terminalCredsByts = Base64.getDecoder().decode(passwordEncd);
		String terminalCredsStrs = new String(terminalCredsByts);
		return JsonUtil.fromJson(terminalCredsStrs, DevicePairingCreds.class);
	}

	public static DevicePairingCreds setDevicePairingCreds(DevicePairingCreds devicePairingCreds)
			throws LockException, PasswordSaveException {
		String terminalCredsStrs = JsonUtil.toJson(devicePairingCreds);
		String passwordEncd = Base64.getEncoder().encodeToString(terminalCredsStrs.getBytes());
		if (KEYRING != null) {
			KEYRING.setPassword(KeyUtil.SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString(),
					passwordEncd);
		}
		return devicePairingCreds;
	}

}
