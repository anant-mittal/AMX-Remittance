package com.amx.service_provider.api_gates.common;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.List;

import org.apache.axis.encoding.Base64;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;

public class PKCS7Signer
{
	public static String PATH_TO_KEYSTORE = null;
	public static String KEY_ALIAS_IN_KEYSTORE = null;
	public static String KEYSTORE_PASSWORD = null;
	public static String SIGNATUREALGO = null;

	static KeyStore loadKeyStore() throws Exception
	{
		KeyStore keystore = KeyStore.getInstance("JKS");
		InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
		keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
		return keystore;
	}

	static CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception
	{
		Security.addProvider(new BouncyCastleProvider());
		Certificate[] certchain = keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);
		final List<Certificate> certlist = new ArrayList<Certificate>();
		for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++)
		{
			certlist.add(certchain[i]);
		}
		Store certstore = new JcaCertStore(certlist);
		Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);
		ContentSigner signer =
				new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC")
						.build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray())));
		CMSSignedDataGenerator generator = new CMSSignedDataGenerator();
		generator.addSignerInfoGenerator(
				new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").build())
						.build(signer, (X509Certificate) cert));
		generator.addCertificates(certstore);
		return generator;
	}

	@SuppressWarnings("static-access")
	public static byte[] signPkcs7(String content) throws Exception
	{
		PKCS7Signer signer = new PKCS7Signer();
		KeyStore keyStore = signer.loadKeyStore();

		CMSSignedDataGenerator signatureGenerator = signer.setUpProvider(keyStore);
		CMSTypedData cmsdata = new CMSProcessableByteArray(content.getBytes("UTF-8"));
		CMSSignedData signeddata = signatureGenerator.generate(cmsdata, true);
		return signeddata.getEncoded();

	}

	public static void main(String[] args) throws Exception
	{		
		PrivateKey private_key = get_private_key("D:\\Exchange\\OWS\\Projects\\Vintaja\\App\\SSL\\private\\131011900001.pkcs8");

		byte[] data =
				"{\"id\":\"131011900001\",\"uid\":\"teller\",\"pwd\":\"p@ssw0rD\",\"code\":114,\"countryCode\":\"PH\",\"data\":{\"referenceNumber\":\"VIC00002\"}}"
						.getBytes("UTF8");

		Signature sig = Signature.getInstance("SHA1WithRSA");
		sig.initSign(private_key);
		sig.update(data);
		byte[] signatureBytes = sig.sign();
		System.out.println("Signature:" + Base64.encode(signatureBytes));
	}

	public static PrivateKey get_private_key(String filename) throws Exception
	{

		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePrivate(spec);
	}

	public static PublicKey get_public_key(String filename) throws Exception
	{

		byte[] keyBytes = Files.readAllBytes(Paths.get(filename));

		X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
		KeyFactory kf = KeyFactory.getInstance("RSA");
		return kf.generatePublic(spec);
	}
}
