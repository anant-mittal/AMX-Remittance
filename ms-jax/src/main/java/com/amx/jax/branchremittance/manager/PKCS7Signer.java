package com.amx.jax.branchremittance.manager;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.encoding.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.amx.jax.config.JaxTenantProperties;

@Component
public class PKCS7Signer implements Serializable{
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
	
	public PKCS7Signer() {
		
	}

	public PKCS7Signer(String path_to_keystore, String key_alias_in_keystore, String keystore_password, String signature_algo) {
		super();
		PATH_TO_KEYSTORE = path_to_keystore;
		KEY_ALIAS_IN_KEYSTORE = key_alias_in_keystore;
		KEYSTORE_PASSWORD = keystore_password;
		SIGNATUREALGO = signature_algo;
	}

	static KeyStore loadKeyStore() throws Exception {
		KeyStore keystore = KeyStore.getInstance("JKS");
		InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
		return keystore;
	}

	static CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		Certificate[] certchain = keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);
		final List<Certificate> certlist = new ArrayList<Certificate>();
		for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
			certlist.add(certchain[i]);
		}
		Store certstore = new JcaCertStore(certlist);
		Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);
		ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC")
				.build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray())));
		CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
		generator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build()).build(signer, (X509Certificate) cert));
		generator.addCertificates(certstore);
		return generator;
	}

	@SuppressWarnings("static-access")
	static byte[] signPkcs7(String content, String path_to_keystore, String key_alias_in_keystore, String keystore_password, String signature_algo) throws Exception {
		PKCS7Signer signer = new PKCS7Signer(path_to_keystore, key_alias_in_keystore, keystore_password, signature_algo);
		KeyStore keyStore = signer.loadKeyStore();
		CMSSignedDataGenerator signatureGenerator = signer.setUpProvider(keyStore);
		CMSTypedData cmsdata = new CMSProcessableByteArray(content.getBytes("UTF-8"));
		CMSSignedData signeddata = signatureGenerator.generate(cmsdata, true);
		return signeddata.getEncoded();

	}

	
	public  String getSignature(String str_to_sing) {
		String str_after_sing = null;
		String path_to_keystore = jaxTenantProperties.getKeyStoreLocatin(); 
		String key_alias_in_keystore = jaxTenantProperties.getKeyStoreAlias();
		String keystore_password = jaxTenantProperties.getKeyStorePwd();
		String signature_algo = jaxTenantProperties.getSigAlgorithem();
		
		try {
			str_after_sing = new String(Base64.encode(PKCS7Signer.signPkcs7(str_to_sing, path_to_keystore, key_alias_in_keystore, keystore_password, signature_algo)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return str_after_sing;
	}
	
	/*public static void main(String[] args) throws Exception {
	//	String str_after_sing = this.getSignature("test");
		String str_after_sing = null, str_to_sing = "23720191070910443INR160802401560645946KEEGAN DENIS SIMOES01/08/2019 04:13:36M23720191070910443INR160802401560645946KEEGAN DENIS SIMOES01/08/2019 04:13:36 PM";

		String path_to_keystore = "C:\\Users\\rabil\\Desktop\\sign\\test_key.jks", key_alias_in_keystore = "almullaexchange", keystore_password = "changeit", signature_algo = "SHA256WITHRSA";

		try {
			PKCS7Signer pk = new PKCS7Signer();
			String str = pk.getSignature(str_to_sing);
			System.out.println("RR 111  Signed Encoded str : "+str); 

			str_after_sing = new String(Base64.encode(PKCS7Signer.signPkcs7(str_to_sing, path_to_keystore, key_alias_in_keystore, keystore_password, signature_algo)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("RR Signed Encoded Bytes: 222 " + str_after_sing);
	}*/

}
