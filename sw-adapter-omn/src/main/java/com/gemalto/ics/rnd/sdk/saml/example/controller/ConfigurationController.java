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
package com.gemalto.ics.rnd.sdk.saml.example.controller;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.gemalto.ics.rnd.sdk.saml.configuration.ClientConfiguration;
import com.gemalto.ics.rnd.sdk.saml.configuration.ConfigurationProvider;
import com.gemalto.ics.rnd.sdk.saml.configuration.DigestMethod;
import com.gemalto.ics.rnd.sdk.saml.configuration.IdpMetadata;
import com.gemalto.ics.rnd.sdk.saml.configuration.SignatureAlgorithm;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpCredential;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpMetadata;
import com.gemalto.ics.rnd.sdk.saml.configuration.SpSigningCredential;
import com.gemalto.ics.rnd.sdk.saml.example.i18n.I18n;
import com.gemalto.ics.rnd.sdk.saml.example.model.ConfigurationModel;
import com.gemalto.ics.rnd.sdk.saml.exception.SAMLException;
import com.gemalto.ics.rnd.sdk.saml.util.KeyStoreUtils;

/**
 * The {@code ConfigurationController} class implements a controller managing
 * configuration.
 */
@Controller
public class ConfigurationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationController.class);

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ConfigurationProvider configurationProvider;

	@RequestMapping(value = "/configuration", method = RequestMethod.GET)
	public ModelAndView index(Locale locale) {
		final ClientConfiguration configuration = configurationProvider.getClientConfiguration();
		ConfigurationModel configurationModel = new ConfigurationModel();
		configurationModel.setConfiguration(configuration);

		Map<String, Object> model = new LinkedHashMap<>();
		putEnums(model, locale);
		model.put("cfgModel", configurationModel);
		return new ModelAndView("configuration", model);
	}

	@RequestMapping(value = "/configuration", method = RequestMethod.POST)
	public ModelAndView save(@ModelAttribute("cfgModel") ConfigurationModel configurationModel, BindingResult bindingResult,
			RedirectAttributes redirectAttributes, Locale locale) {

		if (!bindingResult.hasErrors()) {
			updateSpMetadata(configurationModel, bindingResult);
			updateSpCredentials(configurationModel, bindingResult);
			updateIdpMetadata(configurationModel, bindingResult);
		}

		if (bindingResult.hasErrors()) {
			Map<String, Object> model = new LinkedHashMap<>();
			putEnums(model, locale);
			return new ModelAndView("configuration", model);

		} else {
			redirectAttributes.addFlashAttribute("infoMessage", messageSource.getMessage("configuration.saved", null, locale));

			final Integer activeTabIndex;
			if (!configurationModel.getSpMetadataFile().isEmpty()) {
				activeTabIndex = 0;
			} else if (!configurationModel.getSpPrivateKeyFile().isEmpty()) {
				activeTabIndex = 1;
			} else if (!configurationModel.getIdpMetadataFile().isEmpty() || !configurationModel.getIdpCertificateFile().isEmpty()) {
				activeTabIndex = 2;
			} else {
				activeTabIndex = null;
			}
			redirectAttributes.addFlashAttribute("activeTabIndex", activeTabIndex);
			return new ModelAndView(new RedirectView("/configuration", true));
		}
	}

	protected void putEnums(Map<String, Object> model, Locale locale) {
		model.put("signatureAlgos", I18n.enumAsMap(SignatureAlgorithm.class, messageSource, locale));
		model.put("digestMethods", I18n.enumAsMap(DigestMethod.class, messageSource, locale));
	}

	protected void updateSpMetadata(ConfigurationModel model, Errors errors) {
		final ClientConfiguration configuration = configurationProvider.getClientConfiguration();

		if (model.getSpMetadataFile().isEmpty()) {
			final SpMetadata currentMetadata = configuration.getSpMetadata();
			final SpMetadata newMetadata = model.getConfiguration().getSpMetadata();
			currentMetadata.setEntityId(newMetadata.getEntityId());
			currentMetadata.setAcsLocation(newMetadata.getAcsLocation());
			currentMetadata.setSlsLocation(newMetadata.getSlsLocation());
			currentMetadata.setSlsResponseLocation(newMetadata.getSlsResponseLocation());
			currentMetadata.setAuthnRequestsSigned(newMetadata.isAuthnRequestsSigned());

		} else {
			InputStream is = null;
			try {
				is = model.getSpMetadataFile().getInputStream();
				SpMetadata newSpMetadata = SpMetadata.load(is);
				configuration.setSpMetadata(newSpMetadata);
			} catch (SAMLException | IOException e) {
				LOGGER.info("Error loading SP configuration from metadata file", e);
				errors.rejectValue("spMetadataFile", "error.spMetadataFile.import", new Object[]{e.getMessage()}, null);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}
	}

	protected void updateSpCredentials(ConfigurationModel model, Errors errors) {
		final ClientConfiguration configuration = configurationProvider.getClientConfiguration();
		final SpSigningCredential signingCredential = configuration.getSpSigningCredential();
		signingCredential.setSignatureAlgorithm(model.getConfiguration().getSpSigningCredential().getSignatureAlgorithm());
		signingCredential.setDigestMethod(model.getConfiguration().getSpSigningCredential().getDigestMethod());

		if (!model.getSpPrivateKeyFile().isEmpty()) {
			try {
				updateSpCredentialFromKeyStore(signingCredential, model.getSpPrivateKeyFile(), model.getSpPrivateKeyPassword());
			} catch (Exception e) {
				LOGGER.info("Error loading signing private key from the key store", e);
				errors.rejectValue("spPrivateKeyFile", "error.spPrivateKeyFile.import", new Object[]{e.getMessage()}, null);
			}
		}
		if (!model.getSpEncryptionPrivateKeyFile().isEmpty()) {
			SpCredential spEncryptingCredential = configuration.getSpEncryptingCredential();
			try {
				updateSpCredentialFromKeyStore(spEncryptingCredential, model.getSpEncryptionPrivateKeyFile(),
						model.getSpEncryptionPrivateKeyPassword());
			} catch (Exception e) {
				LOGGER.info("Error loading encryption private key from the key store", e);
				errors.rejectValue("spEncryptionPrivateKeyFile", "error.spPrivateKeyFile.import", new Object[]{e.getMessage()}, null);
			}
		}
	}

	/**
	 * Updates the specified SP credential with the private key and certificate
	 * loaded from the specified key store.
	 *
	 * @param credential
	 *            the SP credential to be updated
	 * @param keyStoreFile
	 *            the file containing the key store
	 * @param keyStorePassword
	 *            the password to access the key store
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws GeneralSecurityException
	 *             if a security-related error occurs such as wrong password
	 */
	protected void updateSpCredentialFromKeyStore(SpCredential credential, MultipartFile keyStoreFile, String keyStorePassword)
			throws IOException, GeneralSecurityException {
		try (InputStream stream = keyStoreFile.getInputStream()) {
			KeyStore.PrivateKeyEntry ksPrivateKeyEntry = KeyStoreUtils.loadKeyFromKeyStore(stream, keyStorePassword, KeyStoreUtils.PKCS12);
			credential.setPrivateKey(ksPrivateKeyEntry.getPrivateKey());
			credential.setCertificate((X509Certificate) ksPrivateKeyEntry.getCertificate());
		}
	}

	protected void updateIdpMetadata(ConfigurationModel model, Errors errors) {
		final ClientConfiguration configuration = configurationProvider.getClientConfiguration();

		if (model.getIdpMetadataFile().isEmpty()) {
			final IdpMetadata newMetadata = model.getConfiguration().getIdpMetadata();
			newMetadata.setSigningCertificates(configuration.getIdpMetadata().getSigningCertificates());
			configuration.setIdpMetadata(newMetadata);
		} else {
			InputStream is = null;
			try {
				is = model.getIdpMetadataFile().getInputStream();
				IdpMetadata newMetadata = IdpMetadata.load(is);
				configuration.setIdpMetadata(newMetadata);
			} catch (SAMLException | IOException e) {
				LOGGER.info("Error loading IDP configuration from metadata file", e);
				errors.rejectValue("idpMetadataFile", "error.idpMetadataFile.import", new Object[]{e.getMessage()}, null);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

		if (!model.getIdpCertificateFile().isEmpty()) {
			InputStream is = null;
			try {
				is = model.getIdpCertificateFile().getInputStream();
				CertificateFactory factory = CertificateFactory.getInstance("X.509", BouncyCastleProvider.PROVIDER_NAME);
				X509Certificate certificate = (X509Certificate) factory.generateCertificate(is);
				final IdpMetadata currentMetadata = configuration.getIdpMetadata();
				currentMetadata.setSigningCertificates(Arrays.asList(certificate));
			} catch (Exception e) {
				LOGGER.info("Error importing IDP certificate", e);
				errors.rejectValue("idpCertificateFile", "error.idpCertificateFile.import", new Object[]{e.getMessage()}, null);
			} finally {
				IOUtils.closeQuietly(is);
			}
		}

	}

}
