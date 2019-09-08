package com.amx.service_provider.homesend;

import com.amx.service_provider.repository.webservice.OwsParamRespcodeRepository;

public class HomeSendDTO {
	
	private String api_login;
	private String api_password;
	private String vendor_id;
	private String api_url;
	private String keyStoreLocation;
	private String keyStorePassword;
	private String trustStoreLocation;
	private String trustStorePassword;
	private OwsParamRespcodeRepository owsParamRespcodeRepository;
	
	public String getApi_login() {
		return api_login;
	}
	public void setApi_login(String api_login) {
		this.api_login = api_login;
	}
	public String getApi_password() {
		return api_password;
	}
	public void setApi_password(String api_password) {
		this.api_password = api_password;
	}
	public String getVendor_id() {
		return vendor_id;
	}
	public void setVendor_id(String vendor_id) {
		this.vendor_id = vendor_id;
	}
	public String getApi_url() {
		return api_url;
	}
	public void setApi_url(String api_url) {
		this.api_url = api_url;
	}
	public String getKeyStoreLocation() {
		return keyStoreLocation;
	}
	public void setKeyStoreLocation(String keyStoreLocation) {
		this.keyStoreLocation = keyStoreLocation;
	}
	public String getKeyStorePassword() {
		return keyStorePassword;
	}
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}
	public String getTrustStoreLocation() {
		return trustStoreLocation;
	}
	public void setTrustStoreLocation(String trustStoreLocation) {
		this.trustStoreLocation = trustStoreLocation;
	}
	public String getTrustStorePassword() {
		return trustStorePassword;
	}
	public void setTrustStorePassword(String trustStorePassword) {
		this.trustStorePassword = trustStorePassword;
	}
	public OwsParamRespcodeRepository getOwsParamRespcodeRepository() {
		return owsParamRespcodeRepository;
	}
	public void setOwsParamRespcodeRepository(OwsParamRespcodeRepository owsParamRespcodeRepository) {
		this.owsParamRespcodeRepository = owsParamRespcodeRepository;
	}
	
}
