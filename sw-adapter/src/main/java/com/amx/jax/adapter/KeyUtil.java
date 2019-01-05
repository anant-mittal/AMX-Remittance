package com.amx.jax.adapter;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.slf4j.Logger;

import com.amx.jax.device.DeviceRestModels.DevicePairingCreds;
import com.amx.jax.dict.UserClient.ClientType;
import com.amx.jax.logger.LoggerService;
import com.amx.utils.CryptoUtil.HashBuilder;
import com.amx.utils.JsonUtil;
import com.amx.utils.NetworkAdapter.NetAddress;

import net.east301.keyring.BackendNotSupportedException;
import net.east301.keyring.Keyring;
import net.east301.keyring.PasswordRetrievalException;
import net.east301.keyring.PasswordSaveException;
import net.east301.keyring.util.LockException;

public class KeyUtil {

	private static final Logger LOGGER = LoggerService.getLogger(KeyUtil.class);

	public static String PREFIX = "mxadapterv1";
	public static String SUFFIX = ".keystore";
	private static String SERVICE_PREFIX = "mx-adapter";
	private static String SERVICE_NAME = null;
	public static Keyring KEYRING = null;

	// 8-byte Salt
	static byte[] salt = {
			(byte) 0xA9, (byte) 0x9B, (byte) 0xC8, (byte) 0x32,
			(byte) 0x56, (byte) 0x35, (byte) 0xE3, (byte) 0x03
	};
	// Iteration count
	static int iterationCount = 19;

	public static Keyring getKeyRing() throws BackendNotSupportedException, IOException, KeyStoreException,
			CertificateException, NoSuchAlgorithmException {
		if (KEYRING == null) {
			Keyring keyRing = Keyring.create();
			if (keyRing.isKeyStorePathRequired()) {
				File keyStoreFile = new File(
						System.getenv("APPDATA")
								// System.getProperty("user.home")
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

	public static void setServiceName(String serviceName) {
		KeyUtil.SERVICE_NAME = String.format("%s-%s", SERVICE_PREFIX, serviceName);
	}

	private static String generateKey(NetAddress address) {
		return new HashBuilder()
				.message(String.format("%s|%s|%s", address.getMac(), address.getHostName(), address.getUserName()))
				.toSHA2().output();
	}

	public static DevicePairingCreds getDevicePairingCreds(NetAddress address)
			throws LockException, PasswordRetrievalException, InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, IOException {
		String key = generateKey(address);
		String passwordEncd = decrypt(key, KEYRING.getPassword(SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString()));
		byte[] terminalCredsByts = Base64.getDecoder().decode(passwordEncd);
		String terminalCredsStrs = new String(terminalCredsByts);
		return JsonUtil.fromJson(terminalCredsStrs, DevicePairingCreds.class);
	}

	public static DevicePairingCreds setDevicePairingCreds(DevicePairingCreds devicePairingCreds, NetAddress address)
			throws LockException, PasswordSaveException, InvalidKeyException, NoSuchAlgorithmException,
			InvalidKeySpecException, NoSuchPaddingException, InvalidAlgorithmParameterException,
			UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String key = generateKey(address);
		String terminalCredsStrs = JsonUtil.toJson(devicePairingCreds);
		String passwordEncd = Base64.getEncoder().encodeToString(terminalCredsStrs.getBytes());
		if (KEYRING != null) {
			KEYRING.setPassword(KeyUtil.SERVICE_NAME, ClientType.BRANCH_ADAPTER.toString(),
					encrypt(key, passwordEncd));
		}
		return devicePairingCreds;
	}

	public static String encrypt(String secretKey, String plainText)
			throws NoSuchAlgorithmException,
			InvalidKeySpecException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			UnsupportedEncodingException,
			IllegalBlockSizeException,
			BadPaddingException {
		// Key generation for enc and desc
		KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
		// Prepare the parameter to the ciphers
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

		// Enc process
		Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
		ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
		String charSet = "UTF-8";
		byte[] in = plainText.getBytes(charSet);
		byte[] out = ecipher.doFinal(in);
		String encStr = new String(Base64.getEncoder().encode(out));
		return encStr;
	}

	public static String decrypt(String secretKey, String encryptedText)
			throws NoSuchAlgorithmException,
			InvalidKeySpecException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			UnsupportedEncodingException,
			IllegalBlockSizeException,
			BadPaddingException,
			IOException {
		// Key generation for enc and desc
		KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), salt, iterationCount);
		SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);
		// Prepare the parameter to the ciphers
		AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);
		// Decryption process; same key will be used for decr
		Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
		dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
		byte[] enc = Base64.getDecoder().decode(encryptedText);
		byte[] utf8 = dcipher.doFinal(enc);
		String charSet = "UTF-8";
		String plainStr = new String(utf8, charSet);
		return plainStr;
	}

}
