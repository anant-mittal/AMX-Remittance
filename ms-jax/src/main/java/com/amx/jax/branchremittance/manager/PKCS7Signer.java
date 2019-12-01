package com.amx.jax.branchremittance.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Signature;

import org.apache.axis.encoding.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.amx.jax.config.JaxTenantProperties;

@Component
public class PKCS7Signer implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static String PATH_TO_KEYSTORE = null;
	private static String KEY_ALIAS_IN_KEYSTORE = null;
	private static String KEYSTORE_PASSWORD = null;
	private static String SIGNATUREALGO = null;

	@Autowired
	JaxTenantProperties jaxTenantProperties;

//	public PKCS7Signer() {
//
//		String path_to_keystore = jaxTenantProperties.getKeyStoreLocatin();
//		String key_alias_in_keystore = jaxTenantProperties.getKeyStoreAlias();
//		String keystore_password = jaxTenantProperties.getKeyStorePwd();
//		String signature_algo = jaxTenantProperties.getSigAlgorithem();
//
//		PATH_TO_KEYSTORE = path_to_keystore;
//		KEY_ALIAS_IN_KEYSTORE = key_alias_in_keystore;
//		KEYSTORE_PASSWORD = keystore_password;
//		SIGNATUREALGO = signature_algo;
//	}
//
//	public PKCS7Signer(String path_to_keystore, String key_alias_in_keystore, String keystore_password,
//			String signature_algo) {
//		PATH_TO_KEYSTORE = path_to_keystore;
//		KEY_ALIAS_IN_KEYSTORE = key_alias_in_keystore;
//		KEYSTORE_PASSWORD = keystore_password;
//		SIGNATUREALGO = signature_algo;
//	}

	public static void setParams(String path_to_keystore, String key_alias_in_keystore, String keystore_password,
			String signature_algo) {
		PATH_TO_KEYSTORE = path_to_keystore;
		KEY_ALIAS_IN_KEYSTORE = key_alias_in_keystore;
		KEYSTORE_PASSWORD = keystore_password;
		SIGNATUREALGO = signature_algo;
	}

	private static KeyStore loadKeyStore() throws Exception {
		KeyStore keystore = KeyStore.getInstance("JKS");
		InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
		return keystore;
	}

	public  String getSignature(String plainText) throws Exception {
		 PATH_TO_KEYSTORE =jaxTenantProperties.getKeyStoreLocatin(); 
		 KEY_ALIAS_IN_KEYSTORE =jaxTenantProperties.getKeyStoreAlias(); 
		 KEYSTORE_PASSWORD =jaxTenantProperties.getKeyStorePwd();
		 SIGNATUREALGO =jaxTenantProperties.getSigAlgorithem(); 
		KeyStore keyStore = loadKeyStore();
		PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray());

		Signature privateSignature = Signature.getInstance(SIGNATUREALGO);
		privateSignature.initSign(privateKey);
		privateSignature.update(plainText.getBytes("UTF-8"));
		byte[] signature = privateSignature.sign();
		return Base64.encode(signature);
	}

	/*public static void main(String[] args) throws Exception {
		System.out.println("helo");
		 String str_to_sing = "23720191070910443INR160802401560645946KEEGAN DENIS SIMOES01/08/2019 04:13:36M23720191070910443INR160802401560645946KEEGANDENISSIMOES01/08/2019 04:13:36 PM";

		//String str_to_sing = "444";

		String path_to_keystore = "D:\\sign\\test_key.jks",
				key_alias_in_keystore = "almullaexchange", keystore_password = "changeit",
				signature_algo = "SHA256WITHRSA";

		try {
			// Signer signer = new Signer(path_to_keystore, key_alias_in_keystore,
			// keystore_password, signature_algo);
			
			PKCS7Signer.setParams(path_to_keystore, key_alias_in_keystore, keystore_password, signature_algo);
			String str = PKCS7Signer.getSignature(str_to_sing);
			System.out.println("Signed Encoded str : " + str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
}
