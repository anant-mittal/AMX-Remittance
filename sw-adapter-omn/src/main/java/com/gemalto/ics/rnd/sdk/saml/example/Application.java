/*
 *  (c) Copyright Gemalto, 2017
 *  ALL RIGHTS RESERVED UNDER COPYRIGHT LAWS.
 *  CONTAINS CONFIDENTIAL AND TRADE SECRET INFORMATION.
 *  GEMALTO MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF
 *  THE SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
 *  TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 *  PARTICULAR PURPOSE, OR NON-INFRINGEMENT. GEMALTO SHALL NOT BE
 *  LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING,
 *  MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 *  THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 *  CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
 *  PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
 *  NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
 *  SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
 *  SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
 *  PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). GEMALTO
 *  SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
 *  HIGH RISK ACTIVITIES.
 */
package com.gemalto.ics.rnd.sdk.saml.example;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.X509Certificate;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.apache.xml.security.algorithms.JCEMapper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import com.amx.utils.ArgUtil;
import com.gemalto.ics.dc.oids.sdk.saml.OmanSingleSignOnClient;
import com.gemalto.ics.rnd.sdk.saml.SAMLInitializer;
import com.gemalto.ics.rnd.sdk.saml.configuration.BasicConfigurationProvider;
import com.gemalto.ics.rnd.sdk.saml.configuration.ClientConfiguration;
import com.gemalto.ics.rnd.sdk.saml.configuration.ConfigurationProvider;
import com.gemalto.ics.rnd.sdk.saml.configuration.DigestMethod;
import com.gemalto.ics.rnd.sdk.saml.configuration.IdpMetadata;
import com.gemalto.ics.rnd.sdk.saml.configuration.SignatureAlgorithm;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpCredential;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpMetadata;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpSigningCredential;
import com.gemalto.ics.rnd.sdk.saml.exception.MetadataException;
import com.gemalto.ics.rnd.sdk.saml.exception.SdkInitializationException;
import com.gemalto.ics.rnd.sdk.saml.util.KeyStoreUtils;

@SpringBootApplication
public class Application extends SpringBootServletInitializer {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		final Provider provider = new BouncyCastleProvider();
		Security.addProvider(provider);
		LOGGER.info("BC provider installed");

		JCEMapper.setProviderId(provider.getName());
		LOGGER.info("Provider [{}] set for Apache Santuario", provider);

		try {
			SAMLInitializer.initialize();
		} catch (SdkInitializationException e) {
			throw new IllegalStateException("Error initializing SAML SDK", e);
		}
		LOGGER.info("SAML initialized");

