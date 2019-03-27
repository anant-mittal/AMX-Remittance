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
package com.gemalto.ics.rnd.sdk.saml.example.model;

import org.springframework.web.multipart.MultipartFile;

import com.gemalto.ics.rnd.sdk.saml.configuration.ClientConfiguration;

/**
 * The {@code ConfigurationModel} class represents a configuration model.
 */
public class ConfigurationModel {

	private MultipartFile spMetadataFile;
	private MultipartFile spPrivateKeyFile;
	private String spPrivateKeyPassword;
	private MultipartFile idpMetadataFile;
	private MultipartFile idpCertificateFile;
	private ClientConfiguration configuration;
	private MultipartFile spEncryptionPrivateKeyFile;
	private String spEncryptionPrivateKeyPassword;

	public MultipartFile getSpMetadataFile() {
		return spMetadataFile;
	}

	public void setSpMetadataFile(MultipartFile spMetadataFile) {
		this.spMetadataFile = spMetadataFile;
	}

	public MultipartFile getSpPrivateKeyFile() {
		return spPrivateKeyFile;
	}

	public void setSpPrivateKeyFile(MultipartFile spPrivateKeyFile) {
		this.spPrivateKeyFile = spPrivateKeyFile;
	}

	public String getSpPrivateKeyPassword() {
		return spPrivateKeyPassword;
	}

	public void setSpPrivateKeyPassword(String spPrivateKeyPassword) {
		this.spPrivateKeyPassword = spPrivateKeyPassword;
	}

	public MultipartFile getIdpMetadataFile() {
		return idpMetadataFile;
	}

	public void setIdpMetadataFile(MultipartFile idpMetadataFile) {
		this.idpMetadataFile = idpMetadataFile;
	}

	public MultipartFile getIdpCertificateFile() {
		return idpCertificateFile;
	}

	public void setIdpCertificateFile(MultipartFile idpCertificateFile) {
		this.idpCertificateFile = idpCertificateFile;
	}

	public ClientConfiguration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(ClientConfiguration configuration) {
		this.configuration = configuration;
	}

	public MultipartFile getSpEncryptionPrivateKeyFile() {
		return spEncryptionPrivateKeyFile;
	}

	public void setSpEncryptionPrivateKeyFile(MultipartFile spEncryptionPrivateKeyFile) {
		this.spEncryptionPrivateKeyFile = spEncryptionPrivateKeyFile;
	}

	public String getSpEncryptionPrivateKeyPassword() {
		return spEncryptionPrivateKeyPassword;
	}

	public void setSpEncryptionPrivateKeyPassword(String spEncryptionPrivateKeyPassword) {
		this.spEncryptionPrivateKeyPassword = spEncryptionPrivateKeyPassword;
	}
}
