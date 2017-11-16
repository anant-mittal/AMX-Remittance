package com.amx.spservice.client.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Config {

	@Value("${spservice.url}")
	private String spServiceUrl;

	public String getSpServiceUrl() {
		return spServiceUrl;
	}

	public void setSpServiceUrl(String spServiceUrl) {
		this.spServiceUrl = spServiceUrl;
	}
}