		return application.sources(Application.class);
	}

	@Bean
	protected VelocityEngine getVelocityEngine() throws Exception {
		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(RuntimeConstants.RESOURCE_LOADER, "classpath");
		engine.setProperty("classpath.resource.loader.class", ClasspathResourceLoader.class.getName());
		engine.init();
		LOGGER.info("Velocity engine initialized");
		return engine;
	}

	@Bean
	public OmanSingleSignOnClient getSingleSignOnClient(ConfigurationProvider configurationProvider,
			VelocityEngine velocityEngine) {
		OmanSingleSignOnClient client = new OmanSingleSignOnClient(configurationProvider, velocityEngine);
		return client;
	}

	@Bean
	public ConfigurationProvider getConfigurationProvider(
			@Value("${idpMetadata}") String idpMetadataName, @Value("${spMetadata}") String spMetadataName,
			@Value("${spSigningCredential}") String spSigningCredential,
			@Value("${spSigningCredential.password}") String spSigningCredentialPassword,
			@Value("${spEncryptingCredential}") String spEncryptingCredential,
			@Value("${spEncryptingCredential.password}") String spEncryptingCredentialPassword) {
		final ClientConfiguration configuration = new ClientConfiguration();
		IdpMetadata idpMetadata = new IdpMetadata();
		try (InputStream idpStream = getResourceInputStream(idpMetadataName)) {
			if (idpStream != null) {
				idpMetadata = IdpMetadata.load(idpStream);
				LOGGER.info("IDP metadata [{}] loaded", idpMetadataName);
			}
		} catch (IOException e) {
			LOGGER.warn("Error loading idp-metadata.xml file from the path: " + idpMetadataName, e);
		} catch (MetadataException e) {
			LOGGER.warn("Error loading IDP metadata", e);
		}

		SpMetadata spMetadata = new SpMetadata();
		try (InputStream spStream = getResourceInputStream(spMetadataName)) {
			if (spStream != null) {
				spMetadata = SpMetadata.load(spStream);
				LOGGER.info("SP metadata [{}] loaded", spMetadataName);
			}
		} catch (IOException e) {
			LOGGER.warn("Error loading sp-metadata.xml file from the path: " + spMetadataName, e);
		} catch (MetadataException e) {
			LOGGER.warn("Error loading SP metadata", e);
		}

		configuration.setIdpMetadata(idpMetadata);
		configuration.setSpMetadata(spMetadata);
		configuration
				.setSpSigningCredential(createSpSigningCredential(spSigningCredential, spSigningCredentialPassword));
		configuration.setSpEncryptingCredential(
				createSpEncryptingCredential(spEncryptingCredential, spEncryptingCredentialPassword));
		return new BasicConfigurationProvider(configuration);
	}

	protected InputStream getResourceInputStream(String name) {
		if (!ArgUtil.isEmpty(name)) {
			ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(
					this.getClass().getClassLoader());
			Resource resource = resolver.getResource(name);
			if (resource != null) {
				try {
					return resource.getInputStream();
				} catch (IOException e) {
					LOGGER.debug("Error to open resource [{}]", name);
				}
			}
		}
		return null;
	}

	protected SpSigningCredential createSpSigningCredential(String signingCredentialName,
			String signingCredentialPassword) {
		final SpSigningCredential credential = new SpSigningCredential();
		credential.setDigestMethod(DigestMethod.SHA256);
		credential.setSignatureAlgorithm(SignatureAlgorithm.SHA256_WITH_RSA);

		try (InputStream is = getResourceInputStream(signingCredentialName)) {
			if (is != null) {
				KeyStore.PrivateKeyEntry ksPrivateKeyEntry = KeyStoreUtils.loadKeyFromKeyStore(is,
						signingCredentialPassword, KeyStoreUtils.PKCS12);

				credential.setPrivateKey(ksPrivateKeyEntry.getPrivateKey());
				credential.setCertificate((X509Certificate) ksPrivateKeyEntry.getCertificate());
				LOGGER.info("SP signing credential [{}] loaded", signingCredentialName);
			}
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.warn("Error to load SP signing credential [{}], error message: {}", signingCredentialName,
					e.getMessage());
		}

		return credential;
	}

	protected SpCredential createSpEncryptingCredential(String encryptingCredentialName,
			String encryptingCredentialPassword) {
		final SpCredential credential = new SpCredential();

		try (InputStream is = getResourceInputStream(encryptingCredentialName)) {
			if (is != null) {
				KeyStore.PrivateKeyEntry ksPrivateKeyEntry = KeyStoreUtils.loadKeyFromKeyStore(is,
						encryptingCredentialPassword, KeyStoreUtils.PKCS12);

				credential.setPrivateKey(ksPrivateKeyEntry.getPrivateKey());
				credential.setCertificate((X509Certificate) ksPrivateKeyEntry.getCertificate());
				LOGGER.info("SP encrypting credential [{}] loaded", encryptingCredentialName);
			}
		} catch (GeneralSecurityException | IOException e) {
			LOGGER.warn("Error to load SP encrypting credential [{}], error message: {}", encryptingCredentialName,
					e.getMessage());
		}

		return credential;
	}

}
