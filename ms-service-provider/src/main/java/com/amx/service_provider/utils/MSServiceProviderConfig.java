package com.amx.service_provider.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.properties")
public class MSServiceProviderConfig {
	
	@Value("${home.key.store.location}")
	String homeSendKeyStoreLocation;

	@Value("${home.key.store.password}")
	String homeSendkeyStorePassword;
	
	@Value("${vintja.key.store.location}")
	String vintjaKeyStoreLocation;

	@Value("${vintja.key.store.password}")
	String vintjakeyStorePassword;
	
	@Value("${trust.store.location}")
	String trustStoreLocation;

	@Value("${trust.store.password}")
	String trustStorePassword;

	public String getHomeSendKeyStoreLocation()
	{
		return homeSendKeyStoreLocation;
	}

	public void setHomeSendKeyStoreLocation(String homeSendKeyStoreLocation)
	{
		this.homeSendKeyStoreLocation = homeSendKeyStoreLocation;
	}

	public String getHomeSendkeyStorePassword()
	{
		return homeSendkeyStorePassword;
	}

	public void setHomeSendkeyStorePassword(String homeSendkeyStorePassword)
	{
		this.homeSendkeyStorePassword = homeSendkeyStorePassword;
	}

	public String getVintjaKeyStoreLocation()
	{
		return vintjaKeyStoreLocation;
	}

	public void setVintjaKeyStoreLocation(String vintjaKeyStoreLocation)
	{
		this.vintjaKeyStoreLocation = vintjaKeyStoreLocation;
	}

	public String getVintjakeyStorePassword()
	{
		return vintjakeyStorePassword;
	}

	public void setVintjakeyStorePassword(String vintjakeyStorePassword)
	{
		this.vintjakeyStorePassword = vintjakeyStorePassword;
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

}
